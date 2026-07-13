package com.ahmetkaragunlu.guidemate.screens.guide.tours.detail.model

data class GuideTourDetailActionUiState(
    val isCancelDialogVisible: Boolean = false,
    val cancellationReason: String = "",
    val isNewSessionSheetVisible: Boolean = false,
    val newSessionForm: NewTourSessionFormState = NewTourSessionFormState(),
)
