package com.ahmetkaragunlu.guidemate.screens.tourist.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.domain.repository.UserRepository
import com.ahmetkaragunlu.guidemate.screens.tourist.finance.store.TouristFinanceStore
import com.ahmetkaragunlu.guidemate.screens.tourist.profile.model.ProfileUiState
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
        financeStore: TouristFinanceStore,
    ) : ViewModel() {
        val uiState: StateFlow<ProfileUiState> =
            combine(userRepository.userState, financeStore.state) { user, finance ->
                ProfileUiState(
                    fullName = listOfNotNull(user.firstName, user.lastName).joinToString(" "),
                    email = "ahmet@gmail.com",
                    balanceMinor = finance.balanceMinor,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue =
                    ProfileUiState(balanceMinor = financeStore.state.value.balanceMinor),
            )
    }
