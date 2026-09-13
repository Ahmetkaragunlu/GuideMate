package com.ahmetkaragunlu.guidemate.session.domain.usecase

import com.ahmetkaragunlu.guidemate.auth.domain.repository.AuthRepository
import com.ahmetkaragunlu.guidemate.notification.domain.navigation.NotificationNavigationCoordinator
import com.ahmetkaragunlu.guidemate.notification.domain.repository.NotificationRepository
import com.ahmetkaragunlu.guidemate.payment.domain.repository.PaymentRepository
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext

class TerminateUserSessionUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val notificationRepository: NotificationRepository,
    private val paymentRepository: PaymentRepository,
    private val notificationNavigationCoordinator: NotificationNavigationCoordinator,
) {
    suspend operator fun invoke() {
        notificationNavigationCoordinator.clear()
        runBestEffort { notificationRepository.dismissSystemNotifications() }

        var cancellation: CancellationException? = null
        try {
            authRepository.logout()
        } catch (exception: CancellationException) {
            cancellation = exception
        } catch (_: Exception) {
            // Local session cleanup inside the auth repository has already run.
        } finally {
            withContext(NonCancellable) {
                runBestEffort { notificationRepository.clearLocalState() }
                runSuspendBestEffort { paymentRepository.clearPendingPayment() }
            }
        }
        cancellation?.let { throw it }
    }
}

private inline fun runBestEffort(block: () -> Unit) {
    try {
        block()
    } catch (_: Exception) {
        // Logout cleanup continues even if one local adapter fails.
    }
}

private suspend inline fun runSuspendBestEffort(crossinline block: suspend () -> Unit) {
    try {
        block()
    } catch (_: Exception) {
        // Logout cleanup continues even if one local adapter fails.
    }
}
