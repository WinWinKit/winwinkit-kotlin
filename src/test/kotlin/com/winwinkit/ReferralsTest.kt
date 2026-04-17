package com.winwinkit

import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue

class ReferralsTest {

    private lateinit var server: MockWebServer
    private lateinit var referrals: Referrals

    @BeforeEach
    fun setUp() {
        server = MockWebServer().apply { start() }
        referrals = Referrals(apiKey = "test-key", baseUrl = server.url("/").toString().trimEnd('/'))
    }

    @AfterEach
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `fetchUser returns Success with user on 200`() = runTest {
        server.enqueue(MockResponse().setResponseCode(200).setBody(USER_RESPONSE_JSON))

        val result = referrals.fetchUser(appUserId = "user-1")

        assertTrue(result is ReferralsResult.Success)
        val user = (result as ReferralsResult.Success).data
        assertNotNull(user)
        assertEquals("user-1", user!!.appUserId)

        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/users/user-1", request.path)
        assertEquals("test-key", request.getHeader("x-api-key"))
    }

    @Test
    fun `fetchUser returns Success with null on 404`() = runTest {
        server.enqueue(MockResponse().setResponseCode(404).setBody(ERRORS_404_JSON))

        val result = referrals.fetchUser(appUserId = "missing")

        assertTrue(result is ReferralsResult.Success)
        assertNull((result as ReferralsResult.Success).data)
    }

    @Test
    fun `fetchUser returns Failure on 500 with parsed errors`() = runTest {
        server.enqueue(MockResponse().setResponseCode(500).setBody(ERRORS_500_JSON))

        val result = referrals.fetchUser(appUserId = "user-1")

        assertTrue(result is ReferralsResult.Failure)
        val failure = result as ReferralsResult.Failure
        assertEquals(1, failure.errors.size)
        assertEquals(500, failure.errors.first().status)
        assertEquals("internal_error", failure.errors.first().code)
    }

    @Test
    fun `createOrUpdateUser posts the request and returns the user`() = runTest {
        server.enqueue(MockResponse().setResponseCode(200).setBody(USER_RESPONSE_JSON))

        val result = referrals.createOrUpdateUser(
            appUserId = "user-1",
            isPremium = true,
            stripeCustomerId = "cus_123",
        )

        assertTrue(result is ReferralsResult.Success)
        assertEquals("user-1", (result as ReferralsResult.Success).data.appUserId)

        val request = server.takeRequest()
        assertEquals("POST", request.method)
        assertEquals("/users", request.path)
        val body = request.body.readUtf8()
        assertTrue(body.contains("\"app_user_id\":\"user-1\""))
        assertTrue(body.contains("\"is_premium\":true"))
        assertTrue(body.contains("\"stripe_customer_id\":\"cus_123\""))
    }

    @Test
    fun `claimCode returns user and rewardsGranted on success`() = runTest {
        server.enqueue(MockResponse().setResponseCode(200).setBody(CLAIM_CODE_RESPONSE_JSON))

        val result = referrals.claimCode(appUserId = "user-1", code = "WELCOME")

        assertTrue(result is ReferralsResult.Success)
        val data = (result as ReferralsResult.Success).data
        assertEquals("user-1", data.user.appUserId)
        assertNotNull(data.rewardsGranted)

        val request = server.takeRequest()
        assertEquals("POST", request.method)
        assertEquals("/users/user-1/claim-code", request.path)
        assertTrue(request.body.readUtf8().contains("\"code\":\"WELCOME\""))
    }

    @Test
    fun `claimCode returns Failure with errors on 400`() = runTest {
        server.enqueue(MockResponse().setResponseCode(400).setBody(ERRORS_400_JSON))

        val result = referrals.claimCode(appUserId = "user-1", code = "BAD")

        assertTrue(result is ReferralsResult.Failure)
        val failure = result as ReferralsResult.Failure
        assertEquals(400, failure.errors.first().status)
        assertEquals("invalid_code", failure.errors.first().code)
    }

    @Test
    fun `registerGooglePlayTransaction posts purchase token`() = runTest {
        server.enqueue(MockResponse().setResponseCode(204))

        val result = referrals.registerGooglePlayTransaction(
            appUserId = "user-1",
            purchaseToken = "token-xyz",
            obfuscatedExternalAccountId = "acct-1",
        )

        assertTrue(result is ReferralsResult.Success)

        val request = server.takeRequest()
        assertEquals("POST", request.method)
        assertEquals("/users/user-1/transactions/google-play", request.path)
        val body = request.body.readUtf8()
        assertTrue(body.contains("\"purchase_token\":\"token-xyz\""))
        assertTrue(body.contains("\"obfuscated_external_account_id\":\"acct-1\""))
    }

    @Test
    fun `registerAppStoreTransaction posts original transaction id`() = runTest {
        server.enqueue(MockResponse().setResponseCode(204))

        val result = referrals.registerAppStoreTransaction(
            appUserId = "user-1",
            originalTransactionId = "txn-1",
        )

        assertTrue(result is ReferralsResult.Success)

        val request = server.takeRequest()
        assertEquals("POST", request.method)
        assertEquals("/users/user-1/transactions/app-store", request.path)
        assertTrue(request.body.readUtf8().contains("\"original_transaction_id\":\"txn-1\""))
    }

    @Test
    fun `Failure contains empty errors when body is not parseable`() = runTest {
        server.enqueue(MockResponse().setResponseCode(500).setBody("<html>oops</html>"))

        val result = referrals.fetchUser(appUserId = "user-1")

        assertTrue(result is ReferralsResult.Failure)
        assertTrue((result as ReferralsResult.Failure).errors.isEmpty())
    }

    private companion object {
        const val EMPTY_BUCKETS =
            """{"basic":[],"credit":[],"offer_code":[],"googleplay_promo_code":[],"revenuecat_entitlement":[],"revenuecat_offering":[]}"""

        const val USER_RESPONSE_JSON = """
{
  "data": {
    "user": {
      "app_user_id": "user-1",
      "referral_code": "ABC123",
      "referral_code_link": "https://winwinkit.com/r/ABC123",
      "is_trial": false,
      "is_premium": true,
      "first_seen_at": null,
      "last_seen_at": null,
      "metadata": null,
      "stripe_customer_id": null,
      "claim_code_eligibility": { "eligible": true, "eligible_until": null },
      "referred_by": null,
      "stats": { "claims": 0, "conversions": 0, "churns": 0 },
      "rewards": { "active": $EMPTY_BUCKETS, "expired": $EMPTY_BUCKETS },
      "referral_program": null
    }
  }
}
"""

        const val CLAIM_CODE_RESPONSE_JSON = """
{
  "data": {
    "user": {
      "app_user_id": "user-1",
      "referral_code": "ABC123",
      "referral_code_link": "https://winwinkit.com/r/ABC123",
      "is_trial": false,
      "is_premium": true,
      "first_seen_at": null,
      "last_seen_at": null,
      "metadata": null,
      "stripe_customer_id": null,
      "claim_code_eligibility": { "eligible": true, "eligible_until": null },
      "referred_by": null,
      "stats": { "claims": 0, "conversions": 0, "churns": 0 },
      "rewards": { "active": $EMPTY_BUCKETS, "expired": $EMPTY_BUCKETS },
      "referral_program": null
    },
    "rewards_granted": $EMPTY_BUCKETS
  }
}
"""

        const val ERRORS_404_JSON = """
{"errors":[{"code":"not_found","status":404,"message":"User not found","source":null}]}
"""

        const val ERRORS_400_JSON = """
{"errors":[{"code":"invalid_code","status":400,"message":"Invalid code","source":null}]}
"""

        const val ERRORS_500_JSON = """
{"errors":[{"code":"internal_error","status":500,"message":"Server error","source":null}]}
"""
    }
}
