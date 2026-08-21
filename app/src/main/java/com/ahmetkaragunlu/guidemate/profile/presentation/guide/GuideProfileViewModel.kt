package com.ahmetkaragunlu.guidemate.profile.presentation.guide

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.profile.presentation.guide.model.GuideProfileUiState
import com.ahmetkaragunlu.guidemate.profile.data.mock.shared.GuideProfileSharedStore
import com.ahmetkaragunlu.guidemate.profile.data.mock.shared.GuideProfileStateProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class GuideProfileViewModel
    @Inject
    constructor(
        stateProvider: GuideProfileStateProvider,
        private val sharedStore: GuideProfileSharedStore,
    ) : ViewModel() {
        val profileState: StateFlow<GuideProfileUiState> =
            stateProvider
                .profileState()
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = stateProvider.currentProfileState(),
                )

        fun onProfileImageSelected(uri: String) {
            sharedStore.updateSelectedProfileImage(uri)
        }
    }
