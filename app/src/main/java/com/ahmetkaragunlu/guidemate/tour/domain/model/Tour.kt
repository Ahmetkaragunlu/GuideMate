package com.ahmetkaragunlu.guidemate.tour.domain.model

import com.ahmetkaragunlu.guidemate.profile.domain.model.GuidePublicSummary
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaReference
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory

data class Tour(
    val id: String,
    val version: Long = 0,
    val guide: GuidePublicSummary,
    val title: String,
    val description: String,
    val location: TourLocation,
    val category: TourCategory,
    val languages: List<TourLanguage>,
    val cover: MediaReference? = null,
    val publication: TourPublication,
    val reviews: TourReviews = TourReviews(),
)
