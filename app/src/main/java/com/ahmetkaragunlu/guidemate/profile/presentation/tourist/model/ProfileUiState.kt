package com.ahmetkaragunlu.guidemate.profile.presentation.tourist.model

import com.ahmetkaragunlu.guidemate.common.ui.formatting.toPlatformCurrencyFromMinorUnit

data class ProfileUiState(
    val fullName: String = "",
    val email: String = "",
    val balanceMinor: Long = 0,
) {
    val balance: String
        get() = balanceMinor.toPlatformCurrencyFromMinorUnit()
}
