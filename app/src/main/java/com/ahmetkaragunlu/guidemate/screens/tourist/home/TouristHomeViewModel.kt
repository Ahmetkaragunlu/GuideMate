package com.ahmetkaragunlu.guidemate.screens.tourist.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.domain.repository.UserRepository
import com.ahmetkaragunlu.guidemate.screens.common.tours.category.TourCategory
import com.ahmetkaragunlu.guidemate.screens.common.tours.category.TourCategoryCatalog
import com.ahmetkaragunlu.guidemate.screens.common.tours.model.PopularTourCardUiModel
import com.ahmetkaragunlu.guidemate.screens.tourist.home.model.BestGuideUiModel
import com.ahmetkaragunlu.guidemate.screens.tourist.home.model.TouristHomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TouristHomeViewModel @Inject constructor(
    userRepository: UserRepository,
) : ViewModel() {

    val userName: StateFlow<String?> =
        userRepository.userState
            .map { it.firstName }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null,
            )

    val categories = TourCategoryCatalog.filterOptions

    private val _selectedCategory = MutableStateFlow<TourCategory?>(null)

    fun updateSelectedCategory(category: TourCategory?) {
        _selectedCategory.value = category
    }

    val uiState: StateFlow<TouristHomeUiState> =
        _selectedCategory
            .map { selectedCategory ->
                TouristHomeUiState(
                    selectedCategory = selectedCategory,
                    popularTours = getDummyTours(selectedCategory),
                    bestGuides = getDummyGuides(),
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue =
                    TouristHomeUiState(
                        selectedCategory = null,
                        popularTours = getDummyTours(category = null),
                        bestGuides = getDummyGuides(),
                    ),
            )

    private data class CategorizedTour(
        val category: TourCategory,
        val tour: PopularTourCardUiModel,
    )

    private fun getDummyTours(category: TourCategory?): List<PopularTourCardUiModel> {
        val allTours =
            listOf(
                CategorizedTour(
                    category = TourCategory.CULTURE,
                    tour =
                        PopularTourCardUiModel(
                            id = "1",
                            title = "Sultanahmet ve Gizli Sokaklar",
                            imageResId = R.drawable.example,
                            rating = "4.9",
                            reviewCount = "(120)",
                            price = 750.0,
                            languagesFlag = "🇹🇷 🇬🇧 🇩🇪",
                            languagesText = "TR, EN, DE",
                            guideName = "Ahmet K.",
                            guideImageResId = R.drawable.unnamed,
                        ),
                ),
                CategorizedTour(
                    category = TourCategory.ADVENTURE,
                    tour =
                        PopularTourCardUiModel(
                            id = "2",
                            title = "Kapadokya Balon Turu",
                            imageResId = R.drawable.example,
                            rating = "5.0",
                            reviewCount = "(85)",
                            price = 2500.0,
                            languagesFlag = "🇬🇧 🇫🇷",
                            languagesText = "EN, FR",
                            guideName = "Mehmet Y.",
                            guideImageResId = R.drawable.unnamed,
                        ),
                ),
                CategorizedTour(
                    category = TourCategory.CULTURE,
                    tour =
                        PopularTourCardUiModel(
                            id = "3",
                            title = "Efes Antik Kent Gezisi",
                            imageResId = R.drawable.example,
                            rating = "4.8",
                            reviewCount = "(210)",
                            price = 600.0,
                            languagesFlag = "🇹🇷 🇬🇧",
                            languagesText = "TR, EN",
                            guideName = "Ayşe Z.",
                            guideImageResId = R.drawable.unnamed,
                        ),
                ),
                CategorizedTour(
                    category = TourCategory.FOOD,
                    tour =
                        PopularTourCardUiModel(
                            id = "4",
                            title = "Boğaz Tekne ve Yemek",
                            imageResId = R.drawable.example,
                            rating = "4.7",
                            reviewCount = "(300)",
                            price = 1200.0,
                            languagesFlag = "🇬🇧 🇸🇦",
                            languagesText = "EN, AR",
                            guideName = "Caner E.",
                            guideImageResId = R.drawable.unnamed,
                        ),
                ),
            )

        return if (category == null) {
            allTours.map { it.tour }
        } else {
            allTours.filter { it.category == category }.map { it.tour }
        }
    }

    private fun getDummyGuides(): List<BestGuideUiModel> =
        listOf(
            BestGuideUiModel(
                id = "1",
                name = "Mehmet Yılmaz",
                imageResId = R.drawable.unnamed,
                specialization = "Profesyonel Tarih Rehberi",
                rating = "5.0",
            ),
            BestGuideUiModel(
                id = "2",
                name = "Zeynep Kaya",
                imageResId = R.drawable.unnamed,
                specialization = "Boğaz ve Doğa Uzmanı",
                rating = "4.9",
            ),
            BestGuideUiModel(
                id = "3",
                name = "Caner Erkin",
                imageResId = R.drawable.unnamed,
                specialization = "Sanat Tarihçisi",
                rating = "4.8",
            ),
            BestGuideUiModel(
                id = "4",
                name = "Elif Demir",
                imageResId = R.drawable.unnamed,
                specialization = "Gurme ve Yemek Rehberi",
                rating = "4.9",
            ),
        )
}
