package com.ahmetkaragunlu.guidemate.screens.guide.tours

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.screens.guide.tours.mapper.toGuideTourCardUiModel
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.GuideTourCardUiModel
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.GuideTourCatalogState
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.GuideTourTab
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.TourWithSession
import com.ahmetkaragunlu.guidemate.screens.guide.tours.shared.GuideTourStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal const val GUIDE_MY_TOURS_SELECTED_TAB_RESULT = "guideMyToursSelectedTab"

@HiltViewModel
class GuideMyToursViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val tourStore: GuideTourStore,
    ) : ViewModel() {
        private val _selectedTab = MutableStateFlow(GuideTourTab.ACTIVE)
        val selectedTab = _selectedTab.asStateFlow()

        init {
            viewModelScope.launch {
                savedStateHandle
                    .getStateFlow(GUIDE_MY_TOURS_SELECTED_TAB_RESULT, "")
                    .filter(String::isNotBlank)
                    .collect { tabName ->
                        GuideTourTab.entries.firstOrNull { it.name == tabName }?.let { tab ->
                            _selectedTab.value = tab
                        }
                        savedStateHandle[GUIDE_MY_TOURS_SELECTED_TAB_RESULT] = ""
                    }
            }
        }

        val tours: StateFlow<List<GuideTourCardUiModel>> =
            combine(tourStore.state, _selectedTab) { catalog, tab ->
                catalog.toursFor(tab).map { it.toGuideTourCardUiModel() }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList(),
            )

        fun changeTab(tab: GuideTourTab) {
            _selectedTab.value = tab
        }

        fun toggleBookingAvailability(
            sessionId: String,
            isOpen: Boolean,
        ) {
            tourStore.setSessionBookingOpen(sessionId = sessionId, isOpen = isOpen)
        }

        fun archiveRejectedTour(tourId: String) {
            tourStore.archiveRejectedTour(tourId)
        }

    }

private fun GuideTourCatalogState.toursFor(tab: GuideTourTab): List<TourWithSession> =
    when (tab) {
        GuideTourTab.ACTIVE -> activeTourItems
        GuideTourTab.REVIEW -> reviewTourItems
        GuideTourTab.PAST -> pastTourItems
    }
