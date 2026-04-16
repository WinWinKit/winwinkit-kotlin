package com.winwinkit

import com.winwinkit.cache.InMemoryKeyValueCache
import com.winwinkit.cache.KeyValueCache
import com.winwinkit.client.apis.ClaimActionsApi
import com.winwinkit.client.apis.RewardsActionsApi
import com.winwinkit.client.apis.UsersApi
import com.winwinkit.client.infrastructure.ApiClient
import com.winwinkit.client.models.User
import com.winwinkit.client.models.UserRewardsGranted
import com.winwinkit.client.models.UserWithdrawCreditsResult
import com.winwinkit.internal.cache.UserCache
import com.winwinkit.internal.cache.UserCacheType
import com.winwinkit.internal.cache.reset
import com.winwinkit.internal.logging.Logger
import com.winwinkit.internal.providers.ClaimActionsProvider
import com.winwinkit.internal.providers.GooglePlayTransactionsProvider
import com.winwinkit.internal.providers.RewardActionsProvider
import com.winwinkit.internal.providers.UsersProvider
import com.winwinkit.internal.service.UserService
import com.winwinkit.internal.service.UserServiceDelegate
import com.winwinkit.internal.util.isEqualToSeconds
import java.time.OffsetDateTime
import java.util.logging.Level
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Entry point for the WinWinKit SDK.
 *
 * Configure the SDK once at startup with [configure], then access the singleton through
 * [Referrals.shared]. Set the user's identity with [setAppUserId] before calling any other
 * method. Observe updates via [delegate] or the reactive [state].
 */
class Referrals private constructor(
    private val apiKey: String,
    private val providers: UserService.Providers,
    private val userCache: UserCacheType,
) {

    companion object {
        @Volatile
        private var instance: Referrals? = null
        private val lock = Any()

        /**
         * Initializes the Referrals SDK. Calling more than once has no effect.
         *
         * @param apiKey Your WinWinKit API key.
         * @param keyValueCache Persistence backend. Defaults to in-memory storage; provide a
         *   [KeyValueCache] backed by `SharedPreferences` or `DataStore` on Android to persist
         *   state across process restarts.
         * @param logLevel Minimum log level. Defaults to [Level.INFO].
         * @param baseEndpointUrl Override for the API endpoint. Defaults to the production URL.
         */
        @JvmStatic
        @JvmOverloads
        fun configure(
            apiKey: String,
            keyValueCache: KeyValueCache = InMemoryKeyValueCache(),
            logLevel: Level = Level.INFO,
            baseEndpointUrl: String? = null,
        ): Referrals {
            synchronized(lock) {
                val existing = instance
                if (existing != null) {
                    Logger.error("Referrals has already been configured. Calling `configure` again has no effect.")
                    return existing
                }
                Logger.logLevel = logLevel
                if (baseEndpointUrl != null) {
                    System.setProperty(ApiClient.baseUrlKey, baseEndpointUrl)
                }
                val usersApi = UsersApi(baseEndpointUrl ?: UsersApi.defaultBasePath)
                val created = Referrals(
                    apiKey = apiKey,
                    providers = UserService.Providers(
                        claimActions = ClaimActionsProvider(ClaimActionsApi(baseEndpointUrl ?: ClaimActionsApi.defaultBasePath)),
                        googlePlayTransactions = GooglePlayTransactionsProvider(usersApi),
                        rewardActions = RewardActionsProvider(RewardsActionsApi(baseEndpointUrl ?: RewardsActionsApi.defaultBasePath)),
                        users = UsersProvider(usersApi),
                    ),
                    userCache = UserCache(keyValueCache),
                )
                instance = created
                return created
            }
        }

        /**
         * Returns the configured singleton. Throws [IllegalStateException] if [configure] has not
         * been called. Use [isConfigured] to check first.
         */
        @JvmStatic
        val shared: Referrals
            get() = instance
                ?: error("Referrals has not been configured yet. To get started call `Referrals.configure(apiKey = ...)`.")

        @JvmStatic
        val isConfigured: Boolean get() = instance != null

        internal fun resetForTesting() {
            synchronized(lock) {
                instance?.userService?.shutdown()
                instance = null
            }
        }
    }

    @Volatile
    private var userService: UserService? = null

    /** Observable state for reactive UI binding. */
    val state: ReferralsState = ReferralsState().also { obs ->
        obs.onClaimCode = { code -> claimCode(code) { /* no-op: state is updated via delegate */ } }
    }

    /** Delegate receiving [ReferralsDelegate] callbacks. Weakly held in Swift; strong in Kotlin. */
    var delegate: ReferralsDelegate? = null
        set(value) {
            if (value === field) {
                Logger.warning("Referrals delegate has already been set.")
                return
            }
            if (value == null) {
                Logger.info("Referrals delegate is being set to null, you probably don't want to do this.")
            }
            field = value
            if (value != null) {
                Logger.debug("Referrals delegate is set.")
            }
        }

    /** Returns the latest cached [User], or null if none is available. */
    val user: User? get() = userService?.cachedUser

    /**
     * Sets your app's unique identifier for the current user.
     *
     * Referral program and rewards will be attached to [appUserId]. Use a UUID or similar
     * random identifier — avoid person-identifying information like email or name. Must be
     * called before any other [set*][setIsPremium] or [claimCode]/[withdrawCredits] call.
     */
    fun setAppUserId(appUserId: String) {
        val service = UserService(
            appUserId = appUserId,
            apiKey = apiKey,
            providers = providers,
            userCache = userCache,
        )
        userService?.shutdown()
        userService = service
        service.delegate = UserServiceDelegateImpl()
        service.refresh()
    }

    fun setIsPremium(isPremium: Boolean) {
        val service = userService ?: run {
            Logger.warning("User identifier `appUserId` must be set before updating any other user properties.")
            return
        }
        if (service.cachedUser?.isPremium == isPremium) return
        service.setIsPremium(isPremium)
        service.refresh()
    }

    fun setIsTrial(isTrial: Boolean) {
        val service = userService ?: run {
            Logger.warning("User identifier `appUserId` must be set before updating any other user properties.")
            return
        }
        if (service.cachedUser?.isTrial == isTrial) return
        service.setIsTrial(isTrial)
        service.refresh()
    }

    fun setFirstSeenAt(firstSeenAt: OffsetDateTime) {
        val service = userService ?: run {
            Logger.warning("User identifier `appUserId` must be set before updating any other user properties.")
            return
        }
        if (firstSeenAt.isAfter(OffsetDateTime.now())) {
            Logger.warning("First seen at date must not be in the future.")
            return
        }
        if (firstSeenAt.isEqualToSeconds(service.cachedUser?.firstSeenAt)) return
        service.setFirstSeenAt(firstSeenAt)
        service.refresh()
    }

    fun setMetadata(metadata: Any?) {
        val service = userService ?: run {
            Logger.warning("User identifier `appUserId` must be set before updating any other user properties.")
            return
        }
        if (service.cachedUser?.metadata == metadata) return
        service.setMetadata(metadata)
        service.refresh()
    }

    fun setStripeCustomerId(stripeCustomerId: String?) {
        val service = userService ?: run {
            Logger.warning("User identifier `appUserId` must be set before updating any other user properties.")
            return
        }
        if (service.cachedUser?.stripeCustomerId == stripeCustomerId) return
        service.setStripeCustomerId(stripeCustomerId)
        service.refresh()
    }

    /** Result of [claimCode]. */
    data class ClaimCodeResult(val user: User, val rewardsGranted: UserRewardsGranted)

    /** Result of [withdrawCredits]. */
    data class WithdrawCreditsResult(val user: User, val withdrawResult: UserWithdrawCreditsResult)

    /**
     * Claims an affiliate, promo, or referral code.
     *
     * @throws ReferralsError.AppUserIdNotSet if [setAppUserId] has not been called.
     * @throws ReferralsError.SuspendedIndefinitely after a prior API-key auth failure.
     * @throws ReferralsError.RequestFailure when the API returns an error.
     */
    suspend fun claimCode(code: String): ClaimCodeResult =
        suspendCancellableCoroutine { cont ->
            claimCode(code) { result ->
                result.fold(
                    onSuccess = { cont.resume(it) },
                    onFailure = { cont.resumeWithException(it) },
                )
            }
        }

    /** Callback-style variant of [claimCode]. */
    fun claimCode(code: String, completion: (Result<ClaimCodeResult>) -> Unit) {
        val service = userService ?: run {
            Logger.warning("User identifier `appUserId` must be set before claiming code.")
            completion(Result.failure(ReferralsError.AppUserIdNotSet))
            return
        }
        state.setClaimCodeState(ReferralsState.ClaimCodeState.Loading)
        service.claimCode(code) { result ->
            result.fold(
                onSuccess = { data ->
                    state.setClaimCodeState(ReferralsState.ClaimCodeState.Success(data.rewardsGranted))
                    completion(Result.success(ClaimCodeResult(data.user, data.rewardsGranted)))
                },
                onFailure = { error ->
                    state.setClaimCodeState(ReferralsState.ClaimCodeState.Failure(error))
                    completion(Result.failure(error))
                },
            )
        }
    }

    /**
     * Withdraws credits of the given [key] by [amount].
     *
     * @throws ReferralsError.AppUserIdNotSet if [setAppUserId] has not been called.
     * @throws ReferralsError.SuspendedIndefinitely after a prior API-key auth failure.
     * @throws ReferralsError.RequestFailure when the API returns an error.
     */
    suspend fun withdrawCredits(key: String, amount: Int): WithdrawCreditsResult =
        suspendCancellableCoroutine { cont ->
            withdrawCredits(key, amount) { result ->
                result.fold(
                    onSuccess = { cont.resume(it) },
                    onFailure = { cont.resumeWithException(it) },
                )
            }
        }

    /** Callback-style variant of [withdrawCredits]. */
    fun withdrawCredits(key: String, amount: Int, completion: (Result<WithdrawCreditsResult>) -> Unit) {
        val service = userService ?: run {
            Logger.warning("User identifier `appUserId` must be set before withdrawing credits.")
            completion(Result.failure(ReferralsError.AppUserIdNotSet))
            return
        }
        service.withdrawCredits(key, amount) { result ->
            result.fold(
                onSuccess = { data -> completion(Result.success(WithdrawCreditsResult(data.user, data.withdrawResult))) },
                onFailure = { error -> completion(Result.failure(error)) },
            )
        }
    }

    /** Triggers an internal refresh of the current user. */
    fun refresh() {
        userService?.refresh()
    }

    /**
     * Registers a Google Play purchase with the WinWinKit backend for direct revenue tracking.
     * Call this after Google Play confirms a successful purchase or subscription. The purchase
     * is deduplicated — calling again with the same [purchaseToken] is a no-op.
     *
     * The call is fire-and-forget: errors are delivered to the [delegate] and surfaced on
     * [state], but not returned to the caller. Requires [setAppUserId] to have been called.
     *
     * @param purchaseToken The `purchaseToken` returned by the Google Play Billing flow.
     * @param obfuscatedExternalAccountId Optional obfuscated account id set via
     *   `BillingFlowParams.setObfuscatedAccountId()` for better matching.
     */
    @JvmOverloads
    fun syncTransactions(purchaseToken: String, obfuscatedExternalAccountId: String? = null) {
        val service = userService ?: run {
            Logger.warning("User identifier `appUserId` must be set before syncing transactions.")
            return
        }
        service.syncGooglePlayTransaction(
            purchaseToken = purchaseToken,
            obfuscatedExternalAccountId = obfuscatedExternalAccountId,
        )
    }

    /** Resets internal state attached to the previously set `appUserId`. */
    fun reset() {
        userService?.shutdown()
        userService = null
        userCache.reset()
        state.setUser(null)
        state.setUserState(ReferralsState.UserState.None)
        state.setClaimCodeState(ReferralsState.ClaimCodeState.None)
        delegate?.onUserUpdate(this, null)
    }

    private inner class UserServiceDelegateImpl : UserServiceDelegate {
        override fun canPerformNextRefresh(service: UserService): Boolean = true

        override fun onUserUpdate(service: UserService, user: User) {
            state.setUser(user)
            state.setUserState(ReferralsState.UserState.Available)
            delegate?.onUserUpdate(this@Referrals, user)
        }

        override fun onError(service: UserService, error: Throwable) {
            state.setUserState(ReferralsState.UserState.Failure(error))
            delegate?.onError(this@Referrals, error)
        }

        override fun onIsRefreshingChanged(service: UserService, isRefreshing: Boolean) {
            if (isRefreshing) {
                state.setUserState(ReferralsState.UserState.Loading)
            }
        }
    }
}
