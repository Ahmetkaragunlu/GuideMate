package com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.model

import androidx.annotation.DrawableRes
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourApprovalStatus
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.TourSessionStatus

data class GuideTourCardUiModel(
    val id: String,
    val tourId: String,
    val title: String,
    val date: String,
    val location: String,
    val media: GuideTourCardMediaUiModel,
    val participantCount: Int,
    val languages: GuideTourCardLanguagesUiModel,
    val category: TourCategory,
    val pricing: GuideTourCardPricingUiModel,
    val rating: GuideTourCardRatingUiModel?,
    val status: GuideTourCardStatusUiModel,
)

data class GuideTourCardMediaUiModel(
    @param:DrawableRes val imageResId: Int,
    val imageUrl: String?,
)

data class GuideTourCardLanguagesUiModel(
    val flags: String,
    val shortCodes: String,
)

data class GuideTourCardPricingUiModel(
    val priceMinor: Long,
    val earningsMinor: Long?,
)

data class GuideTourCardRatingUiModel(
    val value: Double,
    val reviewCount: Long,
)

data class GuideTourCardStatusUiModel(
    val approvalStatus: TourApprovalStatus,
    val sessionStatus: TourSessionStatus,
    val rejectionReason: String?,
    val canArchive: Boolean,
) {
    val isBookingOpen: Boolean
        get() = sessionStatus == TourSessionStatus.OPEN_FOR_BOOKING
}
