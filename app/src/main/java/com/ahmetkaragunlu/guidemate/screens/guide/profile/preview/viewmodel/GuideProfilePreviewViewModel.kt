package com.ahmetkaragunlu.guidemate.screens.guide.profile.preview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.guide.profile.model.GuideProfileUiState
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
            .map(GuideProfileUiState::toPreviewUiState)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = stateProvider.currentProfileState().toPreviewUiState(),
            )
}

private fun GuideProfileUiState.toPreviewUiState(): GuideProfilePreviewUiState =
    GuideProfilePreviewUiState(
        profileImageResId = profileImageResId ?: R.drawable.unnamed,
        profileImageUrl = displayProfileImageUrl,
        displayName = displayName,
        title = title,
        guideLevel = guideLevel,
        rating = rating,
        tourCount = tourCount,
        biography = biography,
        spokenLanguages = spokenLanguages,
        popularTours = popularTours,
    )
