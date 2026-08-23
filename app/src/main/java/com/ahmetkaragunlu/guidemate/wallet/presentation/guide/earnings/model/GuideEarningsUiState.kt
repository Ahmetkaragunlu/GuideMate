package com.ahmetkaragunlu.guidemate.wallet.presentation.guide.earnings.model

import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState

data class GuideEarningsUiState(
    val loadState: ContentLoadState = ContentLoadState.LOADING,
    val selectedYear: Int,
    val availableYears: List<Int>,
    val currentMonth: MonthlyEarningUiModel? = null,
    val currentYearEarnings: List<MonthlyEarningUiModel> = emptyList(),
    val history: List<MonthlyEarningUiModel> = emptyList(),
    val errorMessage: String? = null,
) {
    val walletPreviewEarnings: List<MonthlyEarningUiModel>
        get() = currentYearEarnings
}
