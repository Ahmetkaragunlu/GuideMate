package com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.edit.mapper

import com.ahmetkaragunlu.guidemate.common.ui.formatting.toCurrencyInput
import com.ahmetkaragunlu.guidemate.common.ui.formatting.toCurrencyMinorUnitsOrNull
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourApprovalStatus
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourDetails
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.TourContentInput
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.TourSessionInput
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.edit.model.GuideTourEditContentFormState
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.edit.model.GuideTourEditIdentityState
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.edit.model.GuideTourEditOperationState
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.edit.model.GuideTourEditSessionFormState
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.edit.model.GuideTourEditUiState
import java.time.LocalDateTime
import java.time.ZoneId

internal fun TourDetails.toGuideTourEditUiState(sessionId: String): GuideTourEditUiState? {
    val selectedSession = session(sessionId) ?: return null
    val zone = tour.location.timeZoneId.toZoneId()
    return GuideTourEditUiState(
        identity =
            GuideTourEditIdentityState(
                tourId = tour.id,
                sessionId = selectedSession.id,
                tourVersion = tour.version,
                sessionVersion = selectedSession.version,
                country = tour.location.country,
                countryCode = tour.location.countryCode,
                location = tour.location.city,
                cityPlaceId = tour.location.cityPlaceId,
                timeZoneId = tour.location.timeZoneId,
                isTourIdentityLocked = true,
            ),
        content =
            GuideTourEditContentFormState(
                title = tour.title,
                description = tour.description,
                category = tour.category,
                languages = tour.languages,
                coverImageUrl = tour.cover?.imageUrl,
                coverMediaId = tour.cover?.mediaAssetId,
            ),
        session =
            GuideTourEditSessionFormState(
                meetingPoint = selectedSession.meetingPoint,
                tourDate = selectedSession.startsAt.atZone(zone).toLocalDate(),
                startTime = selectedSession.startsAt.atZone(zone).toLocalTime(),
                durationMinutes = selectedSession.durationMinutes.toString(),
                price = selectedSession.priceMinor.toCurrencyInput(),
                capacity = selectedSession.capacity.toString(),
                hasBookings = selectedSession.bookedCount > 0,
            ),
        operation =
            GuideTourEditOperationState(
                approvalStatus = tour.publication.approvalStatus,
                requiresReviewConfirmation = tour.publication.approvalStatus == TourApprovalStatus.REJECTED,
                loadState = ContentLoadState.CONTENT,
            ),
    )
}

internal fun GuideTourEditUiState.toContentInputOrNull(coverMediaId: String?): TourContentInput? {
    val selectedCategory = content.category ?: return null
    if (
        content.title.isBlank() ||
        content.description.isBlank() ||
        identity.countryCode.isBlank() ||
        identity.cityPlaceId.isBlank() ||
        identity.location.isBlank() ||
        identity.timeZoneId.isBlank() ||
        content.languages.isEmpty() ||
        coverMediaId.isNullOrBlank()
    ) {
        return null
    }
    return TourContentInput(
        title = content.title.trim(),
        description = content.description.trim(),
        countryCode = identity.countryCode,
        cityPlaceId = identity.cityPlaceId,
        cityName = identity.location,
        timeZoneId = identity.timeZoneId,
        category = selectedCategory,
        languageCodes = content.languages.map { it.code },
        coverMediaId = coverMediaId,
    )
}

internal fun GuideTourEditUiState.toSessionInputOrNull(): TourSessionInput? {
    val date = session.tourDate ?: return null
    val time = session.startTime ?: return null
    val duration = session.durationMinutes.toIntOrNull()?.takeIf { it > 0 } ?: return null
    val amount = session.price.toCurrencyMinorUnitsOrNull()?.takeIf { it > 0 } ?: return null
    val participantCapacity = session.capacity.toIntOrNull()?.takeIf { it > 0 } ?: return null
    if (session.meetingPoint.isBlank() || participantCapacity < 1) return null
    val startsAt =
        runCatching {
            LocalDateTime.of(date, time).atZone(identity.timeZoneId.toZoneId()).toInstant()
        }.getOrNull() ?: return null
    return TourSessionInput(
        meetingPoint = session.meetingPoint.trim(),
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
            content.title != original.content.title ||
                content.description != original.content.description ||
                content.category != original.content.category ||
                content.languages != original.content.languages ||
                content.selectedCoverImageUri != null
        )

internal fun GuideTourEditUiState.hasSessionChangesFrom(original: GuideTourEditUiState?): Boolean =
    original != null &&
        (
            session.meetingPoint != original.session.meetingPoint ||
                session.tourDate != original.session.tourDate ||
                session.startTime != original.session.startTime ||
                session.durationMinutes != original.session.durationMinutes ||
                session.price != original.session.price ||
                session.capacity != original.session.capacity
        )

internal fun GuideTourEditUiState.withContentFrom(current: GuideTourEditUiState): GuideTourEditUiState =
    copy(
        content =
            content.copy(
                title = current.content.title,
                description = current.content.description,
                category = current.content.category,
                languages = current.content.languages,
                coverMediaId = current.content.coverMediaId,
                coverImageUrl = current.content.coverImageUrl,
                selectedCoverImageUri = null,
            ),
    )

internal fun GuideTourEditUiState.withSessionFrom(current: GuideTourEditUiState): GuideTourEditUiState =
    copy(
        session =
            session.copy(
                meetingPoint = current.session.meetingPoint,
                tourDate = current.session.tourDate,
                startTime = current.session.startTime,
                durationMinutes = current.session.durationMinutes,
                price = current.session.price,
                capacity = current.session.capacity,
            ),
    )

internal fun String.toZoneId(): ZoneId =
    runCatching { ZoneId.of(this) }.getOrDefault(ZoneId.systemDefault())
