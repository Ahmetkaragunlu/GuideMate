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
                                    paymentPageUrl = url,
                                    isPageLoading = true,
                                )
                            }
                        }
                    }
                }
            }
        }

        fun onPageFinished() {
            mutableUiState.update { it.copy(isPageLoading = false, pageErrorMessage = null) }
            startPolling()
        }

        fun onPageError(message: String) {
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
                    while (true) {
                        delay(POLL_INTERVAL_MILLIS)
                        when (val result = paymentRepository.getPayment(paymentId)) {
                            is DataResult.Success ->
                                if (result.data.status != PaymentStatus.REQUIRES_ACTION) {
                                    mutableUiState.update { it.copy(shouldVerifyPayment = true) }
                                    return@launch
                                }
                            is DataResult.Error -> Unit
                        }
                    }
                }
        }

        private companion object {
            const val POLL_INTERVAL_MILLIS = 2_000L
        }
    }

internal fun String.isSecureHostedPaymentUrl(): Boolean =
    runCatching {
        java.net.URI(this).let { uri ->
            uri.scheme.equals("https", ignoreCase = true) && !uri.host.isNullOrBlank()
        }
    }.getOrDefault(false)
