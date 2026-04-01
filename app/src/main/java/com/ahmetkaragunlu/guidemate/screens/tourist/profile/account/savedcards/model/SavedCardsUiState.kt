package com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.savedcards.model

data class SavedCardsUiState(
    val savedCards: List<SavedCardUi> = emptyList(),
    val showDeleteDialogFor: String? = null,
    val showMakeDefaultDialogFor: String? = null,
)