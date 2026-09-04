package com.ahmetkaragunlu.guidemate.tour.presentation.tourist.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.navigation.tourist.TouristDestination
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationTargetReference
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationTargetType
import com.ahmetkaragunlu.guidemate.notification.domain.repository.NotificationRepository
import com.ahmetkaragunlu.guidemate.review.domain.repository.ReviewRepository
import com.ahmetkaragunlu.guidemate.tour.domain.model.catalog.TourWithSession
import com.ahmetkaragunlu.guidemate.tour.domain.model.catalog.resolveBookingAvailability
import com.ahmetkaragunlu.guidemate.tour.domain.repository.TourDiscoveryRepository
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.mapper.toTourDetailUiState
import com.ahmetkaragunlu.guidemate.tour.presentation.tourist.detail.model.TouristTourDetailScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class TouristTourDetailViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val tourRepository: TourDiscoveryRepository,
        private val reviewRepository: ReviewRepository,
        private val notificationRepository: NotificationRepository,
    ) : ViewModel() {
        private val sessionId = savedStateHandle.toRoute<TouristDestination.TourDetail>().sessionId
        private val _uiState = MutableStateFlow(TouristTourDetailScreenState())
        val uiState: StateFlow<TouristTourDetailScreenState> = _uiState.asStateFlow()
        private var loadJob: Job? = null

        init {
            refresh()
            observeReviewChanges()
        }

        private fun observeReviewChanges() {
            viewModelScope.launch {
                reviewRepository.reviewChanges.collect {
                    loadJob?.cancel()
                    loadJob = null
                    refresh()
                }
            }
        }

        fun refresh() {
            if (loadJob?.isActive == true) return
            loadJob =
                viewModelScope.launch {
                    _uiState.value = TouristTourDetailScreenState(loadState = ContentLoadState.LOADING)
                    when (val result = tourRepository.getSession(sessionId)) {
                        is DataResult.Success -> {
                            val tourWithReviews = result.data.withPublicReviews()
                            val now = Instant.now()
                            _uiState.value =
                                TouristTourDetailScreenState(
                                    loadState = ContentLoadState.CONTENT,
                                    detail = tourWithReviews.toTourDetailUiState(),
                                    bookingAvailability =
                                        tourWithReviews.resolveBookingAvailability(
                                            hasReservation = false,
                                            now = now,
                                        ),
                                )
                            notificationRepository.markRelatedRead(
                                NotificationTargetReference(
                                    type = NotificationTargetType.TOUR,
                                    targetId = tourWithReviews.tour.id,
                                ),
                            )
                        }
                        is DataResult.Error -> {
                            _uiState.value =
                                TouristTourDetailScreenState(loadState = ContentLoadState.ERROR)
                        }
                    }
                }
        }

        private suspend fun TourWithSession.withPublicReviews(): TourWithSession =
            when (
                val result =
                    reviewRepository.getTourReviews(
                        tourId = tour.id,
                        page = 0,
                        size = REVIEW_PREVIEW_SIZE,
                    )
            ) {
                is DataResult.Success -> copy(tour = tour.copy(recentReviews = result.data.items))
                is DataResult.Error -> this
            }

        private companion object {
            const val REVIEW_PREVIEW_SIZE = 20
        }
    }
