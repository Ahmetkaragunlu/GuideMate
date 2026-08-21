package com.ahmetkaragunlu.guidemate.home.presentation.tourist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.auth.domain.repository.UserRepository
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import com.ahmetkaragunlu.guidemate.tour.presentation.category.TourCategoryCatalog
import com.ahmetkaragunlu.guidemate.tour.presentation.mapper.toPopularTourCardUiModel
import com.ahmetkaragunlu.guidemate.tour.data.mock.TourCatalogStore
import com.ahmetkaragunlu.guidemate.tour.data.mock.refreshAtSessionTransitions
import com.ahmetkaragunlu.guidemate.home.presentation.tourist.model.BestGuideUiModel
import com.ahmetkaragunlu.guidemate.home.presentation.tourist.model.TouristHomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TouristHomeViewModel @Inject constructor(
    userRepository: UserRepository,
    tourCatalogStore: TourCatalogStore,
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
        combine(
            _selectedCategory,
            tourCatalogStore.state.refreshAtSessionTransitions(),
        ) { selectedCategory, catalog ->
                val now = Instant.now()
                val popularTours =
                    catalog
                        .bookableTourItemsAt(now)
                        .filter { selectedCategory == null || it.tour.category == selectedCategory }
                        .map { it.toPopularTourCardUiModel() }
                TouristHomeUiState(
                    selectedCategory = selectedCategory,
                    popularTours = popularTours,
                    bestGuides = getDummyGuides(),
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue =
                    TouristHomeUiState(
                        selectedCategory = null,
                        popularTours =
                            tourCatalogStore.state.value
                                .bookableTourItemsAt(Instant.now())
                                .map {
                                    it.toPopularTourCardUiModel()
                                },
                        bestGuides = getDummyGuides(),
                    ),
            )

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
