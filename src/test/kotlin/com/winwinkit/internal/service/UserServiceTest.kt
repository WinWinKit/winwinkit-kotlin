package com.winwinkit.internal.service

import com.winwinkit.ReferralsError
import com.winwinkit.TestFixtures
import com.winwinkit.cache.InMemoryKeyValueCache
import com.winwinkit.client.infrastructure.ClientError
import com.winwinkit.client.infrastructure.ClientException
import com.winwinkit.client.models.User
import com.winwinkit.client.models.UserClaimCodeResponseData
import com.winwinkit.client.models.UserResponseData
import com.winwinkit.client.models.UserWithdrawCreditsResponseData
import com.winwinkit.internal.cache.UserCache
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class UserServiceTest : StringSpec({

    val apiKey = "test-key"

    fun buildService(
        appUserId: String = "user-1",
        users: FakeUsersProvider = FakeUsersProvider(),
        claims: FakeClaimActionsProvider = FakeClaimActionsProvider(),
        rewards: FakeRewardActionsProvider = FakeRewardActionsProvider(),
        googlePlay: FakeGooglePlayTransactionsProvider = FakeGooglePlayTransactionsProvider(),
        dispatcher: kotlinx.coroutines.CoroutineDispatcher,
        cache: UserCache = UserCache(InMemoryKeyValueCache()),
    ): Quintuple = Quintuple(
        UserService(
            appUserId = appUserId,
            apiKey = apiKey,
            providers = UserService.Providers(
                claimActions = claims,
                googlePlayTransactions = googlePlay,
                rewardActions = rewards,
                users = users,
            ),
            userCache = cache,
            dispatcher = dispatcher,
        ),
        users,
        claims,
        rewards,
        googlePlay,
    )

    "refresh creates user via provider and caches the result" {
        runTest {
            val user = TestFixtures.user(appUserId = "user-1")
            val (service, users, _, _, _) = buildService(dispatcher = StandardTestDispatcher(testScheduler)).also {
                it.users.response = UserResponseData(user = user)
            }

            service.refresh()
            advanceUntilIdle()

            users.requests.size shouldBe 1
            users.requests.first().appUserId shouldBe "user-1"
            service.cachedUser shouldBe user
        }
    }

    "refresh includes pending setter values in the request" {
        runTest {
            val user = TestFixtures.user(appUserId = "user-1", isPremium = true)
            val (service, users, _, _, _) = buildService(dispatcher = StandardTestDispatcher(testScheduler)).also {
                it.users.response = UserResponseData(user = user)
            }

            service.setIsPremium(true)
            service.setIsTrial(false)
            service.refresh()
            advanceUntilIdle()

            users.requests.first().isPremium shouldBe true
            users.requests.first().isTrial shouldBe false
        }
    }

    "refresh notifies delegate of updated user" {
        runTest {
            val user = TestFixtures.user(appUserId = "user-1")
            val (service, _, _, _, _) = buildService(dispatcher = StandardTestDispatcher(testScheduler)).also {
                it.users.response = UserResponseData(user = user)
            }
            val delegate = RecordingDelegate()
            service.delegate = delegate

            service.refresh()
            advanceUntilIdle()

            delegate.updates shouldBe listOf(user)
            delegate.errors shouldBe emptyList<Throwable>()
        }
    }

    "refresh notifies delegate of error" {
        runTest {
            val (service, users, _, _, _) = buildService(dispatcher = StandardTestDispatcher(testScheduler))
            users.error = RuntimeException("boom")
            val delegate = RecordingDelegate()
            service.delegate = delegate

            service.refresh()
            advanceUntilIdle()

            delegate.errors.size shouldBe 1
            (delegate.errors.first().message == "boom") shouldBe true
        }
    }

    "refresh maps 401 response to suspendedIndefinitely" {
        runTest {
            val (service, users, _, _, _) = buildService(dispatcher = StandardTestDispatcher(testScheduler))
            users.error = ClientException(
                message = "unauthorized",
                statusCode = 401,
                response = ClientError<Any>(message = "Unauthorized", body = null, statusCode = 401),
            )

            service.refresh()
            advanceUntilIdle()

            service.shouldSuspendIndefinitely shouldBe true
        }
    }

    "suspended service emits SuspendedIndefinitely error and does not call provider" {
        runTest {
            val (service, users, _, _, _) = buildService(dispatcher = StandardTestDispatcher(testScheduler))
            users.error = ClientException(
                statusCode = 401,
                response = ClientError<Any>(body = null, statusCode = 401),
            )
            service.refresh()
            advanceUntilIdle()

            val delegate = RecordingDelegate()
            service.delegate = delegate
            service.refresh()
            advanceUntilIdle()

            users.requests.size shouldBe 1
            (delegate.errors.any { it is ReferralsError.SuspendedIndefinitely }) shouldBe true
        }
    }

    "claimCode calls provider and caches updated user" {
        runTest {
            val user = TestFixtures.user(appUserId = "user-1", isPremium = true)
            val (service, _, claims, _, _) = buildService(dispatcher = StandardTestDispatcher(testScheduler)).also {
                it.claims.response = UserClaimCodeResponseData(
                    rewardsGranted = TestFixtures.emptyRewardsGranted,
                    user = user,
                )
            }

            val results = mutableListOf<Result<UserClaimCodeResponseData>>()
            service.claimCode("PROMO") { results += it }
            advanceUntilIdle()

            claims.calls.single().request.code shouldBe "PROMO"
            results.single().isSuccess shouldBe true
            service.cachedUser shouldBe user
        }
    }

    "claimCode maps API errors to ReferralsError" {
        runTest {
            val (service, _, claims, _, _) = buildService(dispatcher = StandardTestDispatcher(testScheduler))
            val body = """{"errors":[{"code":"not_found","status":404,"message":"no","source":null}]}"""
            claims.error = ClientException(
                statusCode = 404,
                response = ClientError<Any>(body = body, statusCode = 404),
            )

            val results = mutableListOf<Result<UserClaimCodeResponseData>>()
            service.claimCode("X") { results += it }
            advanceUntilIdle()

            val failure = results.single().exceptionOrNull()
            (failure is ReferralsError.RequestFailure) shouldBe true
            (failure as ReferralsError.RequestFailure).errors.single().code shouldBe "not_found"
        }
    }

    "withdrawCredits calls provider and caches updated user" {
        runTest {
            val user = TestFixtures.user(appUserId = "user-1")
            val (service, _, _, rewards, _) = buildService(dispatcher = StandardTestDispatcher(testScheduler)).also {
                it.rewards.response = UserWithdrawCreditsResponseData(
                    user = user,
                    withdrawResult = TestFixtures.zeroWithdrawResult,
                )
            }

            val results = mutableListOf<Result<UserWithdrawCreditsResponseData>>()
            service.withdrawCredits(key = "credits", amount = 5) { results += it }
            advanceUntilIdle()

            rewards.calls.single().request.key shouldBe "credits"
            rewards.calls.single().request.amount shouldBe 5
            results.single().isSuccess shouldBe true
            service.cachedUser shouldBe user
        }
    }

    "syncGooglePlayTransaction registers a new purchase token" {
        runTest {
            val (service, _, _, _, google) = buildService(dispatcher = StandardTestDispatcher(testScheduler))

            service.syncGooglePlayTransaction(purchaseToken = "token-1", obfuscatedExternalAccountId = "abc")
            advanceUntilIdle()

            google.calls.single().request.purchaseToken shouldBe "token-1"
            google.calls.single().request.obfuscatedExternalAccountId shouldBe "abc"
        }
    }

    "syncGooglePlayTransaction caches registered tokens to dedupe later calls" {
        runTest {
            val (service, _, _, _, google) = buildService(dispatcher = StandardTestDispatcher(testScheduler))

            service.syncGooglePlayTransaction(purchaseToken = "token-1", obfuscatedExternalAccountId = null)
            advanceUntilIdle()
            service.syncGooglePlayTransaction(purchaseToken = "token-1", obfuscatedExternalAccountId = null)
            advanceUntilIdle()

            google.calls.size shouldBe 1
        }
    }

    "syncGooglePlayTransaction still registers distinct tokens" {
        runTest {
            val (service, _, _, _, google) = buildService(dispatcher = StandardTestDispatcher(testScheduler))

            service.syncGooglePlayTransaction(purchaseToken = "token-1", obfuscatedExternalAccountId = null)
            advanceUntilIdle()
            service.syncGooglePlayTransaction(purchaseToken = "token-2", obfuscatedExternalAccountId = null)
            advanceUntilIdle()

            google.calls.map { it.request.purchaseToken } shouldBe listOf("token-1", "token-2")
        }
    }

    "syncGooglePlayTransaction does not cache tokens when provider throws" {
        runTest {
            val (service, _, _, _, google) = buildService(dispatcher = StandardTestDispatcher(testScheduler))
            google.error = RuntimeException("nope")

            service.syncGooglePlayTransaction(purchaseToken = "token-1", obfuscatedExternalAccountId = null)
            advanceUntilIdle()
            google.error = null
            service.syncGooglePlayTransaction(purchaseToken = "token-1", obfuscatedExternalAccountId = null)
            advanceUntilIdle()

            google.calls.size shouldBe 2
        }
    }

    "syncGooglePlayTransaction is a no-op when the service is suspended" {
        runTest {
            val (service, users, _, _, google) = buildService(dispatcher = StandardTestDispatcher(testScheduler))
            users.error = ClientException(
                statusCode = 401,
                response = ClientError<Any>(body = null, statusCode = 401),
            )
            service.refresh()
            advanceUntilIdle()

            service.syncGooglePlayTransaction(purchaseToken = "t", obfuscatedExternalAccountId = null)
            advanceUntilIdle()

            google.calls shouldBe emptyList<FakeGooglePlayTransactionsProvider.Call>()
        }
    }
})

private data class Quintuple(
    val service: UserService,
    val users: FakeUsersProvider,
    val claims: FakeClaimActionsProvider,
    val rewards: FakeRewardActionsProvider,
    val googlePlay: FakeGooglePlayTransactionsProvider,
)

private class RecordingDelegate : UserServiceDelegate {
    val updates = mutableListOf<User>()
    val errors = mutableListOf<Throwable>()
    val refreshingStates = mutableListOf<Boolean>()
    var allowRefresh = true

    override fun canPerformNextRefresh(service: UserService): Boolean = allowRefresh

    override fun onUserUpdate(service: UserService, user: User) {
        updates += user
    }

    override fun onError(service: UserService, error: Throwable) {
        errors += error
    }

    override fun onIsRefreshingChanged(service: UserService, isRefreshing: Boolean) {
        refreshingStates += isRefreshing
    }
}
