package com.ahmetkaragunlu.guidemate.session.domain.usecase

import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.notification.domain.navigation.NotificationNavigationCoordinator
import com.ahmetkaragunlu.guidemate.testing.FakeAuthRepository
import com.ahmetkaragunlu.guidemate.testing.FakeNotificationRepository
import com.ahmetkaragunlu.guidemate.testing.FakePaymentRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class TerminateUserSessionUseCaseTest {
    @Test
    fun `logout result failure does not skip local cleanup`() = runTest {
        val authRepository =
            FakeAuthRepository().apply {
                logoutResult = DataResult.Error(AppError.NoResponseFromServer)
            }
        val notificationRepository = FakeNotificationRepository()
        val paymentRepository = FakePaymentRepository()
        val useCase = createUseCase(authRepository, notificationRepository, paymentRepository)

        useCase()

        assertEquals(1, authRepository.logoutCalls)
        assertEquals(1, notificationRepository.dismissSystemNotificationsCalls)
        assertEquals(1, notificationRepository.clearLocalStateCalls)
        assertEquals(1, paymentRepository.clearAllPendingPaymentCalls)
    }

    @Test
    fun `one local cleanup failure does not skip remaining cleanup`() = runTest {
        val notificationRepository =
            FakeNotificationRepository().apply {
                dismissSystemNotificationsException = IllegalStateException("system unavailable")
                clearLocalStateException = IllegalStateException("storage unavailable")
            }
        val authRepository = FakeAuthRepository()
        val paymentRepository = FakePaymentRepository()
        val useCase =
            createUseCase(
                authRepository,
                notificationRepository,
                paymentRepository,
            )

        useCase()

        assertEquals(1, authRepository.logoutCalls)
        assertEquals(1, notificationRepository.dismissSystemNotificationsCalls)
        assertEquals(1, notificationRepository.clearLocalStateCalls)
        assertEquals(1, paymentRepository.clearAllPendingPaymentCalls)
    }

    @Test
    fun `logout cancellation is rethrown after local cleanup`() = runTest {
        val authRepository =
            FakeAuthRepository().apply {
                logoutException = CancellationException("cancelled")
            }
        val notificationRepository = FakeNotificationRepository()
        val paymentRepository = FakePaymentRepository()
        val useCase = createUseCase(authRepository, notificationRepository, paymentRepository)

        try {
            useCase()
            fail("CancellationException expected")
        } catch (_: CancellationException) {
            assertEquals(1, notificationRepository.clearLocalStateCalls)
            assertEquals(1, paymentRepository.clearAllPendingPaymentCalls)
        }
    }

    private fun createUseCase(
        authRepository: FakeAuthRepository,
        notificationRepository: FakeNotificationRepository,
        paymentRepository: FakePaymentRepository,
    ) =
        TerminateUserSessionUseCase(
            authRepository = authRepository,
            notificationRepository = notificationRepository,
            paymentRepository = paymentRepository,
            notificationNavigationCoordinator = NotificationNavigationCoordinator(),
        )
}
