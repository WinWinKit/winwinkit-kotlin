package com.winwinkit.internal.providers

import com.winwinkit.client.apis.RewardsActionsApi
import com.winwinkit.client.models.UserWithdrawCreditsRequest
import com.winwinkit.client.models.UserWithdrawCreditsResponseData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal interface RewardActionsProviderType {
    suspend fun withdrawCredits(
        request: UserWithdrawCreditsRequest,
        appUserId: String,
        apiKey: String,
    ): UserWithdrawCreditsResponseData
}

internal class RewardActionsProvider(
    private val api: RewardsActionsApi = RewardsActionsApi()
) : RewardActionsProviderType {
    override suspend fun withdrawCredits(
        request: UserWithdrawCreditsRequest,
        appUserId: String,
        apiKey: String,
    ): UserWithdrawCreditsResponseData =
        withContext(Dispatchers.IO) {
            api.withdrawCredits(
                appUserId = appUserId,
                xApiKey = apiKey,
                userWithdrawCreditsRequest = request,
            ).data
        }
}
