package com.ahmetkaragunlu.guidemate.tour.presentation.detail.model

import androidx.annotation.DrawableRes
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory

data class TourDetailUiState(
    val sessionId: String = "",
    val tourId: String = "",
    val title: String = "",
    @param:DrawableRes val imageResId: Int = R.drawable.example,
    val imageUrl: String? = null,
    val rating: Double? = null,
    val reviewCount: Long = 0,
    val date: String = "",
    val durationMinutes: Int = 0,
    val location: String = "",
    val languagesFlag: String = "",
    val languagesText: String = "",
    val category: TourCategory? = null,
    val priceMinor: Long = 0,
    val bookedCount: Int = 0,
    val capacity: Int = 0,
    val description: String = "",
    val meetingPoint: String = "",
    val sessionStatus: TourDetailStatus? = null,
    val cancellationReason: String? = null,
    val guideId: String = "",
    val guideName: String = "",
    val guideImageResId: Int = R.drawable.unnamed,
    val guideImageUrl: String? = null,
    val reviews: List<TourDetailReviewUiModel> = emptyList(),
)
