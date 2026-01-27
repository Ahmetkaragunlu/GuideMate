package com.ahmetkaragunlu.guidemate.screens.tourist.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.domain.repository.AuthRepository
import com.ahmetkaragunlu.guidemate.screens.tourist.home.model.BestGuideUiModel
import com.ahmetkaragunlu.guidemate.screens.tourist.home.model.PopularToursCardUiModel
import com.ahmetkaragunlu.guidemate.screens.tourist.shared.TourCategoriesData
import com.ahmetkaragunlu.guidemate.screens.tourist.shared.TourCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TouristHomeViewModel @Inject constructor(
    authRepository: AuthRepository
) : ViewModel() {

    val userName: StateFlow<String?> = authRepository.getUserName
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val categories = TourCategoriesData.categories
    private val _selectedCategory = MutableStateFlow(TourCategory.ALL)
    val selectedCategory = _selectedCategory.asStateFlow()

    fun updateSelectedCategory(category: TourCategory) {
        _selectedCategory.value = category
    }

    val popularTours: StateFlow<List<PopularToursCardUiModel>> = selectedCategory.map { currentCategory ->
        val allTours = getDummyTours()

        if (currentCategory == TourCategory.ALL) {
            allTours
        } else {
            allTours.filter { it.category == currentCategory }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val bestGuides: StateFlow<List<BestGuideUiModel>> = flow {
        emit(getDummyGuides())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private fun getDummyTours(): List<PopularToursCardUiModel> {
        return listOf(
            PopularToursCardUiModel(
                id = "1",
                title = "Sultanahmet ve Gizli Sokaklar",
                imageUrl = R.drawable.aaa,
                rating = "4.9",
                reviewCount = "(120)",
                price = "750 ₺",
                languagesFlag = "🇹🇷 🇬🇧 🇩🇪",
                languagesText = "TR, EN, DE",
                guideName = "Ahmet K.",
                guideImageUrl = R.drawable.unnamed,
                category = TourCategory.CULTURE
            ),
            PopularToursCardUiModel(
                id = "2",
                title = "Kapadokya Balon Turu",
                imageUrl = R.drawable.aaa,
                rating = "5.0",
                reviewCount = "(85)",
                price = "2500 ₺",
                languagesFlag = "🇬🇧 🇫🇷",
                languagesText = "EN, FR",
                guideName = "Mehmet Y.",
                guideImageUrl = R.drawable.unnamed,
                category = TourCategory.ADVENTURE
            ),
            PopularToursCardUiModel(
                id = "3",
                title = "Efes Antik Kent Gezisi",
                imageUrl = R.drawable.aaa,
                rating = "4.8",
                reviewCount = "(210)",
                price = "600 ₺",
                languagesFlag = "🇹🇷 🇬🇧",
                languagesText = "TR, EN",
                guideName = "Ayşe Z.",
                guideImageUrl = R.drawable.unnamed,
                category = TourCategory.CULTURE
            ),
            PopularToursCardUiModel(
                id = "4",
                title = "Boğaz Tekne ve Yemek",
                imageUrl = R.drawable.aaa,
                rating = "4.7",
                reviewCount = "(300)",
                price = "1200 ₺",
                languagesFlag = "🇬🇧 🇸🇦",
                languagesText = "EN, AR",
                guideName = "Caner E.",
                guideImageUrl = R.drawable.unnamed,
                category = TourCategory.FOOD
            )
        )
    }

    private fun getDummyGuides(): List<BestGuideUiModel> {
        return listOf(
            BestGuideUiModel(
                id = "1",
                name = "Mehmet Yılmaz",
                imageUrl = R.drawable.unnamed,
                specialization = "Profesyonel Tarih Rehberi",
                rating = "5.0"
            ),
            BestGuideUiModel(
                id = "2",
                name = "Zeynep Kaya",
                imageUrl = R.drawable.unnamed,
                specialization = "Boğaz ve Doğa Uzmanı",
                rating = "4.9"
            ),
            BestGuideUiModel(
                id = "3",
                name = "Caner Erkin",
                imageUrl = R.drawable.unnamed,
                specialization = "Sanat Tarihçisi",
                rating = "4.8"
            ),
            BestGuideUiModel(
                id = "4",
                name = "Elif Demir",
                imageUrl = R.drawable.unnamed,
                specialization = "Gurme ve Yemek Rehberi",
                rating = "4.9"
            )
        )
    }
}