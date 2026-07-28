package com.ahmetkaragunlu.guidemate.screens.guide.tours.model

import androidx.annotation.DrawableRes
import com.ahmetkaragunlu.guidemate.screens.common.tours.category.TourCategory
import com.ahmetkaragunlu.guidemate.screens.common.tours.model.TourApprovalStatus
import com.ahmetkaragunlu.guidemate.screens.common.tours.model.session.TourSessionStatus

data class GuideTourCardUiModel(
    val id: String,
    val tourId: String,
    val title: String,
    val date: String,
    val location: String,
    @param:DrawableRes val imageResId: Int,
    val imageUrl: String? = null,
    val participantCount: Int,
    val capacity: Int,
    val languagesFlag: String,
    val languagesText: String,
    val category: TourCategory,
    val priceMinor: Long,
    val rating: Double?,
    val reviewCount: Int?,
    val approvalStatus: TourApprovalStatus,
    val sessionStatus: TourSessionStatus,
    val rejectionReason: String? = null,
    val canArchive: Boolean = false,
    val earningsMinor: Long? = null,
) {
    val isBookingOpen: Boolean
        get() = sessionStatus == TourSessionStatus.OPEN_FOR_BOOKING
}
