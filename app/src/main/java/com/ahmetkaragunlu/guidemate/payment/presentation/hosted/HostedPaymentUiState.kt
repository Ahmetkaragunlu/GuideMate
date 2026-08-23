package com.ahmetkaragunlu.guidemate.payment.presentation.hosted

import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState

data class HostedPaymentUiState(
    val loadState: ContentLoadState = ContentLoadState.LOADING,
    val paymentPageUrl: String? = null,
    val reloadToken: Int = 0,
    val isPageLoading: Boolean = true,
    val pageErrorMessage: String? = null,
    val isCancelling: Boolean = false,
    val shouldVerifyPayment: Boolean = false,
)

