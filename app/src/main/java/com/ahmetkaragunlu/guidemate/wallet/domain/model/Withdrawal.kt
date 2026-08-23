package com.ahmetkaragunlu.guidemate.wallet.domain.model

import java.time.Instant

data class Withdrawal(
    val id: String,
    val bankAccountId: String,
    val maskedIban: String,
    val amountMinor: Long,
    val currencyCode: String,
    val status: WithdrawalStatus,
    val payoutMode: PayoutMode,
    val requestedAt: Instant,
    val completedAt: Instant?,
    val failureCode: String?,
)

enum class WithdrawalStatus {
    PENDING,
    PROCESSING,
    COMPLETED,
    FAILED,
    CANCELLED,
}

enum class PayoutMode {
    IYZICO,
    SIMULATED,
}
