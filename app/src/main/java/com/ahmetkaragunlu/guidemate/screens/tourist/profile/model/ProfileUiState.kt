package com.ahmetkaragunlu.guidemate.screens.tourist.profile.model

import com.ahmetkaragunlu.guidemate.components.toLocalCurrency
import com.ahmetkaragunlu.guidemate.screens.common.moneyaction.model.MoneyActionMethodUi

data class ProfileUiState(
    val fullName: String = "",
    val email: String = "",
    val balanceAmount: Double = 1500.0,
    val depositAmount: String = "",
    val selectedCardId: String? = null,
    val selectedMethod: MoneyActionMethodUi? = null,
) {
    val balance: String
        get() = balanceAmount.toLocalCurrency()
}
