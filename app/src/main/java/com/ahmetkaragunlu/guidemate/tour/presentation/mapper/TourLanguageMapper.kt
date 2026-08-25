package com.ahmetkaragunlu.guidemate.tour.presentation.mapper

import com.ahmetkaragunlu.guidemate.common.location.model.LanguageOption
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourLanguage

fun LanguageOption.toTourLanguage(): TourLanguage =
    TourLanguage(
        code = code,
        flagEmoji = flagEmoji,
        displayName = displayName,
        shortCode = shortCode,
    )
