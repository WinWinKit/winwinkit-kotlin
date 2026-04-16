package com.winwinkit.internal.service

import com.winwinkit.client.models.User

internal interface UserServiceDelegate {
    fun canPerformNextRefresh(service: UserService): Boolean
    fun onUserUpdate(service: UserService, user: User)
    fun onError(service: UserService, error: Throwable)
    fun onIsRefreshingChanged(service: UserService, isRefreshing: Boolean)
}
