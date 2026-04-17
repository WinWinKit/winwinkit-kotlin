package com.winwinkit

import com.winwinkit.client.models.ErrorObject

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Failure(val errors: List<ErrorObject>) : ApiResult<Nothing>()
}
