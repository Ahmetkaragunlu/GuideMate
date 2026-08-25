package com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.edit

import com.ahmetkaragunlu.guidemate.common.ui.formatting.toCurrencyMinorUnitsOrNull
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.TourContentInput
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.TourSessionInput
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.edit.model.GuideTourEditUiState
import java.time.LocalDateTime
import java.time.ZoneId

internal fun GuideTourEditUiState.toContentInputOrNull(coverMediaId: String?): TourContentInput? {
    val selectedCategory = category ?: return null
    if (
        title.isBlank() ||
        description.isBlank() ||
        countryCode.isBlank() ||
        cityPlaceId.isBlank() ||
        location.isBlank() ||
        timeZoneId.isBlank() ||
        languages.isEmpty() ||
        coverMediaId.isNullOrBlank()
    ) {
        return null
    }
    return TourContentInput(
        title = title.trim(),
        description = description.trim(),
        countryCode = countryCode,
        cityPlaceId = cityPlaceId,
        cityName = location,
        timeZoneId = timeZoneId,
        category = selectedCategory,
        languageCodes = languages.map { it.code },
        coverMediaId = coverMediaId,
    )
}

internal fun GuideTourEditUiState.toSessionInputOrNull(): TourSessionInput? {
    val date = tourDate ?: return null
    val time = startTime ?: return null
    val duration = durationMinutes.toIntOrNull()?.takeIf { it > 0 } ?: return null
    val amount = price.toCurrencyMinorUnitsOrNull()?.takeIf { it > 0 } ?: return null
    val participantCapacity = capacity.toIntOrNull()?.takeIf { it > 0 } ?: return null
    if (meetingPoint.isBlank() || participantCapacity < 1) return null
    val startsAt =
        runCatching {
            LocalDateTime.of(date, time).atZone(timeZoneId.toZoneId()).toInstant()
        }.getOrNull() ?: return null
    return TourSessionInput(
        meetingPoint = meetingPoint.trim(),
        startsAt = startsAt,
        durationMinutes = duration,
        priceMinor = amount,
        capacity = participantCapacity,
    )
}

internal fun GuideTourEditUiState.hasChangesFrom(original: GuideTourEditUiState?): Boolean =
    hasContentChangesFrom(original) || hasSessionChangesFrom(original)

internal fun GuideTourEditUiState.hasContentChangesFrom(original: GuideTourEditUiState?): Boolean =
    original != null &&
        (
            title != original.title ||
                description != original.description ||
                category != original.category ||
                languages != original.languages ||
                selectedCoverImageUri != null
        )

internal fun GuideTourEditUiState.hasSessionChangesFrom(original: GuideTourEditUiState?): Boolean =
    original != null &&
        (
            meetingPoint != original.meetingPoint ||
                tourDate != original.tourDate ||
                startTime != original.startTime ||
                durationMinutes != original.durationMinutes ||
                price != original.price ||
                capacity != original.capacity
        )

internal fun GuideTourEditUiState.withContentFrom(current: GuideTourEditUiState): GuideTourEditUiState =
    copy(
        content =
            content.copy(
                title = current.title,
                description = current.description,
                category = current.category,
                languages = current.languages,
                coverMediaId = current.coverMediaId,
                coverImageUrl = current.coverImageUrl,
                selectedCoverImageUri = null,
            ),
    )

internal fun GuideTourEditUiState.withSessionFrom(current: GuideTourEditUiState): GuideTourEditUiState =
    copy(
        session =
            session.copy(
                meetingPoint = current.meetingPoint,
                tourDate = current.tourDate,
                startTime = current.startTime,
                durationMinutes = current.durationMinutes,
                price = current.price,
                capacity = current.capacity,
            ),
    )

internal fun String.toZoneId(): ZoneId =
    runCatching { ZoneId.of(this) }.getOrDefault(ZoneId.systemDefault())
