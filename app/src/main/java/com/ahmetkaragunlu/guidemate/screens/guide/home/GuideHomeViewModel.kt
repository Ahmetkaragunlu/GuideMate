package com.ahmetkaragunlu.guidemate.screens.guide.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.domain.repository.UserRepository
import com.ahmetkaragunlu.guidemate.screens.common.guide.performance.store.GuidePerformanceStore
import com.ahmetkaragunlu.guidemate.screens.common.tours.store.TourCatalogStore
import com.ahmetkaragunlu.guidemate.screens.common.tours.store.refreshAtSessionTransitions
import com.ahmetkaragunlu.guidemate.screens.guide.home.model.GuideHomeUiState
import com.ahmetkaragunlu.guidemate.screens.guide.home.model.toDashboardStatistics
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class GuideHomeViewModel @Inject constructor(
    userRepository: UserRepository,
    tourStore: TourCatalogStore,
    performanceStore: GuidePerformanceStore,
) : ViewModel() {
    val uiState: StateFlow<GuideHomeUiState> =
        combine(
            tourStore.state.refreshAtSessionTransitions(),
            performanceStore.summary,
        ) { catalog, performance ->
            val now = Instant.now()
            GuideHomeUiState(
                pendingCount = catalog.pendingReviewTourItems.size,
                activeCount = catalog.bookableTourItemsAt(now).size,
                dashboardStats = performance.toDashboardStatistics(),
            )
        }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue =
                    GuideHomeUiState(
                        dashboardStats =
                            performanceStore.summary.value.toDashboardStatistics(),
                    ),
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
