package com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.error.toMessage
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.tour.domain.model.guide.GuideTourListType
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.TourSessionStatus
import com.ahmetkaragunlu.guidemate.tour.domain.repository.GuideTourRepository
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.mapper.toGuideTourCardUiModel
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.model.GuideMyToursUiState
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.model.GuideTourTab
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class GuideMyToursViewModel
    @Inject
    constructor(
        private val repository: GuideTourRepository,
        private val resourceProvider: ResourceProvider,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(GuideMyToursUiState())
        val uiState = _uiState.asStateFlow()

        private var listJob: Job? = null
        private var currentPage = 0

        init {
            refresh()
        }

        fun changeTab(tab: GuideTourTab) {
            if (tab == _uiState.value.selectedTab) return
            _uiState.update { it.copy(selectedTab = tab) }
            refresh()
        }

        fun applyNavigationResult(tab: GuideTourTab) {
            _uiState.update { it.copy(selectedTab = tab) }
            refresh()
        }

        fun refresh() {
            listJob?.cancel()
            listJob = loadTours(page = 0, append = false)
        }

        fun loadMore() {
            val state = _uiState.value
            if (!state.canLoadMore || state.isLoadingMore || listJob?.isActive == true) return
            listJob = loadTours(page = currentPage + 1, append = true)
        }

        fun toggleBookingAvailability(
            sessionId: String,
            isOpen: Boolean,
        ) {
            if (sessionId in _uiState.value.pendingSessionIds) return
            _uiState.update { it.copy(pendingSessionIds = it.pendingSessionIds + sessionId) }
            viewModelScope.launch {
                when (val result = repository.setSessionBookingOpen(sessionId, isOpen)) {
                    is DataResult.Success -> {
                        val status =
                            if (isOpen) {
                                TourSessionStatus.OPEN_FOR_BOOKING
                            } else {
                                TourSessionStatus.CLOSED
                            }
                        _uiState.update { state ->
                            state.copy(
                                tours =
                                    state.tours.map { tour ->
                                        if (tour.id == sessionId) {
                                            tour.copy(
                                                sessionStatus = status,
                                            )
                                        } else {
                                            tour
                                        }
                                    },
                            )
                        }
                    }
                    is DataResult.Error -> showMessage(result.error.toMessage(resourceProvider))
                }
                _uiState.update { it.copy(pendingSessionIds = it.pendingSessionIds - sessionId) }
            }
        }

        fun archiveRejectedTour(tourId: String) {
            if (tourId in _uiState.value.pendingArchiveTourIds) return
            _uiState.update { it.copy(pendingArchiveTourIds = it.pendingArchiveTourIds + tourId) }
            viewModelScope.launch {
                when (val result = repository.archiveTour(tourId)) {
                    is DataResult.Success -> {
                        _uiState.update { state ->
                            state.copy(tours = state.tours.filterNot { it.tourId == tourId })
                        }
                    }
                    is DataResult.Error -> showMessage(result.error.toMessage(resourceProvider))
                }
                _uiState.update { it.copy(pendingArchiveTourIds = it.pendingArchiveTourIds - tourId) }
            }
        }

        fun onUserMessageShown() {
            _uiState.update { it.copy(userMessage = null) }
        }

        private fun loadTours(
            page: Int,
            append: Boolean,
        ): Job =
            viewModelScope.launch {
                if (append) {
                    _uiState.update { it.copy(isLoadingMore = true, appendFailed = false) }
                } else {
                    _uiState.update {
                        it.copy(
                            loadState = ContentLoadState.LOADING,
                            isLoadingMore = false,
                            appendFailed = false,
                        )
                    }
                }

                when (
                    val result =
                        repository.getTours(
                            tab = _uiState.value.selectedTab.toDomainTab(),
                            page = page,
                            size = PAGE_SIZE,
                        )
                ) {
                    is DataResult.Success -> {
                        val mapped = result.data.items.map { it.toGuideTourCardUiModel() }
                        currentPage = result.data.page
                        _uiState.update { state ->
                            state.copy(
                                tours = if (append) state.tours + mapped else mapped,
                                loadState = ContentLoadState.CONTENT,
                                isLoadingMore = false,
                                appendFailed = false,
                                canLoadMore = !result.data.isLast,
                            )
                        }
                    }
                    is DataResult.Error -> {
                        _uiState.update { state ->
                            state.copy(
                                loadState =
                                    if (append || state.tours.isNotEmpty()) {
                                        ContentLoadState.CONTENT
                                    } else {
                                        ContentLoadState.ERROR
                                    },
                                isLoadingMore = false,
                                appendFailed = append,
                                userMessage =
                                    result.error.toMessage(resourceProvider).takeIf {
                                        append || state.tours.isNotEmpty()
                                    },
                            )
                        }
                    }
                }
            }

        private fun showMessage(message: String) {
            _uiState.update { it.copy(userMessage = message) }
        }

        private companion object {
            const val PAGE_SIZE = 20
        }
    }

private fun GuideTourTab.toDomainTab(): GuideTourListType =
    when (this) {
        GuideTourTab.ACTIVE -> GuideTourListType.ACTIVE
        GuideTourTab.REVIEW -> GuideTourListType.REVIEW
        GuideTourTab.PAST -> GuideTourListType.PAST
    }
