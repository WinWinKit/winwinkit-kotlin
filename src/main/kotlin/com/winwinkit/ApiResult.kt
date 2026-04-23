package com.winwinkit

import com.winwinkit.client.models.ErrorObject

/**
 * The outcome of a [WinWinKit] API call.
 *
 * Exhaustive: every call produces exactly one of [Success], [Failure], or [Exception].
 */
sealed class ApiResult<out T> {
    /** 2xx response; [data] is the parsed body (or `null` when the endpoint treats 404 as absence). */
    data class Success<T>(val data: T) : ApiResult<T>()

    /** 4xx or 5xx response; [errors] is the parsed `ErrorObject` list from the server (empty if the body could not be parsed). */
    data class Failure(val errors: List<ErrorObject>) : ApiResult<Nothing>()

    /** Unexpected throwable (network I/O failure, JSON parse error on a 2xx body, etc.). [cause] preserves the original exception. */
    data class Exception(val cause: Throwable) : ApiResult<Nothing>()
}
