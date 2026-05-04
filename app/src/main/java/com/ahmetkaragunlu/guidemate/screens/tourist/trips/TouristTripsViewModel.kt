package com.ahmetkaragunlu.guidemate.screens.tourist.trips

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TouristTripsViewModel
    @Inject
    constructor() : ViewModel() {
        private val _allTrips =
            MutableStateFlow(
                listOf(
                    TripUiModel(
                        id = "1",
                        title = "Kapadokya Balon Turu",
                        date = "24 Mayıs 2026",
                        location = "Nevşehir, Ürgüp",
                        imageUrl = R.drawable.example,
                        participantCount = 8,
                        category = "Macera",
                        languagesFlag = "🇩🇪🇹🇷",
                        languagesText = "DE, TR",
                        price = 1500.0,
                        rating = 4.9,
                        reviewCount = 120,
                        isPast = false,
                    ),
                    TripUiModel(
                        id = "2",
                        title = "Efes Antik Kent",
                        date = "10 Haziran 2026",
                        location = "İzmir, Buca",
                        imageUrl = R.drawable.example,
                        participantCount = 5,
                        category = "Tarih",
                        languagesFlag = "🇬🇧🇹🇷",
                        languagesText = "EN, TR",
                        price = 800.0,
                        rating = 4.8,
                        reviewCount = 92,
                        isPast = false,
                    ),
                    TripUiModel(
                        id = "3",
                        title = "İstanbul Boğaz Turu",
                        date = "12 Ocak 2025",
                        location = "İstanbul",
                        imageUrl = R.drawable.example,
                        participantCount = 10,
                        category = "Tekne",
                        languagesFlag = "🇫🇷🇹🇷",
                        languagesText = "FR, TR",
                        price = 600.0,
                        rating = 4.7,
                        reviewCount = 210,
                        isPast = true,
                    ),
                    TripUiModel(
                        id = "4",
                        title = "Pamukkale Gezisi",
                        date = "05 Kasım 2024",
                        location = "Denizli",
                        imageUrl = R.drawable.example,
                        participantCount = 12,
                        category = "Doğa",
                        languagesFlag = "🇪🇸🇹🇷",
                        languagesText = "ES, TR",
                        price = 900.0,
                        rating = 4.9,
                        reviewCount = 140,
                        isPast = true,
                    ),
                ),
            )

        // 2. Tab State
        private val _selectedTab = MutableStateFlow(TripTab.UPCOMING)
        val selectedTab = _selectedTab.asStateFlow()

        val trips: StateFlow<List<TripUiModel>> =
            combine(_allTrips, _selectedTab) { allTrips, tab ->
                    when (tab) {
                        TripTab.UPCOMING -> allTrips.filter { !it.isPast }
                        TripTab.PAST -> allTrips.filter { it.isPast }
                    }
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = _allTrips.value.filter { !it.isPast },
                )

        fun changeTab(tab: TripTab) {
            _selectedTab.value = tab
        }

        fun onCancelTripClick(_tripId: String) {
            // TODO: Hook cancel endpoint when backend is ready.
            Unit
        }
    }
