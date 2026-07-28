package com.ahmetkaragunlu.guidemate.screens.guide.earnings.viewmodel

import androidx.lifecycle.ViewModel
import com.ahmetkaragunlu.guidemate.screens.guide.earnings.model.GuideEarningsUiState
import com.ahmetkaragunlu.guidemate.screens.guide.earnings.model.MonthlyEarningUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class GuideEarningsViewModel
    @Inject
    constructor() : ViewModel() {
        private val _uiState = MutableStateFlow(initialUiState())
        val uiState: StateFlow<GuideEarningsUiState> = _uiState.asStateFlow()

        // Mock data (MVP)
        private fun initialUiState(): GuideEarningsUiState =
            GuideEarningsUiState(
                currentMonth =
                    MonthlyEarningUiModel(
                        year = 2026,
                        month = 7,
                        amountMinor = 1_250_000,
                    ),
                history =
                    listOf(
                        MonthlyEarningUiModel(2026, 6, 1_000_000),
                        MonthlyEarningUiModel(2026, 5, 850_000),
                        MonthlyEarningUiModel(2026, 4, 1_200_000),
                        MonthlyEarningUiModel(2026, 3, 700_000),
                        MonthlyEarningUiModel(2026, 2, 950_000),
                        MonthlyEarningUiModel(2026, 1, 825_000),
                    ),
            )
    }
