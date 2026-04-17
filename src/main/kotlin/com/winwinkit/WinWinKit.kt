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
 */
class WinWinKit(
    private val apiKey: String,
    baseUrl: String = DEFAULT_BASE_URL,
) {
    private val usersApi = UsersApi(baseUrl)
    private val claimActionsApi = ClaimActionsApi(baseUrl)
    private val rewardsActionsApi = RewardsActionsApi(baseUrl)

    suspend fun fetchUser(appUserId: String): WinWinKitResult<User?> = call(treat404AsNull = true) {
        usersApi.getUser(appUserId, apiKey).data.user
    }

    suspend fun createOrUpdateUser(
        appUserId: String,
        isPremium: Boolean? = null,
        isTrial: Boolean? = null,
        firstSeenAt: OffsetDateTime? = null,
        metadata: Any? = null,
        stripeCustomerId: String? = null,
    ): WinWinKitResult<User> = call {
        val request = UserCreateRequest(
            appUserId = appUserId,
            isTrial = isTrial,
            isPremium = isPremium,
            firstSeenAt = firstSeenAt,
            metadata = metadata,
            stripeCustomerId = stripeCustomerId,
        )
        usersApi.createOrUpdateUser(apiKey, request).data.user
    }

    suspend fun claimCode(
        appUserId: String,
        code: String,
    ): WinWinKitResult<UserClaimCodeResponseData> = call {
        claimActionsApi.claimCode(appUserId, apiKey, UserClaimCodeRequest(code)).data
    }

    suspend fun withdrawCredits(
        appUserId: String,
        key: String,
        amount: Int,
        operationId: String? = null,
    ): WinWinKitResult<UserWithdrawCreditsResponseData> = call {
        val request = UserWithdrawCreditsRequest(key = key, amount = amount, operationId = operationId)
        rewardsActionsApi.withdrawCredits(appUserId, apiKey, request).data
    }

    suspend fun grantReward(
        appUserId: String,
        key: String,
        operationId: String? = null,
    ): WinWinKitResult<UserGrantRewardResponseData> = call {
        val request = UserGrantRewardRequest(key = key, operationId = operationId)
        rewardsActionsApi.grantReward(appUserId, apiKey, request).data
    }

    suspend fun registerAppStoreTransaction(
        appUserId: String,
        originalTransactionId: String,
        appAccountToken: String? = null,
    ): WinWinKitResult<Unit> = call {
        val request = UserRegisterAppStoreTransactionRequest(
            originalTransactionId = originalTransactionId,
            appAccountToken = appAccountToken,
        )
        usersApi.registerAppStoreTransaction(appUserId, apiKey, request)
    }

    suspend fun registerGooglePlayTransaction(
        appUserId: String,
        purchaseToken: String,
        obfuscatedExternalAccountId: String? = null,
    ): WinWinKitResult<Unit> = call {
        val request = UserRegisterGooglePlayTransactionRequest(
            purchaseToken = purchaseToken,
            obfuscatedExternalAccountId = obfuscatedExternalAccountId,
        )
        usersApi.registerGooglePlayTransaction(appUserId, apiKey, request)
    }

    private suspend inline fun <T> call(
        treat404AsNull: Boolean = false,
        crossinline block: () -> T,
    ): WinWinKitResult<T> = withContext(Dispatchers.IO) {
        try {
            WinWinKitResult.Success(block())
        } catch (e: ClientException) {
            if (treat404AsNull && e.statusCode == 404) {
                @Suppress("UNCHECKED_CAST")
                WinWinKitResult.Success(null as T)
            } else {
                WinWinKitResult.Failure(parseErrors(e))
            }
        } catch (e: ServerException) {
            WinWinKitResult.Failure(parseErrors(e))
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
