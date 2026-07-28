package com.ahmetkaragunlu.guidemate.screens.guide.tours.model

import com.ahmetkaragunlu.guidemate.screens.common.selection.model.LanguageOption
import com.ahmetkaragunlu.guidemate.screens.common.tours.model.TourLanguage

fun LanguageOption.toTourLanguage(): TourLanguage =
    TourLanguage(
        code = code,
        flagEmoji = flagEmoji,
        displayName = displayName,
        shortCode = shortCode,
    )
