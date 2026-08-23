package com.ahmetkaragunlu.guidemate.payment.presentation.status.model

data class PaymentStatusUiState(
    val isLoading: Boolean = true,
    val payment: PaymentStatusUiModel? = null,
    val errorMessage: String? = null,
    val statusMessage: String? = null,
    val shouldOpenHostedCheckout: Boolean = false,
)
