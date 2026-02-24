package com.ahmetkaragunlu.guidemate.screens.guide.wallet.model

data class Transaction(
    val id: String,
    val title: String,
    val timestampMs: Long,
    val formattedDate: String,
    val amount: Double,
    val type: TransactionType
)