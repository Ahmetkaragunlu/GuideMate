package com.ahmetkaragunlu.guidemate.payment.presentation.status

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.navigation.tourist.payment.PAYMENT_ATTEMPT_ID_ARGUMENT
import com.ahmetkaragunlu.guidemate.payment.presentation.status.model.PaymentAttemptStatus
import com.ahmetkaragunlu.guidemate.payment.presentation.status.model.PaymentMethod
import com.ahmetkaragunlu.guidemate.payment.presentation.status.model.PaymentStatusUiState
import com.ahmetkaragunlu.guidemate.payment.data.mock.TouristPaymentStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class PaymentStatusViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val paymentStore: TouristPaymentStore,
    ) : ViewModel() {
        private val paymentAttemptId: String =
            checkNotNull(savedStateHandle[PAYMENT_ATTEMPT_ID_ARGUMENT])

        val uiState: StateFlow<PaymentStatusUiState> =
            paymentStore.attempts
                .map { attempts ->
                    PaymentStatusUiState(
                        attempt =
                            attempts.firstOrNull {
                                it.paymentAttemptId == paymentAttemptId
                            },
                    )
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue =
                        PaymentStatusUiState(
                            attempt = paymentStore.findAttempt(paymentAttemptId),
                        ),
                )

        fun markProviderRedirectCompleted() {
            val attempt = paymentStore.findAttempt(paymentAttemptId) ?: return
            if (attempt.status == PaymentAttemptStatus.REDIRECTING) {
                paymentStore.updateStatus(paymentAttemptId, PaymentAttemptStatus.VERIFYING)
            }
        }

        fun markMockVerificationCompleted() {
            val attempt = paymentStore.findAttempt(paymentAttemptId) ?: return
            if (attempt.status == PaymentAttemptStatus.VERIFYING) {
                paymentStore.updateStatus(paymentAttemptId, PaymentAttemptStatus.SUCCEEDED)
            }
        }

        fun cancelPayment() {
            val attempt = paymentStore.findAttempt(paymentAttemptId) ?: return
            if (attempt.status == PaymentAttemptStatus.REDIRECTING) {
                paymentStore.updateStatus(paymentAttemptId, PaymentAttemptStatus.CANCELLED)
            }
        }

        fun retryPayment() {
            val attempt = paymentStore.findAttempt(paymentAttemptId) ?: return
            if (
                attempt.status == PaymentAttemptStatus.FAILED ||
                    attempt.status == PaymentAttemptStatus.TIMEOUT
            ) {
                paymentStore.updateStatus(
                    paymentAttemptId,
                    if (attempt.method == PaymentMethod.SAVED_CARD) {
                        PaymentAttemptStatus.REDIRECTING
                    } else {
                        PaymentAttemptStatus.VERIFYING
                    },
                )
            }
        }
    }
