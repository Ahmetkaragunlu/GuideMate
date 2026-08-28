package com.ahmetkaragunlu.guidemate.profile.presentation.guide.account.about

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.common.location.locale.LocaleSelectionCatalog
import com.ahmetkaragunlu.guidemate.common.location.model.LanguageOption
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.error.toMessage
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuideProfile
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuideProfileUpdate
import com.ahmetkaragunlu.guidemate.profile.domain.repository.GuideProfileRepository
import com.ahmetkaragunlu.guidemate.profile.presentation.guide.account.about.model.GuideAboutUiState
import com.ahmetkaragunlu.guidemate.profile.presentation.model.GuideSpokenLanguageUi
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class GuideAboutViewModel
    @Inject
    constructor(
        private val profileRepository: GuideProfileRepository,
        private val resourceProvider: ResourceProvider,
    ) : ViewModel() {
        private val _uiState =
            MutableStateFlow(
                profileRepository.cachedOwnProfile?.toAboutUiState()
                    ?: GuideAboutUiState(),
            )
        val uiState: StateFlow<GuideAboutUiState> = _uiState.asStateFlow()

        private var refreshJob: Job? = null
        private var saveJob: Job? = null
        private var hasUserEdited = false

        init {
            refreshProfile()
        }

        fun refreshProfile() {
            if (refreshJob?.isActive == true) return
            refreshJob =
                viewModelScope.launch {
                    val hasCachedProfile = profileRepository.cachedOwnProfile != null
                    if (!hasCachedProfile) {
                        _uiState.update { it.copy(loadState = ContentLoadState.LOADING) }
                    }
                    when (val result = profileRepository.refreshOwnProfile()) {
                        is DataResult.Success -> {
                            if (!hasUserEdited) {
                                _uiState.value = result.data.toAboutUiState()
                            } else {
                                _uiState.update { it.copy(loadState = ContentLoadState.CONTENT) }
                            }
                        }
                        is DataResult.Error -> {
                            if (hasCachedProfile) {
                                _uiState.update {
                                    it.copy(errorMessage = result.error.toMessage(resourceProvider))
                                }
                            } else {
                                _uiState.update { it.copy(loadState = ContentLoadState.ERROR) }
                            }
                        }
                    }
                }
        }

        fun onSpecialtyTitleChange(value: String) {
            if (value.length <= GuideAboutUiState.MAX_SPECIALTY_TITLE_LENGTH) {
                hasUserEdited = true
                _uiState.update { it.copy(specialtyTitle = value) }
            }
        }

        fun onBiographyChange(value: String) {
            if (value.length <= GuideAboutUiState.MAX_BIOGRAPHY_LENGTH) {
                hasUserEdited = true
                _uiState.update { it.copy(biography = value) }
            }
        }

        fun onRemoveLanguageClick(code: String) {
            hasUserEdited = true
            _uiState.update { current ->
                current.copy(spokenLanguages = current.spokenLanguages.filterNot { it.code == code })
            }
        }

        fun onLanguagesSelected(languages: List<LanguageOption>) {
            hasUserEdited = true
            _uiState.update { current ->
                current.copy(
                    spokenLanguages =
                        languages.map { language ->
                            GuideSpokenLanguageUi(
                                code = language.code,
                                displayText = language.chipLabel,
                            )
                        },
                )
            }
        }

        fun onSaveClick() {
            if (saveJob?.isActive == true) return
            val form = _uiState.value
            if (!form.isFormValid) {
                _uiState.update { it.copy(showValidationErrors = true) }
                return
            }

            saveJob =
                viewModelScope.launch {
                    _uiState.update { it.copy(isSaving = true, errorMessage = null) }
                    val result =
                        profileRepository.updateOwnProfile(
                            GuideProfileUpdate(
                                specialtyTitle = form.specialtyTitle.trim(),
                                biography = form.biography.trim(),
                                languageCodes = form.spokenLanguages.map { it.code },
                            ),
                        )
                    when (result) {
                        is DataResult.Success -> {
                            hasUserEdited = false
                            _uiState.value =
                                result.data.toAboutUiState().copy(saveCompleted = true)
                        }
                        is DataResult.Error -> {
                            _uiState.update {
                                it.copy(
                                    isSaving = false,
                                    errorMessage = result.error.toMessage(resourceProvider),
                                )
                            }
                        }
                    }
                }
        }

        fun onErrorShown() {
            _uiState.update { it.copy(errorMessage = null) }
        }

        fun onSaveHandled() {
            _uiState.update { it.copy(saveCompleted = false) }
        }
    }

private fun GuideProfile.toAboutUiState(): GuideAboutUiState {
    val locale = Locale.getDefault()
    return GuideAboutUiState(
        specialtyTitle = specialtyTitle,
        biography = biography,
        spokenLanguages =
            languageCodes.map { code ->
                GuideSpokenLanguageUi(
                    code = code,
                    displayText =
                        LocaleSelectionCatalog.language(code, locale)?.chipLabel
                            ?: code.uppercase(locale),
                )
            },
        loadState = ContentLoadState.CONTENT,
    )
}
