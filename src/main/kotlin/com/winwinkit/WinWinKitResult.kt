package com.winwinkit

import com.winwinkit.client.models.ErrorObject

sealed class WinWinKitResult<out T> {
    data class Success<T>(val data: T) : WinWinKitResult<T>()
    data class Failure(val errors: List<ErrorObject>) : WinWinKitResult<Nothing>()
}
