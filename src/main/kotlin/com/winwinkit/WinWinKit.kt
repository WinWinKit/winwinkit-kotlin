package com.winwinkit

import com.winwinkit.client.apis.ClaimActionsApi
import com.winwinkit.client.apis.RewardsActionsApi
import com.winwinkit.client.apis.UsersApi
import com.winwinkit.client.infrastructure.ClientError
import com.winwinkit.client.infrastructure.ClientException
import com.winwinkit.client.infrastructure.Serializer
import com.winwinkit.client.infrastructure.ServerError
import com.winwinkit.client.infrastructure.ServerException
import com.winwinkit.client.models.ErrorObject
import com.winwinkit.client.models.ErrorsResponse
import com.winwinkit.client.models.User
import com.winwinkit.client.models.UserClaimCodeRequest
import com.winwinkit.client.models.UserClaimCodeResponseData
import com.winwinkit.client.models.UserCreateRequest
import com.winwinkit.client.models.UserGrantRewardRequest
import com.winwinkit.client.models.UserGrantRewardResponseData
import com.winwinkit.client.models.UserRegisterAppStoreTransactionRequest
import com.winwinkit.client.models.UserRegisterGooglePlayTransactionRequest
import com.winwinkit.client.models.UserWithdrawCreditsRequest
import com.winwinkit.client.models.UserWithdrawCreditsResponseData
import java.time.OffsetDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * WinWinKit client. A thin wrapper around the REST API.
 *
 * Set [appUserId] after construction (e.g. on login) before calling any method.
 * Clear it to `null` on logout. Calling any method while [appUserId] is `null`
 * throws [IllegalStateException].
 */
class WinWinKit(
    private val apiKey: String,
    baseUrl: String = DEFAULT_BASE_URL,
) {
    @Volatile
    var appUserId: String? = null

    private val usersApi = UsersApi(baseUrl)
    private val claimActionsApi = ClaimActionsApi(baseUrl)
    private val rewardsActionsApi = RewardsActionsApi(baseUrl)

    suspend fun fetchUser(): ApiResult<User?> = call(treat404AsNull = true) {
        usersApi.getUser(requireAppUserId(), apiKey).data.user
    }

    suspend fun createOrUpdateUser(
        isPremium: Boolean? = null,
        isTrial: Boolean? = null,
        firstSeenAt: OffsetDateTime? = null,
        metadata: Any? = null,
        stripeCustomerId: String? = null,
    ): ApiResult<User> = call {
        val request = UserCreateRequest(
            appUserId = requireAppUserId(),
            isTrial = isTrial,
            isPremium = isPremium,
            firstSeenAt = firstSeenAt,
            metadata = metadata,
            stripeCustomerId = stripeCustomerId,
        )
        usersApi.createOrUpdateUser(apiKey, request).data.user
    }

    suspend fun claimCode(
        code: String,
    ): ApiResult<UserClaimCodeResponseData> = call {
        claimActionsApi.claimCode(requireAppUserId(), apiKey, UserClaimCodeRequest(code)).data
    }

    suspend fun withdrawCredits(
        key: String,
        amount: Int,
        operationId: String? = null,
    ): ApiResult<UserWithdrawCreditsResponseData> = call {
        val request = UserWithdrawCreditsRequest(key = key, amount = amount, operationId = operationId)
        rewardsActionsApi.withdrawCredits(requireAppUserId(), apiKey, request).data
    }

    suspend fun grantReward(
        key: String,
        operationId: String? = null,
    ): ApiResult<UserGrantRewardResponseData> = call {
        val request = UserGrantRewardRequest(key = key, operationId = operationId)
        rewardsActionsApi.grantReward(requireAppUserId(), apiKey, request).data
    }

    suspend fun registerAppStoreTransaction(
        originalTransactionId: String,
        appAccountToken: String? = null,
    ): ApiResult<Unit> = call {
        val request = UserRegisterAppStoreTransactionRequest(
            originalTransactionId = originalTransactionId,
            appAccountToken = appAccountToken,
        )
        usersApi.registerAppStoreTransaction(requireAppUserId(), apiKey, request)
    }

    suspend fun registerGooglePlayTransaction(
        purchaseToken: String,
        obfuscatedExternalAccountId: String? = null,
    ): ApiResult<Unit> = call {
        val request = UserRegisterGooglePlayTransactionRequest(
            purchaseToken = purchaseToken,
            obfuscatedExternalAccountId = obfuscatedExternalAccountId,
        )
        usersApi.registerGooglePlayTransaction(requireAppUserId(), apiKey, request)
    }

    private fun requireAppUserId(): String = appUserId
        ?: error("WinWinKit.appUserId must be set before making API calls")

    private suspend inline fun <T> call(
        treat404AsNull: Boolean = false,
        crossinline block: () -> T,
    ): ApiResult<T> = withContext(Dispatchers.IO) {
        try {
            ApiResult.Success(block())
        } catch (e: ClientException) {
            if (treat404AsNull && e.statusCode == 404) {
                @Suppress("UNCHECKED_CAST")
                ApiResult.Success(null as T)
            } else {
                ApiResult.Failure(parseErrors(e))
            }
        } catch (e: ServerException) {
            ApiResult.Failure(parseErrors(e))
        }
    }

    private fun parseErrors(error: Throwable): List<ErrorObject> {
        val body = when (error) {
            is ClientException -> (error.response as? ClientError<*>)?.body
            is ServerException -> (error.response as? ServerError<*>)?.body
            else -> null
        } as? String ?: return emptyList()

        return try {
            val adapter = Serializer.moshi.adapter(ErrorsResponse::class.java)
            adapter.fromJson(body)?.errors ?: emptyList()
        } catch (_: Throwable) {
            emptyList()
        }
    }

    companion object {
        const val DEFAULT_BASE_URL: String = "https://api.winwinkit.com"
    }
}
