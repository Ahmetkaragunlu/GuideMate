package com.ahmetkaragunlu.guidemate.navigation.tourist.payment

import kotlinx.serialization.Serializable

const val PAYMENT_ATTEMPT_ID_ARGUMENT = "paymentAttemptId"

object TouristPaymentDestination {
    @Serializable data object Wallet

    @Serializable data object WalletTransactions

    @Serializable
    data class Checkout(
        val sessionId: String,
    )

    @Serializable
    data class Status(
        val paymentAttemptId: String,
    )

    @Serializable
    data class Success(
        val paymentAttemptId: String,
    )
}
