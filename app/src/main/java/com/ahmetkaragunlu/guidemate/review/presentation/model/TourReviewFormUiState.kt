package com.ahmetkaragunlu.guidemate.review.presentation.model

data class TourReviewFormUiState(
    val isVisible: Boolean = false,
    val rating: Int = 0,
    val comment: String = "",
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val showSuccessDialog: Boolean = false,
)
