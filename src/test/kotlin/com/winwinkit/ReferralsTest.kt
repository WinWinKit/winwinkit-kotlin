package com.winwinkit

import com.winwinkit.cache.InMemoryKeyValueCache
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec

class ReferralsTest : StringSpec({

    "is not configured by default" {
        Referrals.resetForTesting()
        Referrals.isConfigured shouldBe false
        shouldThrow<IllegalStateException> { Referrals.shared }
    }

    "configure returns a configured singleton" {
        Referrals.resetForTesting()
        val referrals = Referrals.configure(apiKey = "test", keyValueCache = InMemoryKeyValueCache())
        Referrals.isConfigured shouldBe true
        Referrals.shared shouldBe referrals
    }

    "calling configure twice returns the same instance" {
        Referrals.resetForTesting()
        val first = Referrals.configure(apiKey = "test")
        val second = Referrals.configure(apiKey = "other")
        first shouldBe second
    }

    "claimCode without appUserId fails with AppUserIdNotSet" {
        Referrals.resetForTesting()
        val referrals = Referrals.configure(apiKey = "test")
        var received: Throwable? = null
        referrals.claimCode("code") { result -> received = result.exceptionOrNull() }
        (received is ReferralsError.AppUserIdNotSet) shouldBe true
    }

    "withdrawCredits without appUserId fails with AppUserIdNotSet" {
        Referrals.resetForTesting()
        val referrals = Referrals.configure(apiKey = "test")
        var received: Throwable? = null
        referrals.withdrawCredits("credits", 1) { result -> received = result.exceptionOrNull() }
        (received is ReferralsError.AppUserIdNotSet) shouldBe true
    }

    "syncTransactions without appUserId is a silent no-op" {
        Referrals.resetForTesting()
        val referrals = Referrals.configure(apiKey = "test")
        // Should not throw
        referrals.syncTransactions(purchaseToken = "token")
    }

    "state starts in None for both user and claim code" {
        Referrals.resetForTesting()
        val referrals = Referrals.configure(apiKey = "test")
        referrals.state.user.value shouldBe null
        referrals.state.userState.value shouldBe ReferralsState.UserState.None
        referrals.state.claimCodeState.value shouldBe ReferralsState.ClaimCodeState.None
    }
})
