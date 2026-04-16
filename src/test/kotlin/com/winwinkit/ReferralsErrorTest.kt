package com.winwinkit

import com.winwinkit.client.infrastructure.ClientError
import com.winwinkit.client.infrastructure.ClientException
import com.winwinkit.client.infrastructure.ServerError
import com.winwinkit.client.infrastructure.ServerException
import com.winwinkit.client.models.ErrorObject
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class ReferralsErrorTest : StringSpec({

    val sampleError = ErrorObject(code = "invalid_code", status = 400, message = "no match", source = null)

    "AppUserIdNotSet has no error objects" {
        val error: ReferralsError = ReferralsError.AppUserIdNotSet
        error.errorObjects shouldBe null
        error.message shouldBe "App user id must be set before using any other methods."
    }

    "SuspendedIndefinitely has no error objects" {
        val error: ReferralsError = ReferralsError.SuspendedIndefinitely
        error.errorObjects shouldBe null
    }

    "RequestFailure exposes its error list via errorObjects" {
        val error: ReferralsError = ReferralsError.RequestFailure(listOf(sampleError))
        error.errorObjects shouldBe listOf(sampleError)
    }

    "RequestFailure equality is data-class-based" {
        val a = ReferralsError.RequestFailure(listOf(sampleError))
        val b = ReferralsError.RequestFailure(listOf(sampleError))
        (a == b) shouldBe true
    }

    "fromApiException parses ClientException with ErrorsResponse body" {
        val body = """{"errors":[{"code":"not_found","status":404,"message":"user missing","source":null}]}"""
        val response = ClientError<Any>(message = "not found", body = body, statusCode = 404)
        val exception = ClientException(message = "Client error : 404", statusCode = 404, response = response)

        val mapped = ReferralsError.fromApiException(exception)

        (mapped is ReferralsError.RequestFailure) shouldBe true
        (mapped as ReferralsError.RequestFailure).errors.first().code shouldBe "not_found"
    }

    "fromApiException parses ServerException with ErrorsResponse body" {
        val body = """{"errors":[{"code":"internal","status":500,"message":"boom","source":null}]}"""
        val response = ServerError<Any>(message = "oops", body = body, statusCode = 500)
        val exception = ServerException(message = "Server error : 500", statusCode = 500, response = response)

        val mapped = ReferralsError.fromApiException(exception)

        (mapped is ReferralsError.RequestFailure) shouldBe true
        (mapped as ReferralsError.RequestFailure).errors.single().code shouldBe "internal"
    }

    "fromApiException returns RequestFailure with empty errors when body is malformed" {
        val response = ClientError<Any>(message = "oops", body = "not json", statusCode = 400)
        val exception = ClientException(statusCode = 400, response = response)

        val mapped = ReferralsError.fromApiException(exception)

        (mapped is ReferralsError.RequestFailure) shouldBe true
        (mapped as ReferralsError.RequestFailure).errors shouldBe emptyList<ErrorObject>()
    }

    "fromApiException returns null for exceptions that are not API exceptions" {
        ReferralsError.fromApiException(RuntimeException("random")) shouldBe null
    }

    "fromApiException returns null when ClientException has no body" {
        val response = ClientError<Any>(message = null, body = null, statusCode = 400)
        val exception = ClientException(statusCode = 400, response = response)
        ReferralsError.fromApiException(exception) shouldBe null
    }
})
