package com.winwinkit.internal.providers

import com.winwinkit.client.apis.UsersApi
import com.winwinkit.client.models.UserCreateRequest
import com.winwinkit.client.models.UserResponseData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal interface UsersProviderType {
    suspend fun createOrUpdateUser(request: UserCreateRequest, apiKey: String): UserResponseData
}

internal class UsersProvider(private val api: UsersApi = UsersApi()) : UsersProviderType {
    override suspend fun createOrUpdateUser(request: UserCreateRequest, apiKey: String): UserResponseData =
        withContext(Dispatchers.IO) {
            api.createOrUpdateUser(xApiKey = apiKey, userCreateRequest = request).data
        }
}
