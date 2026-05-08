package com.ahmetkaragunlu.guidemate.screens.guide.profile.account.about.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.screens.guide.profile.account.about.model.GuideAboutLanguageUi
import com.ahmetkaragunlu.guidemate.screens.guide.profile.account.about.model.GuideAboutUiState
import com.ahmetkaragunlu.guidemate.screens.guide.profile.account.about.model.toUpdatePayload
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class GuideAboutViewModel @Inject constructor() : ViewModel() {
    private val _uiState =
        MutableStateFlow(
            GuideAboutUiState(
                specialtyTitle = "",
                biography = "",
                spokenLanguages =
                    listOf(
                        GuideAboutLanguageUi(code = "tr", displayText = "🇹🇷 Türkçe"),
                        GuideAboutLanguageUi(code = "en", displayText = "🇬🇧 İngilizce"),
                    ),
            ),
        )
    val uiState: StateFlow<GuideAboutUiState> = _uiState.asStateFlow()

    fun onSpecialtyTitleChange(value: String) {
        _uiState.update { it.copy(specialtyTitle = value) }
    }

    fun onBiographyChange(value: String) {
        _uiState.update { it.copy(biography = value) }
    }

    fun onRemoveLanguageClick(code: String) {
        _uiState.update { current ->
            current.copy(spokenLanguages = current.spokenLanguages.filterNot { it.code == code })
        }
    }

    fun onAddLanguageClick() {
    }

    fun onSaveClick() {
        val payload = _uiState.value.toUpdatePayload()

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            payload.languageCodes.size
            _uiState.update { it.copy(isSaving = false) }
        }
    }
}
