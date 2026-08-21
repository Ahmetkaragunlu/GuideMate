package com.ahmetkaragunlu.guidemate.payment.presentation.savedpaymentmethod.model

import com.ahmetkaragunlu.guidemate.payment.presentation.model.SavedPaymentCardUiModel

data class SavedCardsUiState(
    val savedCards: List<SavedPaymentCardUiModel> = emptyList(),
    val showDeleteDialogFor: String? = null,
    val showMakeDefaultDialogFor: String? = null,
    val isAddCardBottomSheetVisible: Boolean = false,
    val saveCardConsentChecked: Boolean = false,
)
