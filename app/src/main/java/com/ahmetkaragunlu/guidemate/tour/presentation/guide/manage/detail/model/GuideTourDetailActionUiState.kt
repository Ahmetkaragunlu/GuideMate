package com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.detail.model

data class GuideTourDetailActionUiState(
    val isCancelDialogVisible: Boolean = false,
    val cancellationReason: String = "",
    val isNewSessionSheetVisible: Boolean = false,
    val newSessionForm: NewTourSessionFormState = NewTourSessionFormState(),
)
