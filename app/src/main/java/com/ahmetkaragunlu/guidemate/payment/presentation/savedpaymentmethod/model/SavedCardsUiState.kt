package com.ahmetkaragunlu.guidemate.payment.presentation.savedpaymentmethod.model

import com.ahmetkaragunlu.guidemate.payment.presentation.model.SavedPaymentCardUiModel
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState

data class SavedCardsUiState(
    val loadState: ContentLoadState = ContentLoadState.LOADING,
    val savedCards: List<SavedPaymentCardUiModel> = emptyList(),
    val showDeleteDialogFor: String? = null,
    val showMakeDefaultDialogFor: String? = null,
    val isAddCardBottomSheetVisible: Boolean = false,
    val isMutationInProgress: Boolean = false,
    val errorMessage: String? = null,
)
