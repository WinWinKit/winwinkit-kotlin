package com.winwinkit.cache

/**
 * Contract for a binary key-value cache that [com.winwinkit.Referrals] uses to persist its state.
 *
 * The default implementation is [InMemoryKeyValueCache]. Consumers may provide their own
 * implementation backed by `SharedPreferences`, `DataStore`, a file, or any other store.
 */
interface KeyValueCache {
    operator fun get(key: String): ByteArray?
    operator fun set(key: String, value: ByteArray?)
}
