package com.ahmetkaragunlu.guidemate.navigation.tourist.payment

import kotlinx.serialization.Serializable

const val PAYMENT_ID_ARGUMENT = "paymentId"
const val OPEN_HOSTED_IF_REQUIRED_ARGUMENT = "openHostedIfRequired"

object TouristPaymentDestination {
    @Serializable data object Wallet

    @Serializable data object WalletTransactions

    @Serializable
    data class Checkout(
        val sessionId: String,
    )

    @Serializable
    data class Hosted(
        val paymentId: String,
    )

    @Serializable
    data class Status(
        val paymentId: String,
        val openHostedIfRequired: Boolean = true,
    )

    @Serializable
    data class Success(
        val paymentId: String,
    )
}
