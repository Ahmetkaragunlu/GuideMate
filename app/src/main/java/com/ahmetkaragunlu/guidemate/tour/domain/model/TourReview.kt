package com.ahmetkaragunlu.guidemate.tour.domain.model

import androidx.annotation.DrawableRes

data class TourReview(
    val id: String,
    val reviewerName: String,
    val rating: Int,
    val comment: String,
    @param:DrawableRes val reviewerImageResId: Int,
)
