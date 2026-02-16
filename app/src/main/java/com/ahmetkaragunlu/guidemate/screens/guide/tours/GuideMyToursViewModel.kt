package com.ahmetkaragunlu.guidemate.screens.guide.tours

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.GuideTourTab
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.GuideTourUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class GuideMyToursViewModel @Inject constructor() : ViewModel() {

    // Mock Data
    private val allTours = listOf(
        GuideTourUiModel(
            id = "1",
            title = "Kapadokya Balon Turu",
            date = "24 Mayıs 2026",
            location = "Nevşehir, Ürgüp",
            imageUrl = R.drawable.example,
            participantCount = 8,
            price = 1500.0,
            languagesFlag = "🇩🇪🇹🇷",
            languagesText = "DE, TR",
            category = "Macera",
            rating = 4.9,
            reviewCount = 120,
            isActive = true,
            isLive = true
        ),
        GuideTourUiModel(
            id = "2",
            title = "Efes Antik Kent",
            date = "10 Haziran 2026",
            location = "İzmir, Selçuk",
            imageUrl = R.drawable.example,
            participantCount = 5,
            price = 800.0,
            languagesFlag = "🇬🇧🇹🇷",
            languagesText = "EN, TR",
            category = "Tarih",
            rating = null,
            reviewCount = null,
            isActive = true,
            isLive = false
        ),
        GuideTourUiModel(
            id = "3",
            title = "İstanbul Boğaz Turu",
            date = "12 Ocak 2025",
            location = "İstanbul",
            imageUrl = R.drawable.example,
            participantCount = 10,
            price = 600.0,
            languagesFlag = "🇫🇷🇹🇷",
            languagesText = "FR, TR",
            category = "Tekne",
            rating = 4.8,
            reviewCount = 210,
            isActive = false,
            earnings = 6000.0
        ),
        GuideTourUiModel(
            id = "4",
            title = "Pamukkale Gezisi",
            date = "05 Kasım 2024",
            location = "Denizli",
            imageUrl = R.drawable.example,
            participantCount = 12,
            price = 900.0,
            languagesFlag = "🇪🇸🇹🇷",
            languagesText = "ES, TR",
            category = "Doğa",
            rating = 5.0,
            reviewCount = 45,
            isActive = false,
            earnings = 10800.0
        )
    )

    private val _selectedTab = MutableStateFlow(GuideTourTab.ACTIVE)
    val selectedTab = _selectedTab.asStateFlow()

    val tours: StateFlow<List<GuideTourUiModel>> = _selectedTab
        .map { tab ->
            when (tab) {
                GuideTourTab.ACTIVE -> allTours.filter { it.isActive }
                GuideTourTab.PAST -> allTours.filter { !it.isActive }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = allTours.filter { it.isActive }
        )

    fun changeTab(tab: GuideTourTab) {
        _selectedTab.value = tab
    }
}