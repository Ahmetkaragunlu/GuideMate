package com.ahmetkaragunlu.guidemate.screens.guide.tours

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.common.tours.model.catalog.TourCatalogState
import com.ahmetkaragunlu.guidemate.screens.common.tours.model.catalog.TourWithSession
import com.ahmetkaragunlu.guidemate.screens.common.tours.model.operation.TourOperationResult
import com.ahmetkaragunlu.guidemate.screens.common.tours.store.TourCatalogStore
import com.ahmetkaragunlu.guidemate.screens.common.tours.store.refreshAtSessionTransitions
import com.ahmetkaragunlu.guidemate.screens.guide.tours.mapper.toGuideTourCardUiModel
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.GuideTourCardUiModel
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.GuideTourTab
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
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
        private val tourStore: TourCatalogStore,
    ) : ViewModel() {
        private val _selectedTab = MutableStateFlow(GuideTourTab.ACTIVE)
        val selectedTab = _selectedTab.asStateFlow()
        private val _operationMessageResId = MutableStateFlow<Int?>(null)
        val operationMessageResId = _operationMessageResId.asStateFlow()

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
            combine(tourStore.state.refreshAtSessionTransitions(), _selectedTab) { catalog, tab ->
                val now = Instant.now()
                catalog.toursFor(tab = tab, now = now).map { tour ->
                    tour.toGuideTourCardUiModel(now)
                }
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
            showOperationFailure(
                tourStore.setSessionBookingOpen(
                    sessionId = sessionId,
                    isOpen = isOpen,
                ),
            )
        }

        fun archiveRejectedTour(tourId: String) {
            showOperationFailure(tourStore.archiveRejectedTour(tourId))
        }

        fun onOperationMessageShown(@StringRes messageResId: Int) {
            _operationMessageResId.compareAndSet(
                expect = messageResId,
                update = null,
            )
        }

        private fun showOperationFailure(result: TourOperationResult) {
            _operationMessageResId.value = result.errorMessageResId()
        }
    }

@StringRes
private fun TourOperationResult.errorMessageResId(): Int? =
    when (this) {
        TourOperationResult.SUCCESS -> null
        TourOperationResult.SESSION_NOT_FOUND -> R.string.tour_operation_session_not_found
        TourOperationResult.TOUR_NOT_FOUND -> R.string.tour_operation_tour_not_found
        TourOperationResult.SESSION_ALREADY_STARTED -> R.string.tour_operation_session_started
        TourOperationResult.TOUR_NOT_APPROVED -> R.string.tour_operation_not_approved
        TourOperationResult.CAPACITY_FULL -> R.string.tour_operation_capacity_full
        TourOperationResult.STATUS_NOT_MANAGEABLE -> R.string.tour_operation_status_not_manageable
        TourOperationResult.TOUR_NOT_ARCHIVABLE -> R.string.tour_operation_not_archivable
    }

private fun TourCatalogState.toursFor(
    tab: GuideTourTab,
    now: Instant,
): List<TourWithSession> =
    when (tab) {
        GuideTourTab.ACTIVE -> activeTourItemsAt(now)
        GuideTourTab.REVIEW -> reviewTourItems
        GuideTourTab.PAST -> pastTourItemsAt(now)
    }
