package com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.savedcards.model

import com.ahmetkaragunlu.guidemate.screens.tourist.finance.model.SavedPaymentCardUiModel

data class SavedCardsUiState(
    val savedCards: List<SavedPaymentCardUiModel> = emptyList(),
    val showDeleteDialogFor: String? = null,
    val showMakeDefaultDialogFor: String? = null,
    val isAddCardBottomSheetVisible: Boolean = false,
    val saveCardConsentChecked: Boolean = false,
)
