package com.winwinkit

import com.winwinkit.client.models.ErrorObject

sealed class ReferralsResult<out T> {
    data class Success<T>(val data: T) : ReferralsResult<T>()
    data class Failure(val errors: List<ErrorObject>) : ReferralsResult<Nothing>()
}
