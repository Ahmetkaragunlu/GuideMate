package com.ahmetkaragunlu.guidemate.screens.tourist.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.domain.repository.AuthRepository
import compose.icons.TablerIcons
import compose.icons.tablericons.BuildingChurch
import compose.icons.tablericons.Confetti
import compose.icons.tablericons.Flame
import compose.icons.tablericons.LayoutGrid
import compose.icons.tablericons.Palette
import compose.icons.tablericons.ToolsKitchen
import compose.icons.tablericons.Trees
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
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

    val categories = listOf(
        CategoryItem(0, R.string.category_all, TablerIcons.LayoutGrid),
        CategoryItem(0, R.string.category_culture, TablerIcons.BuildingChurch),
        CategoryItem(1, R.string.category_food, TablerIcons.ToolsKitchen),
        CategoryItem(2, R.string.category_nature, TablerIcons.Trees),
        CategoryItem(3, R.string.category_art, TablerIcons.Palette),
        CategoryItem(4, R.string.category_entertainment, TablerIcons.Confetti),
        CategoryItem(5, R.string.category_adventure, TablerIcons.Flame)
    )

    private val _selectedCategoryIndex = MutableStateFlow(0)
    val selectedCategoryIndex = _selectedCategoryIndex.asStateFlow()

    fun updateSelectedCategory(index: Int) {
        _selectedCategoryIndex.value = index
    }

    val popularTours: StateFlow<List<PopularToursCardUiModel>> = flow {
        emit(getDummyTours())
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
                guideImageUrl = R.drawable.unnamed
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
                guideImageUrl = R.drawable.unnamed
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
                guideImageUrl = R.drawable.unnamed
            ),
            PopularToursCardUiModel(
                id = "4",
                title = "Boğaz Tekne Turu",
                imageUrl = R.drawable.aaa,
                rating = "4.7",
                reviewCount = "(300)",
                price = "1200 ₺",
                languagesFlag = "🇬🇧 🇸🇦",
                languagesText = "EN, AR",
                guideName = "Caner E.",
                guideImageUrl = R.drawable.unnamed
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