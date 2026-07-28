package com.ahmetkaragunlu.guidemate.screens.guide.wallet.model

import java.time.Instant

data class WalletTransactionUiModel(
    val id: String,
    val occurredAt: Instant,
    val amountMinor: Long,
    val type: WalletTransactionType,
    val referenceTitle: String? = null,
    val status: WalletTransactionStatus = WalletTransactionStatus.COMPLETED,
    val bankAccountId: String? = null,
)
