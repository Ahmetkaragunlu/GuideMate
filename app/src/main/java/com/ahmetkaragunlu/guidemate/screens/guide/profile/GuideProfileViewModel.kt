package com.ahmetkaragunlu.guidemate.screens.guide.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.screens.guide.profile.model.GuideProfileUiState
import com.ahmetkaragunlu.guidemate.screens.guide.profile.shared.GuideProfileStateProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class GuideProfileViewModel @Inject constructor(
    stateProvider: GuideProfileStateProvider,
) : ViewModel() {

    val profileState: StateFlow<GuideProfileUiState> =
        stateProvider
            .profileState()
            .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = GuideProfileUiState(),
        )
}
