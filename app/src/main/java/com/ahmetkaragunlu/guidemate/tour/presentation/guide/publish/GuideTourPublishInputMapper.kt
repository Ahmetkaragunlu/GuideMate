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
    val selectedCategory = content.category ?: return null
    val startsAt = toStartInstant() ?: return null
    val duration = session.durationMinutes ?: return null
    val amount = session.price.toCurrencyMinorUnitsOrNull() ?: return null
    val participantCapacity = session.capacity.toIntOrNull() ?: return null
    return CreateGuideTourInput(
        content =
            TourContentInput(
                title = content.tourName.trim(),
                description = content.tourDescription.trim(),
                countryCode = location.countryCode,
                cityPlaceId = location.cityPlaceId,
                cityName = location.city,
                timeZoneId = location.timeZoneId,
                category = selectedCategory,
                languageCodes = content.spokenLanguages.map { it.code },
                coverMediaId = coverMediaId.orEmpty(),
            ),
        session =
            TourSessionInput(
                meetingPoint = session.meetingPoint.trim(),
                startsAt = startsAt,
                durationMinutes = duration,
                priceMinor = amount,
                capacity = participantCapacity,
            ),
    )
}

internal fun GuideTourPublishUiState.toStartInstant(): Instant? {
    val date = session.tourDate ?: return null
    val time = session.startTime ?: return null
    return runCatching {
        date.atTime(time).atZone(location.timeZoneId.toZoneId()).toInstant()
    }.getOrNull()
}

internal fun String.toZoneId(): ZoneId =
    runCatching { ZoneId.of(this) }.getOrDefault(ZoneId.systemDefault())
