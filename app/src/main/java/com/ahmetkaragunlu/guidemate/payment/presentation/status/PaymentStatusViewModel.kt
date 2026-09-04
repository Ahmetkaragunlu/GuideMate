package com.ahmetkaragunlu.guidemate.payment.presentation.status

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.BackendErrorCode
import com.ahmetkaragunlu.guidemate.common.ui.error.toMessage
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider
import com.ahmetkaragunlu.guidemate.navigation.tourist.payment.OPEN_HOSTED_IF_REQUIRED_ARGUMENT
import com.ahmetkaragunlu.guidemate.navigation.tourist.payment.PAYMENT_ID_ARGUMENT
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationTargetReference
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationTargetType
import com.ahmetkaragunlu.guidemate.notification.domain.repository.NotificationRepository
import com.ahmetkaragunlu.guidemate.payment.domain.model.Payment
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentPurpose
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentStatus
import com.ahmetkaragunlu.guidemate.payment.domain.repository.PaymentRepository
import com.ahmetkaragunlu.guidemate.payment.presentation.status.model.PaymentStatusUiState
import com.ahmetkaragunlu.guidemate.payment.presentation.status.model.PaymentUiStatus
import com.ahmetkaragunlu.guidemate.payment.presentation.status.model.toStatusUiModel
import com.ahmetkaragunlu.guidemate.wallet.domain.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class PaymentStatusViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val paymentRepository: PaymentRepository,
        private val walletRepository: WalletRepository,
        private val notificationRepository: NotificationRepository,
        private val resourceProvider: ResourceProvider,
    ) : ViewModel() {
        private val paymentId: String = checkNotNull(savedStateHandle[PAYMENT_ID_ARGUMENT])
        private val openHostedIfRequired: Boolean =
            savedStateHandle[OPEN_HOSTED_IF_REQUIRED_ARGUMENT] ?: false
        private val mutableUiState = MutableStateFlow(PaymentStatusUiState())
        val uiState: StateFlow<PaymentStatusUiState> = mutableUiState.asStateFlow()
        private var pollingJob: Job? = null
        private var relatedNotificationsMarkedRead = false

        init {
            refresh()
        }

        fun refresh() {
            pollingJob?.cancel()
            pollingJob =
                viewModelScope.launch {
                    mutableUiState.update {
                        it.copy(isLoading = true, errorMessage = null)
                    }
                    pollUntilResolved()
                }
        }

        private suspend fun pollUntilResolved() {
            while (true) {
                when (val result = paymentRepository.getPayment(paymentId)) {
                    is DataResult.Error -> {
                        if (
                            result.error is AppError.Backend &&
                                result.error.code == BackendErrorCode.PAYMENT_NOT_FOUND
                        ) {
                            paymentRepository.clearPendingPayment(paymentId)
                        }
                        mutableUiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.error.toMessage(resourceProvider),
                            )
                        }
                        return
                    }
                    is DataResult.Success -> {
                        val payment = result.data
                        markRelatedNotificationsRead()
                        if (
                            openHostedIfRequired &&
                                payment.status == PaymentStatus.REQUIRES_ACTION &&
                                payment.paymentPageUrl != null
                        ) {
                            mutableUiState.value =
                                PaymentStatusUiState(
                                    isLoading = false,
                                    payment = payment.toStatusUiModel(),
                                    shouldOpenHostedCheckout = true,
                                )
                            return
                        }
                        if (
                            openHostedIfRequired &&
                                payment.status == PaymentStatus.REQUIRES_ACTION
                        ) {
                            mutableUiState.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessage =
                                        resourceProvider.getString(
                                            R.string.payment_invalid_url_error,
                                        ),
                                )
                            }
                            return
                        }

                        val walletVerified = verifyWalletProjectionIfNeeded(payment)
                        val uiModel = payment.toStatusUiModel(walletVerified)
                        mutableUiState.value =
                            PaymentStatusUiState(
                                isLoading = false,
                                payment = uiModel,
                                statusMessage =
                                    payment.failureCode?.let { failureCode ->
                                        AppError
                                            .Backend(
                                                code = BackendErrorCode.from(failureCode),
                                                fallbackMessage = null,
                                            ).toMessage(resourceProvider)
                                    },
                            )
                        if (uiModel.status != PaymentUiStatus.VERIFYING) {
                            paymentRepository.clearPendingPayment(paymentId)
                            return
                        }
                        delay(POLL_INTERVAL_MILLIS)
                    }
                }
            }
        }

        private suspend fun verifyWalletProjectionIfNeeded(payment: Payment): Boolean {
            if (
                payment.purpose != PaymentPurpose.WALLET_TOP_UP ||
                    payment.status != PaymentStatus.SUCCEEDED
            ) {
                return true
            }
            val wallet = walletRepository.getWallet()
            val transactions = walletRepository.getTransactions(page = 0, size = 1)
            return wallet is DataResult.Success && transactions is DataResult.Success
        }

        private suspend fun markRelatedNotificationsRead() {
            if (relatedNotificationsMarkedRead) return
            val result =
                notificationRepository.markRelatedRead(
                    NotificationTargetReference(
                        type = NotificationTargetType.PAYMENT,
                        targetId = paymentId,
                    ),
                )
            relatedNotificationsMarkedRead = result is DataResult.Success
        }

        private companion object {
            const val POLL_INTERVAL_MILLIS = 2_000L
        }
    }
