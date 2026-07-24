package com.ahmetkaragunlu.guidemate.screens.guide.tours.model

import com.ahmetkaragunlu.guidemate.screens.common.selection.model.LanguageOption

fun LanguageOption.toTourLanguage(): TourLanguage =
    TourLanguage(
        code = code,
        flagEmoji = flagEmoji,
        displayName = displayName,
        shortCode = shortCode,
    )
