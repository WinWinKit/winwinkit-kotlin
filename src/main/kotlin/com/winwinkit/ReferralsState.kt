package com.winwinkit

import com.winwinkit.client.models.ErrorObject
import com.winwinkit.client.models.User
import com.winwinkit.client.models.UserRewardsGranted
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Observable state exposed by [Referrals]. Backed by [StateFlow] so consumers can collect updates
 * from a coroutine or convert to a Compose `State` with `collectAsState()`.
 */
class ReferralsState internal constructor() {

    sealed class UserState {
        object None : UserState()
        object Loading : UserState()
        object Available : UserState()
        data class Failure(val error: Throwable) : UserState()

        val isLoading: Boolean get() = this is Loading
        val isAvailable: Boolean get() = this is Available
        val isFailure: Boolean get() = this is Failure

        val errorObjects: List<ErrorObject>?
            get() = ((this as? Failure)?.error as? ReferralsError)?.errorObjects
    }

    sealed class ClaimCodeState {
        object None : ClaimCodeState()
        object Loading : ClaimCodeState()
        data class Success(val rewardsGranted: UserRewardsGranted) : ClaimCodeState()
        data class Failure(val error: Throwable) : ClaimCodeState()

        val isLoading: Boolean get() = this is Loading
        val isSuccess: Boolean get() = this is Success
        val isFailure: Boolean get() = this is Failure

        val errorObjects: List<ErrorObject>?
            get() = ((this as? Failure)?.error as? ReferralsError)?.errorObjects
    }

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _userState = MutableStateFlow<UserState>(UserState.None)
    val userState: StateFlow<UserState> = _userState.asStateFlow()

    private val _claimCodeState = MutableStateFlow<ClaimCodeState>(ClaimCodeState.None)
    val claimCodeState: StateFlow<ClaimCodeState> = _claimCodeState.asStateFlow()

    internal var onClaimCode: ((String) -> Unit)? = null

    fun claimCode(code: String) {
        onClaimCode?.invoke(code)
    }

    internal fun setUser(value: User?) {
        _user.value = value
    }

    internal fun setUserState(value: UserState) {
        _userState.value = value
    }

    internal fun setClaimCodeState(value: ClaimCodeState) {
        _claimCodeState.value = value
    }
}
