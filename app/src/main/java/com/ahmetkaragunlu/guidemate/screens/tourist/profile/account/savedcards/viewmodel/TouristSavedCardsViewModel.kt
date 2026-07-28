package com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.savedcards.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.screens.tourist.finance.store.TouristFinanceStore
import com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.savedcards.model.SavedCardsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel
class TouristSavedCardsViewModel
    @Inject
    constructor(
        private val financeStore: TouristFinanceStore,
    ) : ViewModel() {
        private val actionState = MutableStateFlow(SavedCardsUiState())

        val uiState: StateFlow<SavedCardsUiState> =
            combine(financeStore.state, actionState) { finance, action ->
                action.copy(savedCards = finance.savedCards)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue =
                    SavedCardsUiState(savedCards = financeStore.state.value.savedCards),
            )

        fun onShowDeleteDialog(cardId: String) {
            actionState.update { it.copy(showDeleteDialogFor = cardId) }
        }

        fun onDismissDeleteDialog() {
            actionState.update { it.copy(showDeleteDialogFor = null) }
        }

        fun onShowMakeDefaultDialog(cardId: String) {
            actionState.update { it.copy(showMakeDefaultDialogFor = cardId) }
        }

        fun onDismissMakeDefaultDialog() {
            actionState.update { it.copy(showMakeDefaultDialogFor = null) }
        }

        fun onConfirmDeleteCard() {
            val cardId = actionState.value.showDeleteDialogFor ?: return
            financeStore.deleteCard(cardId)
            onDismissDeleteDialog()
        }

        fun onConfirmMakeDefaultCard() {
            val cardId = actionState.value.showMakeDefaultDialogFor ?: return
            financeStore.makeDefaultCard(cardId)
            onDismissMakeDefaultDialog()
        }

        fun onShowAddCardSheet() {
            actionState.update { it.copy(isAddCardBottomSheetVisible = true) }
        }

        fun onDismissAddCardSheet() {
            actionState.update {
                it.copy(
                    isAddCardBottomSheetVisible = false,
                    saveCardConsentChecked = false,
                )
            }
        }

        fun onSaveCardConsentChange(isChecked: Boolean) {
            actionState.update { it.copy(saveCardConsentChecked = isChecked) }
        }

    }
