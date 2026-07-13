package com.ahmetkaragunlu.guidemate.screens.guide.profile.account.about.viewmodel

import androidx.lifecycle.ViewModel
import com.ahmetkaragunlu.guidemate.screens.guide.profile.account.about.model.GuideAboutUiState
import com.ahmetkaragunlu.guidemate.screens.guide.profile.account.about.model.toUpdatePayload
import com.ahmetkaragunlu.guidemate.screens.guide.profile.shared.GuideProfileSharedStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class GuideAboutViewModel
    @Inject
    constructor(
        private val sharedStore: GuideProfileSharedStore,
    ) : ViewModel() {
        private val _uiState =
            MutableStateFlow(
                sharedStore.state.value.let { profile ->
                    GuideAboutUiState(
                        specialtyTitle = profile.title,
                        biography = profile.biography,
                        spokenLanguages = profile.spokenLanguages,
                    )
                },
            )
        val uiState: StateFlow<GuideAboutUiState> = _uiState.asStateFlow()

        fun onSpecialtyTitleChange(value: String) {
            if (value.length <= GuideAboutUiState.MAX_SPECIALTY_TITLE_LENGTH) {
                _uiState.update { it.copy(specialtyTitle = value) }
            }
        }

        fun onBiographyChange(value: String) {
            if (value.length <= GuideAboutUiState.MAX_BIOGRAPHY_LENGTH) {
                _uiState.update { it.copy(biography = value) }
            }
        }

        fun onRemoveLanguageClick(code: String) {
            _uiState.update { current ->
                current.copy(spokenLanguages = current.spokenLanguages.filterNot { it.code == code })
            }
        }

        fun onAddLanguageClick() {
        }

        fun onSaveClick(): Boolean {
            val form = _uiState.value
            if (!form.isFormValid) {
                _uiState.update { it.copy(showValidationErrors = true) }
                return false
            }

            val payload = form.toUpdatePayload()
            sharedStore.updateAbout(
                specialtyTitle = payload.specialtyTitle,
                biography = payload.biography,
                spokenLanguages = form.spokenLanguages,
            )
            _uiState.update {
                it.copy(
                    specialtyTitle = payload.specialtyTitle,
                    biography = payload.biography,
                    showValidationErrors = false,
                )
            }
            return true
        }
    }
