package com.ahmetkaragunlu.guidemate.tour.domain.model.guide

data class TourReviewSubmission(
    val reviewId: String,
    val reviewType: String,
    val reviewStatus: String,
    val details: GuideTourDetails,
)
