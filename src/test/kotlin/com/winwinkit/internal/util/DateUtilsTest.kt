package com.winwinkit.internal.util

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.time.OffsetDateTime
import java.time.ZoneOffset

class DateUtilsTest : StringSpec({

    val base = OffsetDateTime.of(2026, 4, 16, 12, 0, 30, 0, ZoneOffset.UTC)

    "isEqualToSeconds returns true for identical instants" {
        base.isEqualToSeconds(base) shouldBe true
    }

    "isEqualToSeconds ignores sub-second differences" {
        base.isEqualToSeconds(base.withNano(500_000_000)) shouldBe true
    }

    "isEqualToSeconds returns false when seconds differ" {
        base.isEqualToSeconds(base.plusSeconds(1)) shouldBe false
    }

    "isEqualToSeconds returns false when other is null" {
        base.isEqualToSeconds(null) shouldBe false
    }

    "isEqualToSeconds compares across time zones" {
        val sameInstantElsewhere = base.withOffsetSameInstant(ZoneOffset.ofHours(5))
        base.isEqualToSeconds(sameInstantElsewhere) shouldBe true
    }
})
