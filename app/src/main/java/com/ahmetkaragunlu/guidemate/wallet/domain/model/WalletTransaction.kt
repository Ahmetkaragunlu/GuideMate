package com.ahmetkaragunlu.guidemate.wallet.domain.model

import java.time.Instant

data class WalletTransaction(
    val id: String,
    val direction: WalletTransactionDirection,
    val type: WalletTransactionType,
    val amountMinor: Long,
    val currencyCode: String,
    val referenceType: String?,
    val referenceId: String?,
    val referenceTitle: String?,
    val occurredAt: Instant,
)

enum class WalletTransactionDirection {
    CREDIT,
    DEBIT,
}

enum class WalletTransactionType {
    TOP_UP,
    TOUR_PURCHASE,
    REFUND,
    GUIDE_EARNING,
    WITHDRAWAL,
    EARNING_REVERSAL,
}
