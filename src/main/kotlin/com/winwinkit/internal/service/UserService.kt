package com.winwinkit.internal.service

import com.winwinkit.ReferralsError
import com.winwinkit.client.infrastructure.ClientException
import com.winwinkit.client.models.User
import com.winwinkit.client.models.UserClaimCodeRequest
import com.winwinkit.client.models.UserClaimCodeResponseData
import com.winwinkit.client.models.UserCreateRequest
import com.winwinkit.client.models.UserRegisterGooglePlayTransactionRequest
import com.winwinkit.client.models.UserWithdrawCreditsRequest
import com.winwinkit.client.models.UserWithdrawCreditsResponseData
import com.winwinkit.internal.cache.UserCacheType
import com.winwinkit.internal.cache.reset
import com.winwinkit.internal.logging.Logger
import com.winwinkit.internal.model.UserUpdate
import com.winwinkit.internal.providers.ClaimActionsProviderType
import com.winwinkit.internal.providers.GooglePlayTransactionsProviderType
import com.winwinkit.internal.providers.RewardActionsProviderType
import com.winwinkit.internal.providers.UsersProviderType
import java.time.OffsetDateTime
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class UserService(
    val appUserId: String,
    val apiKey: String,
    val providers: Providers,
    val userCache: UserCacheType,
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    data class Providers(
        val claimActions: ClaimActionsProviderType,
        val googlePlayTransactions: GooglePlayTransactionsProviderType,
        val rewardActions: RewardActionsProviderType,
        val users: UsersProviderType,
    )

    var delegate: UserServiceDelegate? = null

    private val scope = CoroutineScope(SupervisorJob() + dispatcher)
    private val refreshMutex = Mutex()

    @Volatile
    private var refreshJob: Job? = null

    @Volatile
    var isRefreshing: Boolean = false
        private set(value) {
            field = value
            delegate?.onIsRefreshingChanged(this, value)
        }

    @Volatile
    var shouldSuspendIndefinitely: Boolean = false
        private set

    init {
        val cached = userCache.user
        if (cached != null && cached.appUserId != appUserId) {
            userCache.registeredGooglePlayPurchaseTokens = null
        }
        if (cachedUser == null) {
            userCache.userUpdate = UserUpdate(appUserId = appUserId)
        }
    }

    val cachedUser: User?
        get() = userCache.user?.takeIf { it.appUserId == appUserId }

    private var pendingUserUpdate: UserUpdate?
        get() = userCache.userUpdate?.takeIf { it.appUserId == appUserId }
        set(value) {
            userCache.userUpdate = value
        }

    fun shutdown() {
        scope.cancel()
    }

    fun setIsPremium(value: Boolean) {
        Logger.debug("UserService: Set isPremium value")
        pendingUserUpdate = (pendingUserUpdate ?: UserUpdate(appUserId)).copy(isPremium = value)
    }

    fun setIsTrial(value: Boolean) {
        Logger.debug("UserService: Set isTrial value")
        pendingUserUpdate = (pendingUserUpdate ?: UserUpdate(appUserId)).copy(isTrial = value)
    }

    fun setFirstSeenAt(value: OffsetDateTime) {
        Logger.debug("UserService: Set firstSeenAt value")
        pendingUserUpdate = (pendingUserUpdate ?: UserUpdate(appUserId)).copy(firstSeenAt = value)
    }

    fun setMetadata(value: Any?) {
        Logger.debug("UserService: Set metadata value")
        pendingUserUpdate = (pendingUserUpdate ?: UserUpdate(appUserId)).copy(metadata = value)
    }

    fun setStripeCustomerId(value: String?) {
        Logger.debug("UserService: Set stripeCustomerId value")
        pendingUserUpdate = (pendingUserUpdate ?: UserUpdate(appUserId)).copy(stripeCustomerId = value)
    }

    fun refresh() {
        if (shouldSuspendIndefinitely) {
            Logger.debug("UserService: Refresh suspended indefinitely")
            delegate?.onError(this, ReferralsError.SuspendedIndefinitely)
            return
        }

        delegate?.let {
            if (!it.canPerformNextRefresh(this)) {
                Logger.debug("UserService: Refresh not allowed")
                return
            }
        }

        if (refreshJob?.isActive == true) {
            Logger.debug("UserService: Refresh already in progress")
            return
        }

        refreshJob = scope.launch {
            refreshMutex.withLock { performRefresh() }
        }
    }

    private suspend fun performRefresh() {
        Logger.debug("UserService: Refresh will start")
        isRefreshing = true

        var completedSuccessfully = false
        try {
            val pending = pendingUserUpdate
            val request = UserCreateRequest(
                appUserId = appUserId,
                isTrial = pending?.isTrial,
                isPremium = pending?.isPremium,
                firstSeenAt = pending?.firstSeenAt,
                metadata = pending?.metadata,
                stripeCustomerId = pending?.stripeCustomerId,
            )
            val data = providers.users.createOrUpdateUser(request, apiKey)
            resetUserUpdate(matching = pending)
            cacheUser(data.user)
            completedSuccessfully = true
            Logger.debug("UserService: Refresh did finish")
        } catch (e: Throwable) {
            Logger.debug("UserService: Refresh did fail")
            val mapped = ReferralsError.fromApiException(e) ?: e
            Logger.error("Failed to refresh user: $mapped")
            handleTaskError(mapped, originalError = e)
        } finally {
            isRefreshing = false
            refreshJob = null
        }

        if (completedSuccessfully && pendingUserUpdate != null) {
            Logger.debug("UserService: Refresh will start again")
            refresh()
        }
    }

    fun claimCode(code: String, completion: (Result<UserClaimCodeResponseData>) -> Unit) {
        if (shouldSuspendIndefinitely) {
            Logger.debug("UserService: Claim code suspended indefinitely")
            completion(Result.failure(ReferralsError.SuspendedIndefinitely))
            return
        }
        scope.launch {
            try {
                val response = providers.claimActions.claimCode(
                    request = UserClaimCodeRequest(code = code),
                    appUserId = appUserId,
                    apiKey = apiKey,
                )
                Logger.debug("UserService: Claim code did finish")
                cacheUser(response.user)
                completion(Result.success(response))
            } catch (e: Throwable) {
                Logger.debug("UserService: Claim code did fail")
                val mapped = ReferralsError.fromApiException(e) ?: e
                Logger.error("Failed to claim code: $mapped")
                handleTaskError(mapped, originalError = e)
                completion(Result.failure(mapped))
            }
        }
    }

    fun withdrawCredits(key: String, amount: Int, completion: (Result<UserWithdrawCreditsResponseData>) -> Unit) {
        if (shouldSuspendIndefinitely) {
            Logger.debug("UserService: Withdraw credits suspended indefinitely")
            completion(Result.failure(ReferralsError.SuspendedIndefinitely))
            return
        }
        scope.launch {
            try {
                val response = providers.rewardActions.withdrawCredits(
                    request = UserWithdrawCreditsRequest(key = key, amount = amount),
                    appUserId = appUserId,
                    apiKey = apiKey,
                )
                Logger.debug("UserService: Withdraw credits did finish")
                cacheUser(response.user)
                completion(Result.success(response))
            } catch (e: Throwable) {
                Logger.debug("UserService: Withdraw credits did fail")
                val mapped = ReferralsError.fromApiException(e) ?: e
                Logger.error("Failed to withdraw credits: $mapped")
                handleTaskError(mapped, originalError = e)
                completion(Result.failure(mapped))
            }
        }
    }

    /**
     * Registers a Google Play purchase token with the backend if it hasn't already been registered
     * for this user. Errors are logged and dispatched to the delegate, but not returned.
     */
    fun syncGooglePlayTransaction(purchaseToken: String, obfuscatedExternalAccountId: String?) {
        if (shouldSuspendIndefinitely) {
            Logger.debug("UserService: Sync Google Play transaction suspended indefinitely")
            return
        }
        val alreadyRegistered = userCache.registeredGooglePlayPurchaseTokens?.contains(purchaseToken) == true
        if (alreadyRegistered) {
            Logger.debug("UserService: Google Play purchase token already registered, skipping")
            return
        }
        scope.launch {
            try {
                providers.googlePlayTransactions.registerTransaction(
                    request = UserRegisterGooglePlayTransactionRequest(
                        purchaseToken = purchaseToken,
                        obfuscatedExternalAccountId = obfuscatedExternalAccountId,
                    ),
                    appUserId = appUserId,
                    apiKey = apiKey,
                )
                Logger.debug("UserService: Google Play transaction registered")
                val existing = userCache.registeredGooglePlayPurchaseTokens.orEmpty()
                userCache.registeredGooglePlayPurchaseTokens = existing + purchaseToken
            } catch (e: Throwable) {
                val mapped = ReferralsError.fromApiException(e) ?: e
                Logger.error("Failed to register Google Play transaction: $mapped")
                handleTaskError(mapped, originalError = e)
            }
        }
    }

    private fun cacheUser(user: User) {
        userCache.user = user
        delegate?.onUserUpdate(this, user)
    }

    private fun resetUserUpdate(matching: UserUpdate?) {
        if (userCache.userUpdate == matching) {
            userCache.userUpdate = null
        }
    }

    private fun handleTaskError(error: Throwable, originalError: Throwable) {
        if (originalError is ClientException && originalError.statusCode == 401) {
            handleUnauthorizedError()
        }
        delegate?.onError(this, error)
    }

    private fun handleUnauthorizedError() {
        userCache.reset()
        shouldSuspendIndefinitely = true
        Logger.error(
            "Authorization with the provided API key has failed! " +
                "Please obtain a new API key and use it when initializing the Referrals object."
        )
    }
}
