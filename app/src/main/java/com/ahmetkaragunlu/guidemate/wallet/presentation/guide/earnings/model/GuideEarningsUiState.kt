package com.ahmetkaragunlu.guidemate.wallet.presentation.guide.earnings.model

data class GuideEarningsUiState(
    val currentMonth: MonthlyEarningUiModel,
    val history: List<MonthlyEarningUiModel> = emptyList(),
) {
    val allEarnings: List<MonthlyEarningUiModel>
        get() = listOf(currentMonth) + history
}
