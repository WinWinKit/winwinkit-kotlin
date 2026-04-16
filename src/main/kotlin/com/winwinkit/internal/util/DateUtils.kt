package com.winwinkit.internal.util

import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

internal fun OffsetDateTime.isEqualToSeconds(other: OffsetDateTime?): Boolean {
    if (other == null) return false
    return toInstant().truncatedTo(ChronoUnit.SECONDS) ==
        other.toInstant().truncatedTo(ChronoUnit.SECONDS)
}
