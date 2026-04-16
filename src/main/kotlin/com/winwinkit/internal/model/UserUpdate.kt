package com.winwinkit.internal.model

import com.squareup.moshi.Json
import java.time.OffsetDateTime

internal data class UserUpdate(
    @Json(name = "app_user_id") val appUserId: String,
    @Json(name = "is_premium") val isPremium: Boolean? = null,
    @Json(name = "is_trial") val isTrial: Boolean? = null,
    @Json(name = "first_seen_at") val firstSeenAt: OffsetDateTime? = null,
    @Json(name = "metadata") val metadata: Any? = null,
    @Json(name = "stripe_customer_id") val stripeCustomerId: String? = null,
)
