package com.ahmetkaragunlu.guidemate.tour.domain.model.guide

import com.ahmetkaragunlu.guidemate.tour.domain.model.TourDetails

data class TourReviewSubmission(
    val reviewId: String,
    val reviewType: String,
    val reviewStatus: String,
    val details: TourDetails,
)
