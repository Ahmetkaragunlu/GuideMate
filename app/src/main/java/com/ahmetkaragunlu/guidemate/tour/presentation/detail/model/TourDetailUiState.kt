package com.ahmetkaragunlu.guidemate.tour.presentation.detail.model

import androidx.annotation.DrawableRes
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory

data class TourDetailUiState(
    val tour: TourDetailTourUiState = TourDetailTourUiState(),
    val session: TourDetailSessionUiState = TourDetailSessionUiState(),
    val guide: TourDetailGuideUiState = TourDetailGuideUiState(),
    val reviews: List<TourDetailReviewUiModel> = emptyList(),
)

data class TourDetailTourUiState(
    val id: String = "",
    val title: String = "",
    @param:DrawableRes val imageResId: Int = R.drawable.ic_image_unavailable,
    val imageUrl: String? = null,
    val rating: Double? = null,
    val reviewCount: Long = 0,
    val location: String = "",
    val languagesFlag: String = "",
    val languagesText: String = "",
    val category: TourCategory? = null,
    val description: String = "",
)

data class TourDetailSessionUiState(
    val sessionId: String = "",
    val date: String = "",
    val durationMinutes: Int = 0,
    val priceMinor: Long = 0,
    val reservedParticipantCount: Int? = null,
    val bookedCount: Int = 0,
    val capacity: Int = 0,
    val meetingPoint: String = "",
    val status: TourDetailStatus? = null,
    val cancellationReason: String? = null,
)

data class TourDetailGuideUiState(
    val id: Long = 0L,
    val name: String = "",
    @param:DrawableRes val imageResId: Int = R.drawable.ic_default_avatar,
    val imageUrl: String? = null,
)
