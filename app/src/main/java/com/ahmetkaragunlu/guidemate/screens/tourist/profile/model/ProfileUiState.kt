package com.ahmetkaragunlu.guidemate.screens.tourist.profile.model

import com.ahmetkaragunlu.guidemate.components.toLocalCurrency
import com.ahmetkaragunlu.guidemate.screens.guide.wallet.model.BankAccount

data class ProfileUiState(
    val fullName: String = "",
    val email: String = "",
    val balanceAmount: Double = 0.0,
    val depositAmount: String = "",
    val selectedCardId: String? = null,
    val selectedBankAccount: BankAccount? = null,
) {
    val balance: String
        get() = balanceAmount.toLocalCurrency()
}
