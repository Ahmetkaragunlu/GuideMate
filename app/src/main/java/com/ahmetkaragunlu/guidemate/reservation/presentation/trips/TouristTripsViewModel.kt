package com.ahmetkaragunlu.guidemate.reservation.presentation.trips

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.error.toMessage
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.reservation.domain.model.CancelReservationInput
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationCancellationResult
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationListType
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationRefundEligibility
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationRefundStatus
import com.ahmetkaragunlu.guidemate.reservation.domain.model.TouristReservation
import com.ahmetkaragunlu.guidemate.reservation.domain.repository.ReservationRepository
import com.ahmetkaragunlu.guidemate.reservation.presentation.mapper.toTripUiModel
import com.ahmetkaragunlu.guidemate.reservation.presentation.trips.model.ReservationCancellationFeedback
import com.ahmetkaragunlu.guidemate.reservation.presentation.trips.model.TouristTripsUiState
import com.ahmetkaragunlu.guidemate.reservation.presentation.trips.model.TripTab
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class TouristTripsViewModel
    @Inject
    constructor(
        private val reservationRepository: ReservationRepository,
        private val resourceProvider: ResourceProvider,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(TouristTripsUiState())
        val uiState: StateFlow<TouristTripsUiState> = _uiState.asStateFlow()

        private var reservations: List<TouristReservation> = emptyList()
        private var nextPage = 0
        private var loadJob: Job? = null
        private val cancellationKeys = mutableMapOf<String, String>()

        init {
            refresh()
        }

        fun changeTab(tab: TripTab) {
            if (tab == uiState.value.selectedTab) return
            _uiState.update { it.copy(selectedTab = tab) }
            refresh()
        }

        fun refresh() {
            loadPage(reset = true)
        }

        fun loadMore() {
            val state = uiState.value
            if (!state.canLoadMore || state.isLoadingMore || loadJob?.isActive == true) return
            loadPage(reset = false)
        }

        fun cancelReservation(reservationId: String) {
            if (uiState.value.cancellingReservationId != null) return
            val reservation = reservations.firstOrNull { it.id == reservationId } ?: return
            val idempotencyKey =
                cancellationKeys.getOrPut(reservationId) { UUID.randomUUID().toString() }

            _uiState.update {
                it.copy(
                    cancellingReservationId = reservationId,
                    cancellationFeedback = null,
                )
            }
            viewModelScope.launch {
                when (
                    val result =
                        reservationRepository.cancelReservation(
                            reservationId = reservationId,
                            input = CancelReservationInput(version = reservation.version),
                            idempotencyKey = idempotencyKey,
                        )
                ) {
                    is DataResult.Success -> {
                        cancellationKeys.remove(reservationId)
                        _uiState.update {
                            it.copy(
                                selectedTab = TripTab.PAST,
                                cancellingReservationId = null,
                                cancellationFeedback = result.data.toFeedback(),
                            )
                        }
                        refresh()
                    }
                    is DataResult.Error -> {
                        _uiState.update {
                            it.copy(
                                cancellingReservationId = null,
                                cancellationFeedback =
                                    ReservationCancellationFeedback(
                                        isSuccess = false,
                                        message = result.error.toMessage(resourceProvider),
                                    ),
                            )
                        }
                    }
                }
            }
        }

        fun dismissCancellationFeedback() {
            _uiState.update { it.copy(cancellationFeedback = null) }
        }

        private fun loadPage(reset: Boolean) {
            loadJob?.cancel()
            val requestedTab = uiState.value.selectedTab
            val requestedPage = if (reset) 0 else nextPage
            loadJob =
                viewModelScope.launch {
                    _uiState.update {
                        if (reset) {
                            it.copy(
                                loadState = ContentLoadState.LOADING,
                                trips = emptyList(),
                                canLoadMore = false,
                                isLoadingMore = false,
                                appendFailed = false,
                            )
                        } else {
                            it.copy(isLoadingMore = true, appendFailed = false)
                        }
                    }

                    when (
                        val result =
                            reservationRepository.getMyReservations(
                                type = requestedTab.toReservationListType(),
                                page = requestedPage,
                                size = PAGE_SIZE,
                            )
                    ) {
                        is DataResult.Success -> {
                            if (uiState.value.selectedTab != requestedTab) return@launch
                            reservations =
                                if (reset) result.data.items else reservations + result.data.items
                            nextPage = result.data.page + 1
                            _uiState.update {
                                it.copy(
                                    loadState = ContentLoadState.CONTENT,
                                    trips = reservations.map(TouristReservation::toTripUiModel),
                                    canLoadMore = !result.data.isLast,
                                    isLoadingMore = false,
                                    appendFailed = false,
                                )
                            }
                        }
                        is DataResult.Error -> {
                            if (uiState.value.selectedTab != requestedTab) return@launch
                            _uiState.update {
                                if (reset) {
                                    it.copy(
                                        loadState = ContentLoadState.ERROR,
                                        isLoadingMore = false,
                                    )
                                } else {
                                    it.copy(isLoadingMore = false, appendFailed = true)
                                }
                            }
                        }
                    }
                }
        }

        private fun ReservationCancellationResult.toFeedback(): ReservationCancellationFeedback {
            val messageResId =
                when {
                    refundStatus == ReservationRefundStatus.SUCCEEDED ->
                        R.string.reservation_cancelled_refund_completed
                    refundStatus == ReservationRefundStatus.FAILED ->
                        R.string.reservation_cancelled_refund_failed
                    refundStatus == ReservationRefundStatus.MANUAL_REVIEW ->
                        R.string.reservation_cancelled_refund_review
                    refundStatus == ReservationRefundStatus.REQUESTED ||
                        refundStatus == ReservationRefundStatus.PROCESSING ->
                        R.string.reservation_cancelled_refund_started
                    refundEligibility == ReservationRefundEligibility.NO_REFUND ->
                        R.string.reservation_cancelled_no_refund
                    else -> R.string.reservation_cancelled_success
                }
            return ReservationCancellationFeedback(
                isSuccess = true,
                message = resourceProvider.getString(messageResId),
            )
        }

        private fun TripTab.toReservationListType(): ReservationListType =
            when (this) {
                TripTab.UPCOMING -> ReservationListType.UPCOMING
                TripTab.PAST -> ReservationListType.PAST
            }

        private companion object {
            const val PAGE_SIZE = 20
        }
    }
