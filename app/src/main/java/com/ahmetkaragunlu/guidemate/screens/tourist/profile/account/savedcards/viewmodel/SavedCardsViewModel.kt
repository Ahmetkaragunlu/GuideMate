package com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.savedcards.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.savedcards.model.SavedCardUi
import com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.savedcards.model.SavedCardsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SavedCardsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(
        SavedCardsUiState(savedCards = getSavedCards())
    )
    val uiState: StateFlow<SavedCardsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            savedCards.collectLatest { savedCards ->
                _uiState.update { it.copy(savedCards = savedCards) }
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
        val cardIdToDelete = _uiState.value.showDeleteDialogFor ?: return

        deleteCard(cardIdToDelete)
        _uiState.update { currentState ->
            currentState.copy(
                showDeleteDialogFor = null
            )
        }
    }

    fun onConfirmMakeDefaultCard() {
        val cardIdToMakeDefault = _uiState.value.showMakeDefaultDialogFor ?: return
        makeDefault(cardIdToMakeDefault)
        _uiState.update { currentState ->
            currentState.copy(
                showMakeDefaultDialogFor = null
            )
        }
    }

    companion object {
        private val _savedCards =
            MutableStateFlow(
                listOf(
                    SavedCardUi(
                        cardId = "card_1",
                        bankName = "Garanti BBVA",
                        cardNumber = "**** **** **** 4567",
                        cardHolderName = "Ahmet Karagünlü",
                        expiryDate = "12/28",
                        isDefault = true,
                    ),
                    SavedCardUi(
                        cardId = "card_2",
                        bankName = "Ziraat Bankası",
                        cardNumber = "**** **** **** 9821",
                        cardHolderName = "Ahmet Karagünlü",
                        expiryDate = "07/27",
                        isDefault = false,
                    ),
                    SavedCardUi(
                        cardId = "card_3",
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

        fun deleteCard(cardId: String): List<SavedCardUi> {
            val filteredCards = _savedCards.value.filterNot { it.cardId == cardId }
            _savedCards.value =
                if (filteredCards.isNotEmpty() && filteredCards.none { it.isDefault }) {
                    filteredCards.mapIndexed { index, card -> card.copy(isDefault = index == 0) }
                } else {
                    filteredCards
                }
            return _savedCards.value
        }

        fun makeDefault(cardId: String): List<SavedCardUi> {
            _savedCards.value =
                _savedCards.value
                    .map { card -> card.copy(isDefault = card.cardId == cardId) }
                    .sortedByDescending { it.isDefault }
            return _savedCards.value
        }
    }
}
