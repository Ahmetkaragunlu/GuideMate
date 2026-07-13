package com.ahmetkaragunlu.guidemate.screens.guide.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.domain.repository.UserRepository
import com.ahmetkaragunlu.guidemate.screens.guide.home.model.GuideHomeUiState
import com.ahmetkaragunlu.guidemate.screens.guide.home.model.dashboardStats
import com.ahmetkaragunlu.guidemate.screens.guide.tours.shared.GuideTourStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class GuideHomeViewModel @Inject constructor(
    userRepository: UserRepository,
    tourStore: GuideTourStore,
) : ViewModel() {
    val uiState: StateFlow<GuideHomeUiState> =
        tourStore.state
            .map { catalog ->
                GuideHomeUiState(
                    pendingCount = catalog.pendingReviewTourItems.size,
                    activeCount = catalog.publishedTourItems.size,
                    dashboardStats = dashboardStats,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = GuideHomeUiState(dashboardStats = dashboardStats),
            )

    val userName: StateFlow<String?> =
        userRepository.userState
            .map { it.firstName }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null,
            )
}
