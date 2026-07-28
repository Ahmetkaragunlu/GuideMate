package com.ahmetkaragunlu.guidemate.screens.tourist.finance.model

import java.time.Instant

data class TouristWalletTransactionUiModel(
    val transactionId: String,
    val title: String,
    val amountMinor: Long,
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
