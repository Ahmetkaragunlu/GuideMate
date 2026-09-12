package com.ahmetkaragunlu.guidemate.payment.presentation.hosted

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.error.toMessage
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.navigation.tourist.payment.PAYMENT_ID_ARGUMENT
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentStatus
import com.ahmetkaragunlu.guidemate.payment.domain.repository.PaymentRepository
import com.ahmetkaragunlu.guidemate.payment.presentation.status.model.toStatusUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.min
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HostedPaymentViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val paymentRepository: PaymentRepository,
        private val resourceProvider: ResourceProvider,
    ) : ViewModel() {
        private val paymentId: String = checkNotNull(savedStateHandle[PAYMENT_ID_ARGUMENT])
        private val mutableUiState = MutableStateFlow(HostedPaymentUiState())
        val uiState: StateFlow<HostedPaymentUiState> = mutableUiState.asStateFlow()
        private var pollingJob: Job? = null
        private var minimumVerificationJob: Job? = null
        private var minimumVerificationDurationElapsed = false
        private var backendPollingFinished = false

        init {
            loadPaymentPage()
        }

        fun loadPaymentPage() {
            viewModelScope.launch {
                mutableUiState.update {
                    it.copy(loadState = ContentLoadState.LOADING, pageErrorMessage = null)
                }
                when (val result = paymentRepository.getPayment(paymentId)) {
                    is DataResult.Error ->
                        mutableUiState.update { it.copy(loadState = ContentLoadState.ERROR) }
                    is DataResult.Success -> {
                        val payment = result.data
                        val url = payment.paymentPageUrl
                        if (payment.status != PaymentStatus.REQUIRES_ACTION || url == null) {
                            mutableUiState.update {
                                it.copy(
                                    loadState = ContentLoadState.CONTENT,
                                    payment = payment.toStatusUiModel(),
                                    shouldVerifyPayment = true,
                                )
                            }
                        } else if (!url.isSecureHostedPaymentUrl()) {
                            mutableUiState.update {
                                it.copy(
                                    loadState = ContentLoadState.ERROR,
                                    pageErrorMessage =
                                        resourceProvider.getString(R.string.payment_invalid_url_error),
                                )
                            }
                        } else {
                            mutableUiState.update {
                                it.copy(
                                    loadState = ContentLoadState.CONTENT,
                                    payment = payment.toStatusUiModel(),
                                    paymentPageUrl = url,
                                    isPageLoading = true,
                                )
                            }
                        }
                    }
                }
            }
        }

        fun onPageStarted(url: String?) {
            if (url?.isPaymentCallbackUrl() == true) beginCallbackVerification()
        }

        fun onPageFinished(url: String?) {
            if (url?.isPaymentCallbackUrl() == true) beginCallbackVerification()
            if (!uiState.value.isVerifyingCallback) {
                mutableUiState.update { it.copy(isPageLoading = false, pageErrorMessage = null) }
            }
        }

        fun onPageError(message: String) {
            if (uiState.value.isVerifyingCallback) return
            mutableUiState.update {
                it.copy(isPageLoading = false, pageErrorMessage = message)
            }
        }

        fun retryPage() {
            mutableUiState.update {
                it.copy(
                    reloadToken = it.reloadToken + 1,
                    isPageLoading = true,
                    pageErrorMessage = null,
                )
            }
        }

        fun cancelPayment() {
            if (uiState.value.isCancelling) return
            viewModelScope.launch {
                mutableUiState.update { it.copy(isCancelling = true) }
                when (val result = paymentRepository.cancelPayment(paymentId)) {
                    is DataResult.Success ->
                        mutableUiState.update {
                            it.copy(isCancelling = false, shouldVerifyPayment = true)
                        }
                    is DataResult.Error ->
                        mutableUiState.update {
                            it.copy(
                                isCancelling = false,
                                pageErrorMessage = result.error.toMessage(resourceProvider),
                            )
                        }
                }
            }
        }

        private fun startPolling() {
            if (pollingJob?.isActive == true) return
            pollingJob =
                viewModelScope.launch {
                    var elapsedMillis = 0L
                    var nextDelayMillis = INITIAL_POLL_INTERVAL_MILLIS
                    while (elapsedMillis < MAX_POLLING_DURATION_MILLIS) {
                        val delayMillis =
                            min(
                                nextDelayMillis,
                                MAX_POLLING_DURATION_MILLIS - elapsedMillis,
                            )
                        delay(delayMillis)
                        elapsedMillis += delayMillis
                        when (val result = paymentRepository.getPayment(paymentId)) {
                            is DataResult.Success ->
                                if (result.data.status != PaymentStatus.REQUIRES_ACTION) {
                                    if (!uiState.value.isVerifyingCallback) {
                                        beginCallbackVerification()
                                    }
                                    backendPollingFinished = true
                                    completeCallbackVerificationIfReady()
                                    return@launch
                                }
                            is DataResult.Error -> Unit
                        }
                        nextDelayMillis =
                            (nextDelayMillis * 2).coerceAtMost(MAX_POLL_INTERVAL_MILLIS)
                    }
                    backendPollingFinished = true
                    completeCallbackVerificationIfReady()
                }
        }

        private fun beginCallbackVerification() {
            if (uiState.value.isVerifyingCallback) return
            minimumVerificationDurationElapsed = false
            backendPollingFinished = false
            mutableUiState.update {
                it.copy(
                    isPageLoading = false,
                    pageErrorMessage = null,
                    isVerifyingCallback = true,
                )
            }
            minimumVerificationJob?.cancel()
            minimumVerificationJob =
                viewModelScope.launch {
                    delay(MINIMUM_VERIFICATION_DURATION_MILLIS)
                    minimumVerificationDurationElapsed = true
                    completeCallbackVerificationIfReady()
                }
            startPolling()
        }

        private fun completeCallbackVerificationIfReady() {
            if (minimumVerificationDurationElapsed && backendPollingFinished) {
                mutableUiState.update { it.copy(shouldVerifyPayment = true) }
            }
        }

        private companion object {
            const val INITIAL_POLL_INTERVAL_MILLIS = 2_000L
            const val MAX_POLL_INTERVAL_MILLIS = 8_000L
            const val MAX_POLLING_DURATION_MILLIS = 30_000L
            const val MINIMUM_VERIFICATION_DURATION_MILLIS = 3_000L
        }
    }

internal fun String.isSecureHostedPaymentUrl(): Boolean =
    runCatching {
        java.net.URI(this).let { uri ->
            uri.scheme.equals("https", ignoreCase = true) && !uri.host.isNullOrBlank()
        }
    }.getOrDefault(false)

internal fun String.isPaymentCallbackUrl(): Boolean =
    runCatching {
        java.net.URI(this).path?.trimEnd('/') == "/api/v1/payments/iyzico/callback"
    }.getOrDefault(false)
