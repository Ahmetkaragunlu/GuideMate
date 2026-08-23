package com.ahmetkaragunlu.guidemate.payment.presentation.model

data class PaymentLaunch(
    val paymentId: String,
    val requiresHostedCheckout: Boolean,
)

