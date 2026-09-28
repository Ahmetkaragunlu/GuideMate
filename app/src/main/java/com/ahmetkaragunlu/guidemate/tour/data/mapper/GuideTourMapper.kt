package com.ahmetkaragunlu.guidemate.tour.data.mapper

import com.ahmetkaragunlu.guidemate.common.location.locale.LocaleSelectionCatalog
import com.ahmetkaragunlu.guidemate.common.network.model.response.ApiPageResponse
import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.media.data.mapper.toDomain
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuidePublicSummary
import com.ahmetkaragunlu.guidemate.profile.domain.model.level.GuideLevelTier
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.request.CreateGuideTourRequest
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.response.GuideDashboardResponse
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.response.GuideTourCardResponse
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.request.SubmitTourChangeRequest
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.request.TourContentRequest
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.response.TourDetailResponse
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.response.TourReviewSubmissionResponse
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.request.TourSessionRequest
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.response.TourSessionResponse
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.request.UpdateTourSessionRequest
import com.ahmetkaragunlu.guidemate.tour.domain.model.Tour
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourApprovalStatus
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourDetails
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourLocation
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourPublication
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourReviews
import com.ahmetkaragunlu.guidemate.tour.domain.model.guide.GuideDashboard
import com.ahmetkaragunlu.guidemate.tour.domain.model.guide.GuideTourCard
import com.ahmetkaragunlu.guidemate.tour.domain.model.guide.GuideTourCardPricing
import com.ahmetkaragunlu.guidemate.tour.domain.model.guide.GuideTourCardSchedule
import com.ahmetkaragunlu.guidemate.tour.domain.model.guide.GuideTourCardStats
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

fun ApiPageResponse<GuideTourCardResponse>.toDomain(): PagedResult<GuideTourCard> =
    PagedResult(
        items = content.map(GuideTourCardResponse::toDomain),
        page = page,
        size = size,
        totalElements = totalElements,
        totalPages = totalPages,
        isFirst = isFirst,
        isLast = isLast,
    )

fun GuideTourCardResponse.toDomain(): GuideTourCard =
    GuideTourCard(
        tourId = tourId,
        sessionId = sessionId,
        tourVersion = tourVersion,
        sessionVersion = sessionVersion,
        title = title,
        schedule =
            GuideTourCardSchedule(
                cityName = cityName,
                countryCode = countryCode,
                timeZoneId = timeZoneId,
                startsAt = Instant.parse(startsAt),
                durationMinutes = durationMinutes,
            ),
        category = categoryCode.toTourCategory(),
        languageCodes = languageCodes,
        cover = cover.toDomain(),
        pricing =
            GuideTourCardPricing(
                priceMinor = priceMinor,
                currencyCode = currencyCode,
                netEarningsMinor = netEarningsMinor,
            ),
        stats =
            GuideTourCardStats(
                bookedCount = bookedCount,
                capacity = capacity,
                averageRating = averageRating,
                reviewCount = reviewCount,
            ),
        approvalStatus = TourApprovalStatus.valueOf(approvalStatus),
        sessionStatus = TourSessionStatus.valueOf(sessionStatus),
        rejectionReason = rejectionReason,
        canArchive = canArchive,
    )

fun TourDetailResponse.toDomain(): TourDetails {
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
                        id = guide.guideId,
                        displayName = guide.displayName,
                        profileImageUrl = guide.avatar?.imageUrl,
                ),
                title = title,
                description = description,
                location =
                    TourLocation(
                        countryCode = countryCode,
                        country = country,
                        cityPlaceId = cityPlaceId,
                        city = cityName,
                        timeZoneId = timeZoneId,
                    ),
                category = categoryCode.toTourCategory(),
                languages = languageCodes.map { it.toTourLanguage(locale) },
                cover = cover.toDomain(),
                publication =
                    TourPublication(
                        approvalStatus = TourApprovalStatus.valueOf(approvalStatus),
                        approvalSubmittedAt = submittedAt?.let(Instant::parse),
                        publishedAt = publishedAt?.let(Instant::parse),
                        rejectionReason = rejectionReason,
                    ),
                reviews =
                    TourReviews(
                        averageRating = averageRating.takeIf { reviewCount > 0 },
                        reviewCount = reviewCount,
                    ),
            ),
        sessions = sessions.map(TourSessionResponse::toDomain),
    )
}

fun TourSessionResponse.toDomain(): TourSession =
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

fun GuideDashboardResponse.toDomain(): GuideDashboard =
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

fun TourReviewSubmissionResponse.toDomain(): TourReviewSubmission =
    TourReviewSubmission(
        reviewId = reviewId,
        reviewType = reviewType,
        reviewStatus = reviewStatus,
        details = tour.toDomain(),
    )

fun CreateGuideTourInput.toDto(): CreateGuideTourRequest =
    CreateGuideTourRequest(
        tour = content.toDto(),
        session = session.toDto(),
    )

fun SubmitTourChangeInput.toDto(): SubmitTourChangeRequest =
    SubmitTourChangeRequest(
        baseVersion = baseVersion,
        proposedTour = content.toDto(),
    )

fun UpdateTourSessionInput.toDto(): UpdateTourSessionRequest =
    UpdateTourSessionRequest(
        version = version,
        meetingPoint = session.meetingPoint,
        startsAt = session.startsAt.toString(),
        durationMinutes = session.durationMinutes,
        priceMinor = session.priceMinor,
        capacity = session.capacity,
    )

fun TourSessionInput.toDto(): TourSessionRequest =
    TourSessionRequest(
        meetingPoint = meetingPoint,
        startsAt = startsAt.toString(),
        durationMinutes = durationMinutes,
        priceMinor = priceMinor,
        capacity = capacity,
    )

private fun TourContentInput.toDto(): TourContentRequest =
    TourContentRequest(
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
