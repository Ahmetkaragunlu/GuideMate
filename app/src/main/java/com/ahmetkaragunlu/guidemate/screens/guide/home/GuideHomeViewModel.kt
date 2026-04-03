package com.ahmetkaragunlu.guidemate.screens.guide.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.domain.repository.UserRepository
import com.ahmetkaragunlu.guidemate.screens.guide.home.model.GuideHomeUiState
import com.ahmetkaragunlu.guidemate.screens.guide.home.model.dashboardStats
import com.ahmetkaragunlu.guidemate.screens.guide.home.model.recentActivities
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class GuideHomeViewModel @Inject constructor(
    userRepository: UserRepository,
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            GuideHomeUiState(
                monthlyEarnings = 12500.0,
                pendingCount = 1,
                activeCount = 3,
                dashboardStats = dashboardStats,
                recentActivities = recentActivities,
            ),
        )
    val uiState: StateFlow<GuideHomeUiState> = _uiState.asStateFlow()

    val userName: StateFlow<String?> =
        userRepository.userState
            .map { it.firstName }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null,
            )
}
