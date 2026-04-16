package com.winwinkit.cache

import java.util.concurrent.ConcurrentHashMap

/** Thread-safe in-memory [KeyValueCache]. This is the default cache used by [com.winwinkit.Referrals]. */
class InMemoryKeyValueCache : KeyValueCache {
    private val storage = ConcurrentHashMap<String, ByteArray>()

    override fun get(key: String): ByteArray? = storage[key]

    override fun set(key: String, value: ByteArray?) {
        if (value == null) storage.remove(key) else storage[key] = value
    }
}
