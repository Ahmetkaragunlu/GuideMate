package com.ahmetkaragunlu.guidemate.wallet.presentation.guide.earnings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.error.toMessage
import com.ahmetkaragunlu.guidemate.common.ui.formatting.PLATFORM_CURRENCY_CODE
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.wallet.domain.model.MonthlyGuideEarning
import com.ahmetkaragunlu.guidemate.wallet.domain.repository.GuideFinanceRepository
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.earnings.model.GuideEarningsUiState
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.earnings.model.MonthlyEarningUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.YearMonth
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val EARNINGS_YEAR_OPTION_COUNT = 5

@HiltViewModel
class GuideEarningsViewModel
    @Inject
    constructor(
        private val repository: GuideFinanceRepository,
        private val resourceProvider: ResourceProvider,
    ) : ViewModel() {
        private val currentPeriod = YearMonth.now()
        private val mutableUiState =
            MutableStateFlow(
                GuideEarningsUiState(
                    selectedYear = currentPeriod.year,
                    availableYears =
                        (currentPeriod.year downTo
                            currentPeriod.year - EARNINGS_YEAR_OPTION_COUNT + 1).toList(),
                ),
            )
        val uiState: StateFlow<GuideEarningsUiState> = mutableUiState.asStateFlow()
        private var currentMonth: MonthlyEarningUiModel? = null
        private var loadJob: Job? = null

        init {
            refresh()
        }

        fun refresh() {
            loadYear(mutableUiState.value.selectedYear)
        }

        fun selectYear(year: Int) {
            if (year == mutableUiState.value.selectedYear || year !in mutableUiState.value.availableYears) {
                return
            }
            mutableUiState.update { it.copy(selectedYear = year) }
            loadYear(year)
        }

        private fun loadYear(year: Int) {
            loadJob?.cancel()
            loadJob =
                viewModelScope.launch {
                    val hasContent = mutableUiState.value.currentMonth != null
                    if (!hasContent) {
                        mutableUiState.update {
                            it.copy(loadState = ContentLoadState.LOADING, errorMessage = null)
                        }
                    }
                    when (val result = repository.getMonthlyEarnings(year)) {
                        is DataResult.Success -> updateEarnings(year, result.data)
                        is DataResult.Error ->
                            mutableUiState.update {
                                it.copy(
                                    loadState =
                                        if (hasContent) {
                                            ContentLoadState.CONTENT
                                        } else {
                                            ContentLoadState.ERROR
                                        },
                                    errorMessage = result.error.toMessage(resourceProvider),
                                )
                            }
                    }
                }
        }

        private fun updateEarnings(
            year: Int,
            earnings: List<MonthlyGuideEarning>,
        ) {
            val mapped = earnings.map { it.toUiModel() }.sortedByDescending { it.month }
            if (year != mutableUiState.value.selectedYear) return
            if (year == currentPeriod.year) {
                currentMonth =
                    mapped.firstOrNull { it.month == currentPeriod.monthValue }
                        ?: MonthlyEarningUiModel(
                            year = currentPeriod.year,
                            month = currentPeriod.monthValue,
                            amountMinor = 0,
                            currencyCode = mapped.firstOrNull()?.currencyCode ?: PLATFORM_CURRENCY_CODE,
                        )
            }
            mutableUiState.update {
                it.copy(
                    loadState = ContentLoadState.CONTENT,
                    currentMonth = currentMonth,
                    currentYearEarnings =
                        if (year == currentPeriod.year) {
                            listOfNotNull(currentMonth) +
                                mapped.filterNot { earning -> earning.month == currentPeriod.monthValue }
                        } else {
                            it.currentYearEarnings
                        },
                    history =
                        if (year == currentPeriod.year) {
                            mapped.filterNot { earning -> earning.month == currentPeriod.monthValue }
                        } else {
                            mapped
                        },
                    errorMessage = null,
                )
            }
        }

        private fun MonthlyGuideEarning.toUiModel(): MonthlyEarningUiModel =
            MonthlyEarningUiModel(
                year = year,
                month = month,
                amountMinor = netEarningsMinor,
                currencyCode = currencyCode,
            )
    }
