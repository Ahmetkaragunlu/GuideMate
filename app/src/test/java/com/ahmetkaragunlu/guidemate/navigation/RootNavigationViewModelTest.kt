package com.ahmetkaragunlu.guidemate.navigation

import com.ahmetkaragunlu.guidemate.auth.domain.model.UserRole
import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.navigation.auth.AuthStartDestination
import com.ahmetkaragunlu.guidemate.notification.domain.navigation.NotificationNavigationCoordinator
import com.ahmetkaragunlu.guidemate.testing.FakeAuthRepository
import com.ahmetkaragunlu.guidemate.testing.FakeNotificationRepository
import com.ahmetkaragunlu.guidemate.testing.FakeOnboardingRepository
import com.ahmetkaragunlu.guidemate.testing.FakeUserRepository
import com.ahmetkaragunlu.guidemate.testing.authenticatedUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RootNavigationViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun noStoredSession_opensOnboardingAndClearsStaleSession() =
        runTest {
            val authRepository = FakeAuthRepository().apply { storedSession = false }
            val viewModel =
                createViewModel(
                    authRepository = authRepository,
                    onboardingRepository = FakeOnboardingRepository(completed = false),
                )

            runCurrent()

            assertEquals(RootNavigationTarget.AUTH, viewModel.uiState.value.target)
            assertEquals(
                AuthStartDestination.ONBOARDING,
                viewModel.uiState.value.authStartDestination,
            )
            assertEquals(1, authRepository.clearLocalSessionCalls)
        }

    @Test
    fun storedGuideSession_restoresGuideDestinationAndLogoutClearsNotificationState() =
        runTest {
            val guide = authenticatedUser(role = UserRole.GUIDE)
            val authRepository =
                FakeAuthRepository().apply {
                    storedSession = true
                    currentUserResult = com.ahmetkaragunlu.guidemate.common.result.DataResult.Success(guide)
                }
            val userRepository = FakeUserRepository(guide).apply { restoredUser = guide }
            val notificationRepository = FakeNotificationRepository()
            val viewModel =
                createViewModel(
                    authRepository = authRepository,
                    userRepository = userRepository,
                    notificationRepository = notificationRepository,
                )

            runCurrent()
            assertEquals(RootNavigationTarget.GUIDE, viewModel.uiState.value.target)

            viewModel.logout()
            runCurrent()

            assertEquals(1, authRepository.logoutCalls)
            assertEquals(1, notificationRepository.clearLocalStateCalls)
        }

    private fun createViewModel(
        authRepository: FakeAuthRepository = FakeAuthRepository(),
        userRepository: FakeUserRepository = FakeUserRepository(),
        onboardingRepository: FakeOnboardingRepository = FakeOnboardingRepository(),
        notificationRepository: FakeNotificationRepository = FakeNotificationRepository(),
    ): RootNavigationViewModel =
        RootNavigationViewModel(
            authRepository = authRepository,
            userRepository = userRepository,
            onboardingRepository = onboardingRepository,
            notificationRepository = notificationRepository,
            notificationNavigationCoordinator = NotificationNavigationCoordinator(),
        )
}
