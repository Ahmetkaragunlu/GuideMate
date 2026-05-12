package com.ahmetkaragunlu.guidemate.screens.guide.profile.preview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.common.tours.model.PopularTourCardUiModel
import com.ahmetkaragunlu.guidemate.screens.guide.profile.guidelevel.model.GuideLevelTier
import com.ahmetkaragunlu.guidemate.screens.guide.profile.model.GuideSpokenLanguageUi
import com.ahmetkaragunlu.guidemate.screens.guide.profile.preview.model.GuideProfilePreviewUiState
import com.ahmetkaragunlu.guidemate.screens.guide.profile.shared.GuideProfileStateProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class GuideProfilePreviewViewModel
@Inject constructor(stateProvider: GuideProfileStateProvider) : ViewModel() {
    val uiState: StateFlow<GuideProfilePreviewUiState> =
        stateProvider
            .profileState()
            .map { profile ->
                GuideProfilePreviewUiState(
                    profileImageResId = profile.profileImageResId ?: R.drawable.unnamed,
                    displayName = profile.displayName,
                    title = profile.title,
                    guideLevel = profile.guideLevel,
                    rating = profile.rating,
                    tourCount = profile.tourCount,
                    biography = profile.biography,
                    spokenLanguages = profile.spokenLanguages,
                    popularTours = profile.popularTours,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue =
                    GuideProfilePreviewUiState(
                        profileImageResId = R.drawable.unnamed,
                        displayName = "Ahmet Karagünlü",
                        title = "Sanat Tarihçisi",
                        guideLevel = GuideLevelTier.SUPER,
                        rating = 4.9,
                        tourCount = 124,
                        biography = "Merhaba, ben Ahmet. Yıllardır İstanbul’un tarihi semtlerinde yürüyüş turları düzenliyorum. Tur boyunca sadece yapıları değil, o yapıların ardındaki insan hikayelerini, kültürel dönüşümleri ve günlük hayatı da paylaşmayı seviyorum. Amacım, şehri turist gibi değil, yerel biri gibi hissettirerek gezdirmek.",
                        spokenLanguages =
                            listOf(
                                GuideSpokenLanguageUi(code = "tr", displayText = "🇹🇷 Türkçe"),
                                GuideSpokenLanguageUi(code = "en", displayText = "🇬🇧 İngilizce"),
                                GuideSpokenLanguageUi(code = "de", displayText = "🇩🇪 Almanca"),
                                GuideSpokenLanguageUi(code = "fr", displayText = "🇫🇷 Fransızca"),
                                GuideSpokenLanguageUi(code = "it", displayText = "🇮🇹 İtalyanca"),
                            ),
                        popularTours =
                            listOf(
                                PopularTourCardUiModel(
                                    id = "preview-tour-1",
                                    title = "Sultanahmet ve Gizli Sokaklar",
                                    imageUrl = R.drawable.example,
                                    rating = "4.9",
                                    reviewCount = "(120)",
                                    price = 750.0,
                                    languagesFlag = "🇹🇷 🇬🇧 🇩🇪",
                                    languagesText = "TR, EN, DE",
                                    guideName = "Ahmet K.",
                                    guideImageUrl = R.drawable.unnamed,
                                ),
                                PopularTourCardUiModel(
                                    id = "preview-tour-2",
                                    title = "Kapadokya Balon Turu",
                                    imageUrl = R.drawable.example,
                                    rating = "5.0",
                                    reviewCount = "(85)",
                                    price = 2500.0,
                                    languagesFlag = "🇬🇧 🇫🇷",
                                    languagesText = "EN, FR",
                                    guideName = "Ahmet K.",
                                    guideImageUrl = R.drawable.unnamed,
                                ),
                            ),
                    ),
            )
}
