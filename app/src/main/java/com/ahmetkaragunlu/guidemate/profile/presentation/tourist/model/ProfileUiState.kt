package com.ahmetkaragunlu.guidemate.profile.presentation.tourist.model

import com.ahmetkaragunlu.guidemate.common.ui.formatting.toCurrencyFromMinorUnit
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState

data class ProfileUiState(
    val loadState: ContentLoadState = ContentLoadState.LOADING,
    val fullName: String = "",
    val email: String = "",
    val balanceMinor: Long = 0,
    val currencyCode: String = "USD",
) {
    val balance: String
        get() = balanceMinor.toCurrencyFromMinorUnit(currencyCode)
}
