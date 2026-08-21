package com.ahmetkaragunlu.guidemate.tour.presentation.formatting

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

fun Instant.formatTourDateTime(timeZoneId: String): String {
    val zoneId = runCatching { ZoneId.of(timeZoneId) }.getOrDefault(ZoneId.systemDefault())
    val formatter =
        DateTimeFormatter
            .ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)
            .withLocale(Locale.getDefault())
    return formatter.format(atZone(zoneId))
}
