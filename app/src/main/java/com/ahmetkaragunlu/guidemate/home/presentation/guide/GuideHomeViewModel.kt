package com.ahmetkaragunlu.guidemate.home.presentation.guide

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.auth.domain.repository.UserRepository
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.home.presentation.guide.model.GuideHomeUiState
import com.ahmetkaragunlu.guidemate.home.presentation.guide.model.toDashboardStatistics
import com.ahmetkaragunlu.guidemate.tour.domain.repository.GuideTourRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class GuideHomeViewModel
    @Inject
    constructor(
        userRepository: UserRepository,
        private val tourRepository: GuideTourRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(GuideHomeUiState())
        val uiState = _uiState.asStateFlow()
        private var refreshJob: Job? = null

        val userName: StateFlow<String?> =
            userRepository.userState
                .map { it.firstName }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = null,
                )

        init {
            refreshDashboard()
        }

        fun refreshDashboard() {
            if (refreshJob?.isActive == true) return
            refreshJob =
                viewModelScope.launch {
                    val hasContent = _uiState.value.dashboardStats.isNotEmpty()
                    if (!hasContent) {
                        _uiState.update { it.copy(dashboardLoadState = ContentLoadState.LOADING) }
                    }
                    when (val result = tourRepository.getDashboard()) {
                        is DataResult.Success -> {
                            val dashboard = result.data
                            _uiState.value =
                                GuideHomeUiState(
                                    pendingCount = dashboard.pendingReviewCount,
                                    activeCount = dashboard.activeSessionCount,
                                    dashboardStats = dashboard.toDashboardStatistics(),
                                    currentMonthEarningsMinor =
                                        dashboard.currentMonthEarningsMinor,
                                    currencyCode = dashboard.currencyCode,
                                    dashboardLoadState = ContentLoadState.CONTENT,
                                )
                        }
                        is DataResult.Error -> {
                            _uiState.update {
                                it.copy(
                                    dashboardLoadState =
                                        if (hasContent) {
                                            ContentLoadState.CONTENT
                                        } else {
                                            ContentLoadState.ERROR
                                        },
                                )
                            }
                        }
                    }
                }
        }
    }
