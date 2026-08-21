package com.ahmetkaragunlu.guidemate.reservation.presentation.trips.model

import androidx.annotation.DrawableRes
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailStatus
import java.time.Instant

data class TripUiModel(
    val id: String,
    val tourSessionId: String,
    val title: String,
    val date: String,
    val location: String,
    @param:DrawableRes val imageResId: Int,
    val imageUrl: String? = null,
    val participantCount: Int,
    val category: TourCategory,
    val languagesFlag: String,
    val languagesText: String,
    val priceMinor: Long,
    val rating: Double? = null,
    val reviewCount: Int? = null,
    val startsAt: Instant,
    val sessionStatus: TourDetailStatus? = null,
    val cancellationReason: String? = null,
) {
    val isPast: Boolean
        get() = sessionStatus != null
}
