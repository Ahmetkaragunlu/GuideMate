package com.ahmetkaragunlu.guidemate.screens.guide.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.domain.repository.UserRepository
import com.ahmetkaragunlu.guidemate.screens.guide.profile.model.GuideProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class GuideProfileViewModel @Inject constructor(
    userRepository: UserRepository,
) : ViewModel() {

    val firstName: StateFlow<String?> =
        userRepository.userState
            .map { it.firstName }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null,
            )

    val lastName: StateFlow<String?> =
        userRepository.userState
            .map { it.lastName }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null,
            )

    val profileState: StateFlow<GuideProfileUiState> =
        flowOf(
            GuideProfileUiState(
                profileImageUrl = null,
                title = "Profesyonel Tur Rehberi",
                guideLevel = "Süper Rehber",
                rating = 4.9,
                tourCount = 124,
            )
        ).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = GuideProfileUiState(),
        )
}