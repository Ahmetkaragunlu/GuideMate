package com.ahmetkaragunlu.guidemate.screens.common.tours.detail.model

import androidx.annotation.DrawableRes

data class TourDetailReviewUiModel(
    val id: String,
    val reviewerName: String,
    val comment: String,
    val rating: Int,
    @param:DrawableRes val reviewerImageResId: Int,
)
