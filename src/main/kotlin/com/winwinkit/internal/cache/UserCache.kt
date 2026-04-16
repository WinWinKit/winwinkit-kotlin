package com.winwinkit.internal.cache

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.winwinkit.cache.KeyValueCache
import com.winwinkit.client.infrastructure.Serializer
import com.winwinkit.client.models.User
import com.winwinkit.internal.logging.Logger
import com.winwinkit.internal.model.UserUpdate

internal interface UserCacheType {
    var user: User?
    var userUpdate: UserUpdate?
    var registeredGooglePlayPurchaseTokens: Set<String>?
}

internal fun UserCacheType.reset() {
    user = null
    userUpdate = null
    registeredGooglePlayPurchaseTokens = null
}

internal class UserCache(private val keyValueCache: KeyValueCache) : UserCacheType {

    private val userAdapter: JsonAdapter<User> = moshi.adapter(User::class.java)
    private val userUpdateAdapter: JsonAdapter<UserUpdate> = moshi.adapter(UserUpdate::class.java)
    private val stringSetAdapter: JsonAdapter<Set<String>> =
        moshi.adapter(Types.newParameterizedType(Set::class.java, String::class.java))

    override var user: User?
        get() = read(Keys.USER, userAdapter, "User")
        set(value) = write(Keys.USER, value, userAdapter, "User")

    override var userUpdate: UserUpdate?
        get() = read(Keys.USER_UPDATE, userUpdateAdapter, "UserUpdate")
        set(value) = write(Keys.USER_UPDATE, value, userUpdateAdapter, "UserUpdate")

    override var registeredGooglePlayPurchaseTokens: Set<String>?
        get() = read(Keys.GOOGLE_PLAY_PURCHASE_TOKENS, stringSetAdapter, "purchase tokens")
        set(value) = write(Keys.GOOGLE_PLAY_PURCHASE_TOKENS, value, stringSetAdapter, "purchase tokens")

    private fun <T> read(key: String, adapter: JsonAdapter<T>, label: String): T? {
        val bytes = keyValueCache[key] ?: return null
        return try {
            adapter.fromJson(bytes.toString(Charsets.UTF_8))
        } catch (e: Throwable) {
            Logger.error("Unable to deserialize $label: $e")
            keyValueCache[key] = null
            null
        }
    }

    private fun <T> write(key: String, value: T?, adapter: JsonAdapter<T>, label: String) {
        try {
            keyValueCache[key] = value?.let { adapter.toJson(it).toByteArray(Charsets.UTF_8) }
        } catch (e: Throwable) {
            Logger.error("Unable to serialize $label: $e")
        }
    }

    private companion object {
        private val moshi: Moshi = Serializer.moshi
    }

    private object Keys {
        const val USER = "com.winwinkit.cache.user"
        const val USER_UPDATE = "com.winwinkit.cache.userUpdate"
        const val GOOGLE_PLAY_PURCHASE_TOKENS = "com.winwinkit.cache.googlePlayPurchaseTokens"
    }
}
