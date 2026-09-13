package com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish

import com.ahmetkaragunlu.guidemate.common.ui.formatting.toCurrencyMinorUnitsOrNull
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.CreateGuideTourInput
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.TourContentInput
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.TourSessionInput
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.model.GuideTourPublishUiState
import java.time.Instant
import java.time.ZoneId

internal fun GuideTourPublishUiState.toCreateInputOrNull(
    coverMediaId: String?,
): CreateGuideTourInput? {
    if (firstValidationError() != null) return null
    val selectedCategory = category ?: return null
    val startsAt = toStartInstant() ?: return null
    val duration = durationMinutes ?: return null
    val amount = price.toCurrencyMinorUnitsOrNull() ?: return null
    val participantCapacity = capacity.toIntOrNull() ?: return null
    return CreateGuideTourInput(
        content =
            TourContentInput(
                title = tourName.trim(),
                description = tourDescription.trim(),
                countryCode = countryCode,
                cityPlaceId = cityPlaceId,
                cityName = city,
                timeZoneId = timeZoneId,
                category = selectedCategory,
                languageCodes = spokenLanguages.map { it.code },
                coverMediaId = coverMediaId.orEmpty(),
            ),
        session =
            TourSessionInput(
                meetingPoint = meetingPoint.trim(),
                startsAt = startsAt,
                durationMinutes = duration,
                priceMinor = amount,
                capacity = participantCapacity,
            ),
    )
}

internal fun GuideTourPublishUiState.toStartInstant(): Instant? {
    val date = tourDate ?: return null
    val time = startTime ?: return null
    return runCatching { date.atTime(time).atZone(timeZoneId.toZoneId()).toInstant() }.getOrNull()
}

internal fun String.toZoneId(): ZoneId =
    runCatching { ZoneId.of(this) }.getOrDefault(ZoneId.systemDefault())
