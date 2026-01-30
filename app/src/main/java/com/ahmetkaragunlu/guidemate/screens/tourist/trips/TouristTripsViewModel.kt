package com.ahmetkaragunlu.guidemate.screens.tourist.trips


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.R

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TouristTripsViewModel @Inject constructor() : ViewModel() {

    // 1. Mock Data
    private val allTrips = listOf(
        TripUiModel("1", "Kapadokya Balon Turu", "24 Mayıs 2026", "Nevşehir, Ürgüp", R.drawable.aaa, false),
        TripUiModel("2", "Efes Antik Kent", "10 Haziran 2026", "İzmir, Buca", R.drawable.aaa, false),
        TripUiModel("3", "İstanbul Boğaz Turu", "12 Ocak 2025", "İstanbul", R.drawable.aaa, true),
        TripUiModel("4", "Pamukkale Gezisi", "05 Kasım 2024", "Denizli", R.drawable.aaa, true)
    )

    // 2. Tab State
    private val _selectedTab = MutableStateFlow(TripTab.UPCOMING)
    val selectedTab = _selectedTab.asStateFlow()

    val trips: StateFlow<List<TripUiModel>> = _selectedTab
        .map { tab ->
            when (tab) {
                TripTab.UPCOMING -> allTrips.filter { !it.isPast }
                TripTab.PAST -> allTrips.filter { it.isPast }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = allTrips.filter { !it.isPast }
        )

    fun changeTab(tab: TripTab) {
        _selectedTab.value = tab
    }
}