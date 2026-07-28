package com.ahmetkaragunlu.guidemate.screens.tourist.profile.model

import com.ahmetkaragunlu.guidemate.screens.common.formatting.toLocalCurrencyFromMinorUnit

data class ProfileUiState(
    val fullName: String = "",
    val email: String = "",
    val balanceMinor: Long = 0,
) {
    val balance: String
        get() = balanceMinor.toLocalCurrencyFromMinorUnit()
}
