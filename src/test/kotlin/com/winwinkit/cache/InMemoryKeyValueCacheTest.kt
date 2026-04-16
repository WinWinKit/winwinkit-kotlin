package com.winwinkit.cache

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class InMemoryKeyValueCacheTest : StringSpec({

    "reading a missing key returns null" {
        val cache = InMemoryKeyValueCache()
        cache["absent"] shouldBe null
    }

    "writing and reading back returns the same bytes" {
        val cache = InMemoryKeyValueCache()
        cache["k"] = "value".toByteArray()
        cache["k"]?.toString(Charsets.UTF_8) shouldBe "value"
    }

    "writing null removes a stored entry" {
        val cache = InMemoryKeyValueCache()
        cache["k"] = "value".toByteArray()
        cache["k"] = null
        cache["k"] shouldBe null
    }

    "writes are isolated per cache instance" {
        val a = InMemoryKeyValueCache()
        val b = InMemoryKeyValueCache()
        a["k"] = "hello".toByteArray()
        b["k"] shouldBe null
    }
})
