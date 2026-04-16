package com.winwinkit.internal.providers

import com.winwinkit.client.apis.ClaimActionsApi
import com.winwinkit.client.models.UserClaimCodeRequest
import com.winwinkit.client.models.UserClaimCodeResponseData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal interface ClaimActionsProviderType {
    suspend fun claimCode(
        request: UserClaimCodeRequest,
        appUserId: String,
        apiKey: String,
    ): UserClaimCodeResponseData
}

internal class ClaimActionsProvider(
    private val api: ClaimActionsApi = ClaimActionsApi()
) : ClaimActionsProviderType {
    override suspend fun claimCode(
        request: UserClaimCodeRequest,
        appUserId: String,
        apiKey: String,
    ): UserClaimCodeResponseData =
        withContext(Dispatchers.IO) {
            api.claimCode(appUserId = appUserId, xApiKey = apiKey, userClaimCodeRequest = request).data
        }
}
