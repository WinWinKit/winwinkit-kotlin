package com.winwinkit

import com.winwinkit.client.models.User

/**
 * Callback interface for [Referrals] events.
 *
 * Set via [Referrals.delegate]. [onUserUpdate] is called when the user is refreshed or
 * reset (with `null` on reset). [onError] is called when any SDK operation fails.
 */
interface ReferralsDelegate {
    fun onUserUpdate(referrals: Referrals, user: User?)
    fun onError(referrals: Referrals, error: Throwable)
}
