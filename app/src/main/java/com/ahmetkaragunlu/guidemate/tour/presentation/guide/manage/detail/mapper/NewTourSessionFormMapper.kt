package com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.detail.mapper

import com.ahmetkaragunlu.guidemate.common.ui.formatting.toCurrencyMinorUnitsOrNull
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.TourSessionInput
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.detail.model.NewTourSessionFormState
import java.time.ZoneId

internal fun NewTourSessionFormState.toTourSessionInputOrNull(): TourSessionInput? {
    val date = selectedDate ?: return null
    val time = selectedTime ?: return null
    val duration = durationMinutes?.takeIf { it > 0 } ?: return null
    val normalizedMeetingPoint = meetingPoint.trim().takeIf(String::isNotEmpty) ?: return null
    val amount = price.toCurrencyMinorUnitsOrNull()?.takeIf { it > 0 } ?: return null
    val participantCapacity = capacity.toIntOrNull()?.takeIf { it > 0 } ?: return null
    val startsAt =
        runCatching {
            date
                .atTime(time)
                .atZone(ZoneId.of(timeZoneId))
                .toInstant()
        }.getOrNull() ?: return null

    return TourSessionInput(
        meetingPoint = normalizedMeetingPoint,
        startsAt = startsAt,
        durationMinutes = duration,
        priceMinor = amount,
        capacity = participantCapacity,
    )
}
