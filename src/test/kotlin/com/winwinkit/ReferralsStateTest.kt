package com.winwinkit

import com.winwinkit.TestFixtures
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class ReferralsStateTest : StringSpec({

    "UserState.None flags" {
        val s: ReferralsState.UserState = ReferralsState.UserState.None
        s.isLoading shouldBe false
        s.isAvailable shouldBe false
        s.isFailure shouldBe false
        s.errorObjects shouldBe null
    }

    "UserState.Loading flags" {
        val s: ReferralsState.UserState = ReferralsState.UserState.Loading
        s.isLoading shouldBe true
        s.isAvailable shouldBe false
        s.isFailure shouldBe false
    }

    "UserState.Available flags" {
        val s: ReferralsState.UserState = ReferralsState.UserState.Available
        s.isAvailable shouldBe true
        s.isLoading shouldBe false
        s.isFailure shouldBe false
    }

    "UserState.Failure exposes errorObjects for RequestFailure" {
        val errors = listOf(com.winwinkit.client.models.ErrorObject("c", 400, "m", null))
        val s: ReferralsState.UserState = ReferralsState.UserState.Failure(ReferralsError.RequestFailure(errors))
        s.isFailure shouldBe true
        s.errorObjects shouldBe errors
    }

    "UserState.Failure has no errorObjects for non-ReferralsError" {
        val s: ReferralsState.UserState = ReferralsState.UserState.Failure(RuntimeException("other"))
        s.errorObjects shouldBe null
    }

    "ClaimCodeState.Success exposes the granted rewards" {
        val s: ReferralsState.ClaimCodeState = ReferralsState.ClaimCodeState.Success(TestFixtures.emptyRewardsGranted)
        s.isSuccess shouldBe true
        (s as ReferralsState.ClaimCodeState.Success).rewardsGranted shouldBe TestFixtures.emptyRewardsGranted
    }

    "ClaimCodeState.Loading flags" {
        val s: ReferralsState.ClaimCodeState = ReferralsState.ClaimCodeState.Loading
        s.isLoading shouldBe true
        s.isSuccess shouldBe false
        s.isFailure shouldBe false
    }

    "claimCode invokes the onClaimCode handler" {
        val state = ReferralsState()
        var captured: String? = null
        state.onClaimCode = { code -> captured = code }
        state.claimCode("PROMO")
        captured shouldBe "PROMO"
    }

    "claimCode does nothing when no handler is registered" {
        val state = ReferralsState()
        state.claimCode("PROMO") // must not throw
    }
})
