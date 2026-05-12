package com.ahmetkaragunlu.guidemate.screens.guide.profile.shared

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.common.tours.model.PopularTourCardUiModel
import com.ahmetkaragunlu.guidemate.screens.guide.profile.model.GuideProfileSharedState
import com.ahmetkaragunlu.guidemate.screens.guide.profile.model.GuideSpokenLanguageUi
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Singleton
class GuideProfileSharedStore
    @Inject
    constructor() {
        private val _state =
            MutableStateFlow(
                GuideProfileSharedState(
                    profileImageResId = R.drawable.unnamed,
                    profileImageUrl = null,
                    title = "Sanat Tarihçisi",
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

        val state: StateFlow<GuideProfileSharedState> = _state.asStateFlow()

        fun updateAbout(
            specialtyTitle: String,
            biography: String,
            spokenLanguages: List<GuideSpokenLanguageUi>,
        ) {
            _state.update { current ->
                current.copy(
                    title = specialtyTitle,
                    biography = biography,
                    spokenLanguages = spokenLanguages,
                )
            }
        }
    }
