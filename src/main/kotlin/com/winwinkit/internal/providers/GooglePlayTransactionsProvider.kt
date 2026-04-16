package com.winwinkit.internal.providers

import com.winwinkit.client.apis.UsersApi
import com.winwinkit.client.models.UserRegisterGooglePlayTransactionRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal interface GooglePlayTransactionsProviderType {
    suspend fun registerTransaction(
        request: UserRegisterGooglePlayTransactionRequest,
        appUserId: String,
        apiKey: String,
    )
}

internal class GooglePlayTransactionsProvider(
    private val api: UsersApi = UsersApi(),
) : GooglePlayTransactionsProviderType {
    override suspend fun registerTransaction(
        request: UserRegisterGooglePlayTransactionRequest,
        appUserId: String,
        apiKey: String,
    ) {
        withContext(Dispatchers.IO) {
            api.registerGooglePlayTransaction(
                appUserId = appUserId,
                xApiKey = apiKey,
                userRegisterGooglePlayTransactionRequest = request,
            )
        }
    }
}
