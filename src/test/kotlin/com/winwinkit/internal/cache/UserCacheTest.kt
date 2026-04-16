package com.winwinkit.internal.cache

import com.winwinkit.TestFixtures
import com.winwinkit.cache.InMemoryKeyValueCache
import com.winwinkit.internal.model.UserUpdate
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.time.OffsetDateTime
import java.time.ZoneOffset

class UserCacheTest : StringSpec({

    "user is null when nothing is cached" {
        val cache = UserCache(InMemoryKeyValueCache())
        cache.user shouldBe null
    }

    "user roundtrips through serialization" {
        val cache = UserCache(InMemoryKeyValueCache())
        val user = TestFixtures.user(appUserId = "abc", isPremium = true)

        cache.user = user

        cache.user shouldBe user
    }

    "userUpdate roundtrips through serialization" {
        val cache = UserCache(InMemoryKeyValueCache())
        val update = UserUpdate(
            appUserId = "abc",
            isPremium = true,
            firstSeenAt = OffsetDateTime.of(2026, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
        )

        cache.userUpdate = update

        cache.userUpdate shouldBe update
    }

    "registeredGooglePlayPurchaseTokens roundtrips" {
        val cache = UserCache(InMemoryKeyValueCache())
        cache.registeredGooglePlayPurchaseTokens = setOf("token-a", "token-b")
        cache.registeredGooglePlayPurchaseTokens shouldBe setOf("token-a", "token-b")
    }

    "writing null removes a cached value" {
        val cache = UserCache(InMemoryKeyValueCache())
        cache.user = TestFixtures.user()
        cache.user = null
        cache.user shouldBe null
    }

    "reset clears all cached fields" {
        val cache = UserCache(InMemoryKeyValueCache())
        cache.user = TestFixtures.user()
        cache.userUpdate = UserUpdate(appUserId = "abc")
        cache.registeredGooglePlayPurchaseTokens = setOf("t")

        cache.reset()

        cache.user shouldBe null
        cache.userUpdate shouldBe null
        cache.registeredGooglePlayPurchaseTokens shouldBe null
    }

    "corrupted data is discarded on read" {
        val backing = InMemoryKeyValueCache()
        backing["com.winwinkit.cache.user"] = "not valid json".toByteArray()
        val cache = UserCache(backing)

        cache.user shouldBe null
        backing["com.winwinkit.cache.user"] shouldBe null
    }
})
