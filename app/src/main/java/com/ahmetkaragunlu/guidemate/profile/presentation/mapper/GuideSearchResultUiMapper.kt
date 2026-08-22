package com.ahmetkaragunlu.guidemate.profile.presentation.mapper

import com.ahmetkaragunlu.guidemate.profile.domain.model.GuideSearchResult
import com.ahmetkaragunlu.guidemate.profile.presentation.model.GuideResultUiModel

fun GuideSearchResult.toGuideResultUiModel(): GuideResultUiModel =
    GuideResultUiModel(
        guideId = guideId,
        displayName = displayName,
        specialtyTitle = specialtyTitle,
        avatarUrl = avatar?.imageUrl,
        averageRating = averageRating,
    )
