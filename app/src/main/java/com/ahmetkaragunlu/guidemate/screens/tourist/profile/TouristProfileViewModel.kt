package com.ahmetkaragunlu.guidemate.screens.tourist.profile


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.data.local.TokenManager
import com.ahmetkaragunlu.guidemate.screens.tourist.profile.model.ProfileMenuType
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
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()


    fun loadProfileData() {
        viewModelScope.launch {
            val savedName = tokenManager.getUserName() ?: ""
            _uiState.update {
                it.copy(
                    fullName = savedName,
                    email = "ahmet@gmail.com",
                    balance = "1500,00 $"
                )
            }
        }
    }

    fun onMenuItemClicked(type: ProfileMenuType) {
        when(type) {
            ProfileMenuType.CARDS -> {  }
            ProfileMenuType.PASSWORD -> {  }
            ProfileMenuType.NOTIFICATIONS -> { /* ... */ }
            ProfileMenuType.LANGUAGE -> { /* ... */ }
            ProfileMenuType.LEGAL -> { /* ... */ }
            ProfileMenuType.HELP -> { /* ... */ }
        }
    }
}