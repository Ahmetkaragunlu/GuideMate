package com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.savedcards.viewmodel

import androidx.lifecycle.ViewModel
import com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.savedcards.model.SavedCardUi
import com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.savedcards.model.SavedCardsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class SavedCardsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(
        SavedCardsUiState(savedCards = manualSavedCards())
    )
    val uiState: StateFlow<SavedCardsUiState> = _uiState.asStateFlow()

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

        _uiState.update { currentState ->
            val filteredList = currentState.savedCards.filterNot { it.cardId == cardIdToDelete }
            val finalList =
                if (filteredList.isNotEmpty() && (filteredList.size == 1 || filteredList.none { it.isDefault })) {
                    filteredList.mapIndexed { index, card ->
                        card.copy(isDefault = index == 0)
                    }
                } else {
                    filteredList
                }

            currentState.copy(
                savedCards = finalList,
                showDeleteDialogFor = null
            )
        }
    }

    fun onConfirmMakeDefaultCard() {
        val cardIdToMakeDefault = _uiState.value.showMakeDefaultDialogFor ?: return
        _uiState.update { currentState ->
            currentState.copy(
                savedCards = currentState.savedCards.map { card ->
                    card.copy(isDefault = card.cardId == cardIdToMakeDefault)
                }.sortedByDescending { it.isDefault },
                showMakeDefaultDialogFor = null
            )
        }
    }


    private fun manualSavedCards(): List<SavedCardUi> = listOf(
        SavedCardUi(
            "card_1",
            "Garanti BBVA",
            "**** **** **** 4567",
            "Ahmet Karagünlü",
            "12/28",
            true
        ),
        SavedCardUi(
            "card_2",
            "Ziraat Bankası",
            "**** **** **** 9821",
            "Ahmet Karagünlü",
            "07/27",
            false
        ),
        SavedCardUi(
            "card_3",
            "İş Bankası",
            "**** **** **** 1122",
            "Ahmet Karagünlü",
            "05/29",
            false
        ),
    )
}