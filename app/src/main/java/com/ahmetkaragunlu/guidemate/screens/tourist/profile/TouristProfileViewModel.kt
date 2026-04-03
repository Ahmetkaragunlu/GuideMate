package com.ahmetkaragunlu.guidemate.screens.tourist.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.domain.repository.UserRepository
import com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.savedcards.model.toBankAccount
import com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.savedcards.viewmodel.SavedCardsViewModel
import com.ahmetkaragunlu.guidemate.screens.tourist.profile.model.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
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
    private var isProfileLoaded = false

    fun loadProfileData() {
        if (isProfileLoaded) return
        isProfileLoaded = true

        viewModelScope.launch {
            combine(
                userRepository.userState,
                SavedCardsViewModel.savedCards,
            ) { user, savedCards ->
                user to savedCards
            }.collect { (user, savedCards) ->
                val defaultCard =
                    savedCards.firstOrNull { it.isDefault } ?: savedCards.firstOrNull()
                _uiState.update {
                    it.copy(
                        fullName = listOfNotNull(user.firstName, user.lastName).joinToString(" "),
                        email = "ahmet@gmail.com",
                        selectedCardId = defaultCard?.cardId,
                        selectedBankAccount = defaultCard?.toBankAccount(),
                    )
                }
            }
        }
    }

    fun onDepositAmountChange(value: String) {
        if (value.isEmpty() || value.all(Char::isDigit)) {
            _uiState.update { it.copy(depositAmount = value) }
        }
    }

    fun onDepositPresetSelected(amount: Int) {
        _uiState.update { it.copy(depositAmount = amount.toString()) }
    }

    fun onAddMoneyConfirm() {
        val amount = _uiState.value.depositAmount.toDoubleOrNull() ?: return
        _uiState.update {
            it.copy(
                balanceAmount = it.balanceAmount + amount,
                depositAmount = "",
            )
        }
    }

    fun onChangeBankClick() {
        val savedCards = SavedCardsViewModel.getSavedCards()
        if (savedCards.isEmpty()) return

        val currentIndex =
            savedCards.indexOfFirst { it.cardId == _uiState.value.selectedCardId }.let { index ->
                if (index >= 0) index else 0
            }
        val nextCard = savedCards[(currentIndex + 1) % savedCards.size]

        _uiState.update {
            it.copy(
                selectedCardId = nextCard.cardId,
                selectedBankAccount = nextCard.toBankAccount(),
            )
        }
    }

    fun syncSelectedCardWithDefault() {
        val savedCards = SavedCardsViewModel.getSavedCards()
        val defaultCard = savedCards.firstOrNull { it.isDefault } ?: savedCards.firstOrNull()

        _uiState.update {
            it.copy(
                selectedCardId = defaultCard?.cardId,
                selectedBankAccount = defaultCard?.toBankAccount(),
            )
        }
    }
}
