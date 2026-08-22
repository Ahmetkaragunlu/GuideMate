package com.ahmetkaragunlu.guidemate.wallet.domain.model

data class WalletAccount(
    val balanceMinor: Long,
    val availableBalanceMinor: Long,
    val currencyCode: String,
)
