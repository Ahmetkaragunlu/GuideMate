package com.ahmetkaragunlu.guidemate.profile.presentation.tourist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.auth.domain.repository.UserRepository
import com.ahmetkaragunlu.guidemate.wallet.data.mock.tourist.TouristWalletStore
import com.ahmetkaragunlu.guidemate.profile.presentation.tourist.model.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class TouristProfileViewModel
    @Inject
    constructor(
        userRepository: UserRepository,
        walletStore: TouristWalletStore,
    ) : ViewModel() {
        val uiState: StateFlow<ProfileUiState> =
            combine(userRepository.userState, walletStore.state) { user, wallet ->
                ProfileUiState(
                    fullName = listOfNotNull(user.firstName, user.lastName).joinToString(" "),
                    email = user.email.orEmpty(),
                    balanceMinor = wallet.balanceMinor,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue =
                    ProfileUiState(balanceMinor = walletStore.state.value.balanceMinor),
            )
    }
