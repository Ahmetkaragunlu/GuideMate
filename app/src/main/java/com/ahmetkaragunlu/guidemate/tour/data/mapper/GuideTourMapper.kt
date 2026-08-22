package com.ahmetkaragunlu.guidemate.tour.data.mapper

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.location.data.LocaleSelectionCatalog
import com.ahmetkaragunlu.guidemate.common.network.model.ApiPageResponse
import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.media.data.mapper.toDomain
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuidePublicSummary
import com.ahmetkaragunlu.guidemate.profile.domain.model.level.GuideLevelTier
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.CreateGuideTourRequestDto
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.GuideDashboardResponseDto
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.GuideTourCardResponseDto
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.SubmitTourChangeRequestDto
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.TourContentRequestDto
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.TourDetailResponseDto
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.TourReviewSubmissionResponseDto
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.TourSessionRequestDto
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.TourSessionResponseDto
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.UpdateTourSessionRequestDto
import com.ahmetkaragunlu.guidemate.tour.domain.model.Tour
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourApprovalStatus
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourDetails
import com.ahmetkaragunlu.guidemate.tour.domain.model.guide.GuideDashboard
import com.ahmetkaragunlu.guidemate.tour.domain.model.guide.GuideTourCard
import com.ahmetkaragunlu.guidemate.tour.domain.model.guide.TourReviewSubmission
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.CreateGuideTourInput
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.SubmitTourChangeInput
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.TourContentInput
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.TourSessionInput
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.UpdateTourSessionInput
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.TourCancellationActor
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.TourSession
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.TourSessionStatus
import java.time.Instant
import java.util.Locale

fun ApiPageResponse<GuideTourCardResponseDto>.toDomain(): PagedResult<GuideTourCard> =
    PagedResult(
        items = content.map(GuideTourCardResponseDto::toDomain),
        page = page,
        size = size,
        totalElements = totalElements,
        totalPages = totalPages,
        isFirst = isFirst,
        isLast = isLast,
    )

fun GuideTourCardResponseDto.toDomain(): GuideTourCard =
    GuideTourCard(
        tourId = tourId,
        sessionId = sessionId,
        tourVersion = tourVersion,
        sessionVersion = sessionVersion,
        title = title,
        cityName = cityName,
        countryCode = countryCode,
        timeZoneId = timeZoneId,
        category = categoryCode.toTourCategory(),
        languageCodes = languageCodes,
        cover = cover.toDomain(),
        startsAt = Instant.parse(startsAt),
        durationMinutes = durationMinutes,
        priceMinor = priceMinor,
        currencyCode = currencyCode,
        bookedCount = bookedCount,
        capacity = capacity,
        averageRating = averageRating,
        reviewCount = reviewCount,
        netEarningsMinor = netEarningsMinor,
        approvalStatus = TourApprovalStatus.valueOf(approvalStatus),
        sessionStatus = TourSessionStatus.valueOf(sessionStatus),
        rejectionReason = rejectionReason,
        canArchive = canArchive,
    )

fun TourDetailResponseDto.toDomain(): TourDetails {
    val locale = Locale.getDefault()
    val country =
        LocaleSelectionCatalog.country(countryCode, locale)?.displayName
            ?: countryCode
    return TourDetails(
        tour =
            Tour(
                id = tourId,
                version = version,
                guide =
                    GuidePublicSummary(
                        id = guide.guideId.toString(),
                        displayName = guide.displayName,
                        profileImageResId = R.drawable.unnamed,
                        profileImageUrl = guide.avatar?.imageUrl,
                    ),
                title = title,
                description = description,
                countryCode = countryCode,
                country = country,
                cityPlaceId = cityPlaceId,
                city = cityName,
                timeZoneId = timeZoneId,
                category = categoryCode.toTourCategory(),
                languages = languageCodes.map { it.toTourLanguage(locale) },
                coverImageResId = R.drawable.example,
                coverMediaId = cover.mediaAssetId,
                coverImageUrl = cover.imageUrl,
                approvalStatus = TourApprovalStatus.valueOf(approvalStatus),
                approvalSubmittedAt = submittedAt?.let(Instant::parse),
                publishedAt = publishedAt?.let(Instant::parse),
                rejectionReason = rejectionReason,
                averageRating = averageRating.takeIf { reviewCount > 0 },
                reviewCount = reviewCount,
            ),
        sessions = sessions.map(TourSessionResponseDto::toDomain),
    )
}

fun TourSessionResponseDto.toDomain(): TourSession =
    TourSession(
        id = sessionId,
        tourId = tourId,
        version = version,
        meetingPoint = meetingPoint,
        startsAt = Instant.parse(startsAt),
        durationMinutes = durationMinutes,
        priceMinor = priceMinor,
        currencyCode = currencyCode,
        capacity = capacity,
        bookedCount = bookedCount,
        status = TourSessionStatus.valueOf(status),
        cancellationActor = cancellationActor?.let(TourCancellationActor::valueOf),
        cancellationReason = cancellationReason,
        cancelledAt = cancelledAt?.let(Instant::parse),
    )

fun GuideDashboardResponseDto.toDomain(): GuideDashboard =
    GuideDashboard(
        activeSessionCount = activeSessionCount,
        pendingReviewCount = pendingReviewCount,
        completedSessionCount = completedSessionCount,
        totalParticipantCount = totalParticipantCount,
        averageRating = averageRating,
        reviewCount = reviewCount,
        level = GuideLevelTier.valueOf(level),
        currentMonthEarningsMinor = currentMonthEarningsMinor,
        currencyCode = currencyCode,
    )

fun TourReviewSubmissionResponseDto.toDomain(): TourReviewSubmission =
    TourReviewSubmission(
        reviewId = reviewId,
        reviewType = reviewType,
        reviewStatus = reviewStatus,
        details = tour.toDomain(),
    )

fun CreateGuideTourInput.toDto(): CreateGuideTourRequestDto =
    CreateGuideTourRequestDto(
        tour = content.toDto(),
        session = session.toDto(),
    )

fun SubmitTourChangeInput.toDto(): SubmitTourChangeRequestDto =
    SubmitTourChangeRequestDto(
        baseVersion = baseVersion,
        proposedTour = content.toDto(),
    )

fun UpdateTourSessionInput.toDto(): UpdateTourSessionRequestDto =
    UpdateTourSessionRequestDto(
        version = version,
        meetingPoint = session.meetingPoint,
        startsAt = session.startsAt.toString(),
        durationMinutes = session.durationMinutes,
        priceMinor = session.priceMinor,
        capacity = session.capacity,
    )

fun TourSessionInput.toDto(): TourSessionRequestDto =
    TourSessionRequestDto(
        meetingPoint = meetingPoint,
        startsAt = startsAt.toString(),
        durationMinutes = durationMinutes,
        priceMinor = priceMinor,
        capacity = capacity,
    )

private fun TourContentInput.toDto(): TourContentRequestDto =
    TourContentRequestDto(
        title = title,
        description = description,
        countryCode = countryCode,
        cityPlaceId = cityPlaceId,
        cityName = cityName,
        timeZoneId = timeZoneId,
        categoryCode = category.code,
        languageCodes = languageCodes,
        coverMediaId = coverMediaId,
    )
