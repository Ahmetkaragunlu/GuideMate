package com.ahmetkaragunlu.guidemate.screens.tourist.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.domain.repository.UserRepository
import com.ahmetkaragunlu.guidemate.screens.tourist.profile.model.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TouristProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun loadProfileData() {
        viewModelScope.launch {
            userRepository.userState.collect { user ->
                _uiState.update {
                    it.copy(
                        fullName = listOfNotNull(user.firstName, user.lastName).joinToString(" "),
                        email = "ahmet@gmail.com",
                        balance = "1500,00 $"
                    )
                }
            }
        }
    }
}