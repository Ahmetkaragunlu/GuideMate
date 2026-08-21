package com.ahmetkaragunlu.guidemate.reservation.presentation.trips

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.tour.domain.model.catalog.TourCatalogState
import com.ahmetkaragunlu.guidemate.tour.data.mock.TourCatalogStore
import com.ahmetkaragunlu.guidemate.tour.data.mock.refreshAtSessionTransitions
import com.ahmetkaragunlu.guidemate.reservation.presentation.mapper.toTripUiModel
import com.ahmetkaragunlu.guidemate.reservation.domain.model.TouristReservation
import com.ahmetkaragunlu.guidemate.reservation.data.mock.TouristReservationStore
import com.ahmetkaragunlu.guidemate.reservation.presentation.trips.model.TripTab
import com.ahmetkaragunlu.guidemate.reservation.presentation.trips.model.TripUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import javax.inject.Inject
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class TouristTripsViewModel
    @Inject
    constructor(
        private val reservationStore: TouristReservationStore,
        tourCatalogStore: TourCatalogStore,
    ) : ViewModel() {
        private val _selectedTab = MutableStateFlow(TripTab.UPCOMING)
        val selectedTab = _selectedTab.asStateFlow()

        val trips: StateFlow<List<TripUiModel>> =
            combine(
                reservationStore.reservations,
                tourCatalogStore.state.refreshAtSessionTransitions(),
                _selectedTab,
            ) {
                    reservations,
                    catalog,
                    tab,
                ->
                    reservations.toTripUiModels(
                        catalog = catalog,
                        tab = tab,
                        now = Instant.now(),
                    )
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue =
                        reservationStore.reservations.value.toTripUiModels(
                            catalog = tourCatalogStore.state.value,
                            tab = TripTab.UPCOMING,
                            now = Instant.now(),
                        ),
                )

        fun changeTab(tab: TripTab) {
            _selectedTab.value = tab
        }

        fun cancelReservation(reservationId: String): Boolean {
            val cancelled = reservationStore.cancelReservation(reservationId)
            if (cancelled) {
                _selectedTab.value = TripTab.PAST
            }
            return cancelled
        }
    }

private fun List<TouristReservation>.toTripUiModels(
    catalog: TourCatalogState,
    tab: TripTab,
    now: Instant,
): List<TripUiModel> {
    val trips =
        map { reservation ->
            reservation.toTripUiModel(
                currentTour = catalog.findBySessionId(reservation.tourSessionId),
                now = now,
            )
        }
    return when (tab) {
        TripTab.UPCOMING -> trips.filterNot(TripUiModel::isPast).sortedBy(TripUiModel::startsAt)
        TripTab.PAST -> trips.filter(TripUiModel::isPast).sortedByDescending(TripUiModel::startsAt)
    }
}
