package com.winwinkit

import com.winwinkit.client.infrastructure.ClientError
import com.winwinkit.client.infrastructure.ClientException
import com.winwinkit.client.infrastructure.Serializer
import com.winwinkit.client.infrastructure.ServerError
import com.winwinkit.client.infrastructure.ServerException
import com.winwinkit.client.models.ErrorObject
import com.winwinkit.client.models.ErrorsResponse

/**
 * The error type for [Referrals].
 */
sealed class ReferralsError(message: String) : RuntimeException(message) {

    /** The app user id must be set before using any other methods. */
    object AppUserIdNotSet : ReferralsError("App user id must be set before using any other methods.")

    /**
     * Service is suspended indefinitely.
     *
     * Normally this happens after receiving an unauthorized error from the WinWinKit API.
     * Obtain a new API key and configure [Referrals] with a valid one.
     */
    object SuspendedIndefinitely : ReferralsError("Referrals is suspended indefinitely.")

    /** Request to WinWinKit API has failed with the provided errors. */
    data class RequestFailure(val errors: List<ErrorObject>) :
        ReferralsError("Request failure: $errors")

    /** Returns the API error objects, if this is a [RequestFailure]. */
    val errorObjects: List<ErrorObject>?
        get() = (this as? RequestFailure)?.errors

    internal companion object {
        fun fromApiException(error: Throwable): ReferralsError? {
            val body = when (error) {
                is ClientException -> (error.response as? ClientError<*>)?.body
                is ServerException -> (error.response as? ServerError<*>)?.body
                else -> return null
            } ?: return null

            val json = body as? String ?: return null

            return try {
                val adapter = Serializer.moshi.adapter(ErrorsResponse::class.java)
                val parsed = adapter.fromJson(json)
                RequestFailure(parsed?.errors ?: emptyList())
            } catch (_: Throwable) {
                RequestFailure(emptyList())
            }
        }
    }
}
