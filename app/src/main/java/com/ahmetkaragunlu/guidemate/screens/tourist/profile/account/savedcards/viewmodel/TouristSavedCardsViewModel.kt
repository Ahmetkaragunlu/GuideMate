package com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.savedcards.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.screens.common.profile.account.savedcards.appendSavedCard
import com.ahmetkaragunlu.guidemate.screens.common.profile.account.savedcards.deleteSavedCard
import com.ahmetkaragunlu.guidemate.screens.common.profile.account.savedcards.makeDefaultSavedCard
import com.ahmetkaragunlu.guidemate.screens.common.profile.account.savedcards.model.AddSavedCardFormState
import com.ahmetkaragunlu.guidemate.screens.common.profile.account.savedcards.model.SavedCardUi
import com.ahmetkaragunlu.guidemate.screens.common.profile.account.savedcards.model.SavedCardsUiState
import com.ahmetkaragunlu.guidemate.screens.common.profile.account.savedcards.sanitizeCardHolderName
import com.ahmetkaragunlu.guidemate.screens.common.profile.account.savedcards.sanitizeCardNumber
import com.ahmetkaragunlu.guidemate.screens.common.profile.account.savedcards.sanitizeExpiryMonth
import com.ahmetkaragunlu.guidemate.screens.common.profile.account.savedcards.sanitizeExpiryYear
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class TouristSavedCardsViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(SavedCardsUiState(savedCards = getSavedCards()))
    val uiState: StateFlow<SavedCardsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            savedCards.collectLatest { cards ->
                _uiState.update { it.copy(savedCards = cards) }
            }
        }
    }

    fun onShowDeleteDialog(cardId: String) {
        _uiState.update { it.copy(showDeleteDialogFor = cardId) }
    }

    fun onDismissDeleteDialog() {
        _uiState.update { it.copy(showDeleteDialogFor = null) }
    }

    fun onShowMakeDefaultDialog(cardId: String) {
        _uiState.update { it.copy(showMakeDefaultDialogFor = cardId) }
    }

    fun onDismissMakeDefaultDialog() {
        _uiState.update { it.copy(showMakeDefaultDialogFor = null) }
    }

    fun onConfirmDeleteCard() {
        val cardId = _uiState.value.showDeleteDialogFor ?: return
        _savedCards.value = deleteSavedCard(_savedCards.value, cardId)
        _uiState.update { it.copy(showDeleteDialogFor = null) }
    }

    fun onConfirmMakeDefaultCard() {
        val cardId = _uiState.value.showMakeDefaultDialogFor ?: return
        _savedCards.value = makeDefaultSavedCard(_savedCards.value, cardId)
        _uiState.update { it.copy(showMakeDefaultDialogFor = null) }
    }

    fun onShowAddCardSheet() {
        _uiState.update { it.copy(isAddCardBottomSheetVisible = true) }
    }

    fun onDismissAddCardSheet() {
        _uiState.update {
            it.copy(
                isAddCardBottomSheetVisible = false,
                addCardForm = AddSavedCardFormState(),
            )
        }
    }

    fun onCardNumberChange(value: String) {
        _uiState.update {
            it.copy(
                addCardForm = it.addCardForm.copy(cardNumber = sanitizeCardNumber(value)),
            )
        }
    }

    fun onCardHolderNameChange(value: String) {
        _uiState.update {
            it.copy(
                addCardForm = it.addCardForm.copy(cardHolderName = sanitizeCardHolderName(value)),
            )
        }
    }

    fun onExpiryMonthChange(value: String) {
        _uiState.update {
            it.copy(
                addCardForm = it.addCardForm.copy(expiryMonth = sanitizeExpiryMonth(value)),
            )
        }
    }

    fun onExpiryYearChange(value: String) {
        _uiState.update {
            it.copy(
                addCardForm = it.addCardForm.copy(expiryYear = sanitizeExpiryYear(value)),
            )
        }
    }

    fun onConfirmAddCard() {
        val form = _uiState.value.addCardForm
        if (!form.canSubmit) return

        _savedCards.value = appendSavedCard(_savedCards.value, form)

        _uiState.update {
            it.copy(
                isAddCardBottomSheetVisible = false,
                addCardForm = AddSavedCardFormState(),
            )
        }
    }

    companion object {
        private val _savedCards =
            MutableStateFlow(
                listOf(
                    SavedCardUi(
                        cardId = "tourist_card_1",
                        bankName = "Garanti BBVA",
                        cardNumber = "**** **** **** 4567",
                        cardHolderName = "Ahmet Karagünlü",
                        expiryDate = "12/28",
                        isDefault = true,
                    ),
                    SavedCardUi(
                        cardId = "tourist_card_2",
                        bankName = "Ziraat Bankası",
                        cardNumber = "**** **** **** 9821",
                        cardHolderName = "Ahmet Karagünlü",
                        expiryDate = "07/27",
                        isDefault = false,
                    ),
                    SavedCardUi(
                        cardId = "tourist_card_3",
                        bankName = "İş Bankası",
                        cardNumber = "**** **** **** 1122",
                        cardHolderName = "Ahmet Karagünlü",
                        expiryDate = "05/29",
                        isDefault = false,
                    ),
                ),
            )

        val savedCards: StateFlow<List<SavedCardUi>> = _savedCards.asStateFlow()

        fun getSavedCards(): List<SavedCardUi> = _savedCards.value
    }
}
