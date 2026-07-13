package com.ahmetkaragunlu.guidemate.screens.common.tours.detail.model

import androidx.annotation.DrawableRes
import com.ahmetkaragunlu.guidemate.R

data class TourDetailUiState(
    val sessionId: String = "",
    val tourId: String = "",
    val title: String = "",
    @param:DrawableRes val imageResId: Int = R.drawable.example,
    val imageUrl: String? = null,
    val rating: Double? = null,
    val reviewCount: Int = 0,
    val date: String = "",
    val durationMinutes: Int = 0,
    val location: String = "",
    val languagesFlag: String = "",
    val languagesText: String = "",
    val category: String = "",
    val price: Double = 0.0,
    val bookedCount: Int = 0,
    val capacity: Int = 0,
    val description: String = "",
    val meetingPoint: String = "",
    val sessionStatus: TourDetailStatus? = null,
    val cancellationReason: String? = null,
    val guideName: String = "",
    val guideImageResId: Int = R.drawable.unnamed,
    val reviews: List<TourDetailReviewUiModel> = emptyList(),
)
