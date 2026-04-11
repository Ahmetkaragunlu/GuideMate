package com.ahmetkaragunlu.guidemate.screens.tourist.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.domain.repository.UserRepository
import com.ahmetkaragunlu.guidemate.screens.common.profile.account.savedcards.findDefaultSavedCard
import com.ahmetkaragunlu.guidemate.screens.common.profile.account.savedcards.findNextSavedCard
import com.ahmetkaragunlu.guidemate.screens.common.profile.account.savedcards.model.toMoneyActionMethodUi
import com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.savedcards.viewmodel.TouristSavedCardsViewModel
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
    private val userRepository: UserRepository,
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
                TouristSavedCardsViewModel.savedCards,
            ) { user, savedCards ->
                user to savedCards
            }.collect { (user, savedCards) ->
                val defaultCard = findDefaultSavedCard(savedCards)
                _uiState.update {
                    it.copy(
                        fullName = listOfNotNull(user.firstName, user.lastName).joinToString(" "),
                        email = "ahmet@gmail.com",
                        selectedCardId = defaultCard?.cardId,
                        selectedMethod = defaultCard?.toMoneyActionMethodUi(),
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
        val nextCard =
            findNextSavedCard(
                cards = TouristSavedCardsViewModel.getSavedCards(),
                currentCardId = _uiState.value.selectedCardId,
            ) ?: return

        _uiState.update {
            it.copy(
                selectedCardId = nextCard.cardId,
                selectedMethod = nextCard.toMoneyActionMethodUi(),
            )
        }
    }

    fun syncSelectedCardWithDefault() {
        val defaultCard = findDefaultSavedCard(TouristSavedCardsViewModel.getSavedCards())

        _uiState.update {
            it.copy(
                selectedCardId = defaultCard?.cardId,
                selectedMethod = defaultCard?.toMoneyActionMethodUi(),
            )
        }
    }
}
