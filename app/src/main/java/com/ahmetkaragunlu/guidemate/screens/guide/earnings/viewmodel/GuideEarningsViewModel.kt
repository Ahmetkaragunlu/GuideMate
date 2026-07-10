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
                        amount = 12500.0,
                    ),
                history =
                    listOf(
                        MonthlyEarningUiModel(2026, 6, 10000.0),
                        MonthlyEarningUiModel(2026, 5, 8500.0),
                        MonthlyEarningUiModel(2026, 4, 12000.0),
                        MonthlyEarningUiModel(2026, 3, 7000.0),
                        MonthlyEarningUiModel(2026, 2, 9500.0),
                        MonthlyEarningUiModel(2026, 1, 8250.0),
                    ),
            )
    }
