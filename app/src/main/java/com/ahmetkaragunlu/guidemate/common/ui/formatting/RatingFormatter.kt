package com.ahmetkaragunlu.guidemate.common.ui.formatting

import java.util.Locale

fun Double.toRatingText(locale: Locale = Locale.getDefault()): String =
    String.format(locale, "%.1f", this)
