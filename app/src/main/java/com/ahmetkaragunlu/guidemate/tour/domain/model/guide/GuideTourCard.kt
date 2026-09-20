package com.ahmetkaragunlu.guidemate.tour.domain.model.guide

import com.ahmetkaragunlu.guidemate.media.domain.model.MediaReference
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourApprovalStatus
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.TourSessionStatus

data class GuideTourCard(
    val tourId: String,
    val sessionId: String,
    val tourVersion: Long,
    val sessionVersion: Long,
    val title: String,
    val schedule: GuideTourCardSchedule,
    val category: TourCategory,
    val languageCodes: List<String>,
    val cover: MediaReference?,
    val pricing: GuideTourCardPricing,
    val stats: GuideTourCardStats,
    val approvalStatus: TourApprovalStatus,
    val sessionStatus: TourSessionStatus,
    val rejectionReason: String?,
    val canArchive: Boolean,
)
