package com.ahmetkaragunlu.guidemate.screens.guide.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.domain.UserRepository
import com.ahmetkaragunlu.guidemate.screens.guide.profile.model.GuideProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class GuideProfileViewModel @Inject constructor(
    userRepository: UserRepository
) : ViewModel() {

    val firstName: StateFlow<String?> = userRepository.userName
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val lastName: StateFlow<String?> = userRepository.userLastName
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val profileState: StateFlow<GuideProfileUiState> = flow {
        // MOCK DATA
        val mockData = GuideProfileUiState(
            profileImageUrl = null,
            title = "Profesyonel Tur Rehberi",
            guideLevel = "Süper Rehber",
            rating = 4.9,
            tourCount = 124
        )
        emit(mockData)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = GuideProfileUiState()
    )
}