package com.ahmetkaragunlu.guidemate.tour.presentation.mapper

import com.ahmetkaragunlu.guidemate.tour.presentation.model.PopularTourCardUiModel
import com.ahmetkaragunlu.guidemate.tour.domain.model.catalog.TourWithSession

fun TourWithSession.toPopularTourCardUiModel(): PopularTourCardUiModel =
    PopularTourCardUiModel(
        id = session.id,
        title = tour.title,
        imageResId = tour.coverImageResId,
        imageUrl = tour.coverImageUrl,
        rating = tour.averageRating?.toString() ?: "-",
        reviewCount = "(${tour.reviewCount})",
        priceMinor = session.priceMinor,
        languagesFlag = tour.languages.joinToString(separator = " ") { it.flagEmoji },
        languagesText = tour.languages.joinToString(separator = ", ") { it.shortCode },
        guideName = tour.guide.displayName,
        guideImageResId = tour.guide.profileImageResId,
        guideImageUrl = tour.guide.profileImageUrl,
    )
