package com.ahmetkaragunlu.guidemate.wallet.domain.model

import java.time.Instant

data class BankAccount(
    val id: String,
    val maskedIban: String,
    val bankCode: String,
    val bankName: String,
    val accountHolderName: String,
    val isDefault: Boolean,
    val createdAt: Instant,
)
