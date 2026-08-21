package com.ahmetkaragunlu.guidemate.review.presentation.model

import androidx.annotation.StringRes

data class TourReviewFormUiState(
    val isVisible: Boolean = false,
    val rating: Int = 0,
    val comment: String = "",
    val isSubmitting: Boolean = false,
    @param:StringRes val errorResId: Int? = null,
    val showSuccessDialog: Boolean = false,
)
