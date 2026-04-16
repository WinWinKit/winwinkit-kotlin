package com.winwinkit.internal.service

import com.winwinkit.client.models.UserClaimCodeRequest
import com.winwinkit.client.models.UserClaimCodeResponseData
import com.winwinkit.client.models.UserCreateRequest
import com.winwinkit.client.models.UserRegisterGooglePlayTransactionRequest
import com.winwinkit.client.models.UserResponseData
import com.winwinkit.client.models.UserWithdrawCreditsRequest
import com.winwinkit.client.models.UserWithdrawCreditsResponseData
import com.winwinkit.internal.providers.ClaimActionsProviderType
import com.winwinkit.internal.providers.GooglePlayTransactionsProviderType
import com.winwinkit.internal.providers.RewardActionsProviderType
import com.winwinkit.internal.providers.UsersProviderType

internal class FakeUsersProvider : UsersProviderType {
    val requests = mutableListOf<UserCreateRequest>()
    var response: UserResponseData? = null
    var error: Throwable? = null

    override suspend fun createOrUpdateUser(request: UserCreateRequest, apiKey: String): UserResponseData {
        requests += request
        error?.let { throw it }
        return checkNotNull(response) { "FakeUsersProvider: no response configured" }
    }
}

internal class FakeClaimActionsProvider : ClaimActionsProviderType {
    data class Call(val request: UserClaimCodeRequest, val appUserId: String)

    val calls = mutableListOf<Call>()
    var response: UserClaimCodeResponseData? = null
    var error: Throwable? = null

    override suspend fun claimCode(
        request: UserClaimCodeRequest,
        appUserId: String,
        apiKey: String,
    ): UserClaimCodeResponseData {
        calls += Call(request, appUserId)
        error?.let { throw it }
        return checkNotNull(response) { "FakeClaimActionsProvider: no response configured" }
    }
}

internal class FakeRewardActionsProvider : RewardActionsProviderType {
    data class Call(val request: UserWithdrawCreditsRequest, val appUserId: String)

    val calls = mutableListOf<Call>()
    var response: UserWithdrawCreditsResponseData? = null
    var error: Throwable? = null

    override suspend fun withdrawCredits(
        request: UserWithdrawCreditsRequest,
        appUserId: String,
        apiKey: String,
    ): UserWithdrawCreditsResponseData {
        calls += Call(request, appUserId)
        error?.let { throw it }
        return checkNotNull(response) { "FakeRewardActionsProvider: no response configured" }
    }
}

internal class FakeGooglePlayTransactionsProvider : GooglePlayTransactionsProviderType {
    data class Call(val request: UserRegisterGooglePlayTransactionRequest, val appUserId: String)

    val calls = mutableListOf<Call>()
    var error: Throwable? = null

    override suspend fun registerTransaction(
        request: UserRegisterGooglePlayTransactionRequest,
        appUserId: String,
        apiKey: String,
    ) {
        calls += Call(request, appUserId)
        error?.let { throw it }
    }
}
