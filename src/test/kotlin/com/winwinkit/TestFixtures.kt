package com.winwinkit

import com.winwinkit.client.models.User
import com.winwinkit.client.models.UserClaimCodeEligibility
import com.winwinkit.client.models.UserRewards
import com.winwinkit.client.models.UserRewardsActive
import com.winwinkit.client.models.UserRewardsExpired
import com.winwinkit.client.models.UserRewardsGranted
import com.winwinkit.client.models.UserStats
import com.winwinkit.client.models.UserWithdrawCreditsResult

internal object TestFixtures {

    private val emptyRewardsActive = UserRewardsActive(
        basic = emptyList(),
        credit = emptyList(),
        offerCode = emptyList(),
        googleplayPromoCode = emptyList(),
        revenuecatEntitlement = emptyList(),
        revenuecatOffering = emptyList(),
    )

    private val emptyRewardsExpired = UserRewardsExpired(
        basic = emptyList(),
        credit = emptyList(),
        offerCode = emptyList(),
        googleplayPromoCode = emptyList(),
        revenuecatEntitlement = emptyList(),
        revenuecatOffering = emptyList(),
    )

    val emptyRewards = UserRewards(
        active = emptyRewardsActive,
        expired = emptyRewardsExpired,
    )

    val emptyRewardsGranted = UserRewardsGranted(
        basic = emptyList(),
        credit = emptyList(),
        offerCode = emptyList(),
        googleplayPromoCode = emptyList(),
        revenuecatEntitlement = emptyList(),
        revenuecatOffering = emptyList(),
    )

    val zeroWithdrawResult = UserWithdrawCreditsResult(
        creditsAvailableAtStart = 0,
        creditsAvailableAtEnd = 0,
        creditsRequestedToWithdraw = 0,
        creditsWithdrawn = 0,
    )

    @Suppress("DEPRECATION")
    fun user(
        appUserId: String = "user-1",
        isPremium: Boolean? = null,
        isTrial: Boolean? = null,
    ): User = User(
        appUserId = appUserId,
        referralCode = null,
        referralCodeLink = null,
        isTrial = isTrial,
        isPremium = isPremium,
        firstSeenAt = null,
        lastSeenAt = null,
        metadata = null,
        stripeCustomerId = null,
        claimCodeEligibility = UserClaimCodeEligibility(eligible = true, eligibleUntil = null),
        referredBy = null,
        stats = UserStats(claims = 0, conversions = 0, churns = 0),
        rewards = emptyRewards,
        referralProgram = null,
    )
}
