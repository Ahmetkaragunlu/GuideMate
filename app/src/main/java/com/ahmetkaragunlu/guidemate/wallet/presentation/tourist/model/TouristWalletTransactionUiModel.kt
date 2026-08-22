package com.ahmetkaragunlu.guidemate.wallet.presentation.tourist.model

import java.time.Instant

data class TouristWalletTransactionUiModel(
    val transactionId: String,
    val referenceTitle: String?,
    val amountMinor: Long,
    val currencyCode: String,
    val type: TouristWalletTransactionType,
    val status: TouristWalletTransactionStatus,
    val createdAt: Instant,
)

enum class TouristWalletTransactionType {
    TOP_UP,
    TOUR_PURCHASE,
    REFUND,
}

enum class TouristWalletTransactionStatus {
    PENDING,
    COMPLETED,
    FAILED,
    REFUNDED,
}
