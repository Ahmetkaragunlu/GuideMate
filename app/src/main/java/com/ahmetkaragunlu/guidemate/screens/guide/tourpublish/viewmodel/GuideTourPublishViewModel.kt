package com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.viewmodel

import androidx.lifecycle.ViewModel
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.model.GuideTourLanguageUi
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.model.GuideTourPublishUiState
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.model.toCreatePayload
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class GuideTourPublishViewModel
    @Inject
    constructor() : ViewModel() {
        private val _uiState = MutableStateFlow(initialUiState())
        val uiState: StateFlow<GuideTourPublishUiState> = _uiState.asStateFlow()

        fun resetDraft() {
            _uiState.value = initialUiState()
        }

        fun onRemoveLanguageClick(code: String) {
            _uiState.update { current ->
                current.copy(spokenLanguages = current.spokenLanguages.filterNot { it.code == code })
            }
        }

        fun onPriceChange(input: String) {
            if (input.all(Char::isDigit)) {
                _uiState.update { it.copy(price = input) }
            }
        }

        fun onTourNameChange(value: String) {
            _uiState.update { it.copy(tourName = value) }
        }

        fun onTourDescriptionChange(value: String) {
            _uiState.update { it.copy(tourDescription = value) }
        }

        fun onMeetingPointChange(value: String) {
            _uiState.update { it.copy(meetingPoint = value) }
        }

        fun onPublishClick() {
            val payload = _uiState.value.toCreatePayload()
            payload.languageCodes.size
        }


        // Mock data (MVP)
        private fun initialUiState(): GuideTourPublishUiState =
            GuideTourPublishUiState(
                country = "Türkiye",
                city = "İstanbul",
                tourDate = "24 Mayıs 2026",
                category = "Tarih ve Kültür",
                spokenLanguages =
                    listOf(
                        GuideTourLanguageUi(
                            code = "tr",
                            flagEmoji = "🇹🇷",
                            displayName = "Türkçe",
                            previewCode = "TR",
                        ),
                        GuideTourLanguageUi(
                            code = "en",
                            flagEmoji = "🇬🇧",
                            displayName = "İngilizce",
                            previewCode = "ENG",
                        ),
                    ),
                previewImageResId = R.drawable.example,
            )
    }
