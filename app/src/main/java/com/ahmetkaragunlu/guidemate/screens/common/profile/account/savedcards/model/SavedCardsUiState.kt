package com.ahmetkaragunlu.guidemate.screens.common.profile.account.savedcards.model

data class SavedCardsUiState(
    val savedCards: List<SavedCardUi> = emptyList(),
    val showDeleteDialogFor: String? = null,
    val showMakeDefaultDialogFor: String? = null,
    val isAddCardBottomSheetVisible: Boolean = false,
    val addCardForm: AddSavedCardFormState = AddSavedCardFormState(),
)
