package com.ahmetkaragunlu.guidemate.reservation.data.mapper

import com.ahmetkaragunlu.guidemate.common.location.locale.LocaleSelectionCatalog
import com.ahmetkaragunlu.guidemate.common.network.model.ApiPageResponse
import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaReference
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuidePublicSummary
import com.ahmetkaragunlu.guidemate.reservation.data.remote.model.CancelReservationRequestDto
import com.ahmetkaragunlu.guidemate.reservation.data.remote.model.ReservationCancellationResponseDto
import com.ahmetkaragunlu.guidemate.reservation.data.remote.model.ReservationResponseDto
import com.ahmetkaragunlu.guidemate.reservation.data.remote.model.ReservationReviewResponseDto
import com.ahmetkaragunlu.guidemate.reservation.data.remote.model.ReservationSnapshotResponseDto
import com.ahmetkaragunlu.guidemate.reservation.domain.model.CancelReservationInput
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationCancellationActor
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationCancellationDetails
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationCancellationPolicy
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationCancellationResult
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationAttendance
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationLocationSnapshot
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationPurchaseDetails
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationRatingSummary
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationRefundEligibility
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationRefundStatus
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationScheduleSnapshot
import com.ahmetkaragunlu.guidemate.reservation.domain.model.TouristReservation
import com.ahmetkaragunlu.guidemate.reservation.domain.model.TouristReservationSnapshot
import com.ahmetkaragunlu.guidemate.reservation.domain.model.TouristReservationStatus
import com.ahmetkaragunlu.guidemate.review.domain.model.SubmittedReview
import com.ahmetkaragunlu.guidemate.tour.data.mapper.toTourCategory
import com.ahmetkaragunlu.guidemate.tour.data.mapper.toTourLanguage
import java.time.Instant
import java.util.Locale

fun ApiPageResponse<ReservationResponseDto>.toDomain(): PagedResult<TouristReservation> =
    PagedResult(
        items = content.map(ReservationResponseDto::toDomain),
        page = page,
        size = size,
        totalElements = totalElements,
        totalPages = totalPages,
        isFirst = isFirst,
        isLast = isLast,
    )

fun ReservationResponseDto.toDomain(): TouristReservation =
    TouristReservation(
        id = reservationId,
        tourSessionId = sessionId,
        version = version,
        purchase =
            ReservationPurchaseDetails(
                participantCount = participantCount,
                unitPriceMinor = unitPriceMinor,
                totalPriceMinor = totalPriceMinor,
                currencyCode = currencyCode,
            ),
        snapshot = snapshot.toDomain(),
        status = TouristReservationStatus.valueOf(status),
        holdExpiresAt = holdExpiresAt?.let(Instant::parse),
        cancellation =
            ReservationCancellationDetails(
                actor = cancellationActor?.let(ReservationCancellationActor::valueOf),
                reason = cancellationReason,
                cancelledAt = cancelledAt?.let(Instant::parse),
                refundEligibility =
                    cancellationRefundEligibility
                        ?.let(ReservationRefundEligibility::valueOf)
                        ?: ReservationRefundEligibility.NOT_APPLICABLE,
                policy =
                    ReservationCancellationPolicy(
                        code = cancellationPolicyCode,
                        version = cancellationPolicyVersion,
                    ),
            ),
        rating = ReservationRatingSummary(averageRating = averageRating, reviewCount = reviewCount),
        attendance = ReservationAttendance(bookedCount = bookedCount, capacity = capacity),
        review = review?.toDomain(),
    )

fun ReservationCancellationResponseDto.toDomain(): ReservationCancellationResult =
    ReservationCancellationResult(
        reservation = reservation.toDomain(),
        refundEligibility = ReservationRefundEligibility.valueOf(refundEligibility),
        refundId = refundId,
        refundStatus = refundStatus?.let(ReservationRefundStatus::valueOf),
    )

fun CancelReservationInput.toDto(): CancelReservationRequestDto =
    CancelReservationRequestDto(
        version = version,
        reason = reason?.trim()?.takeIf(String::isNotEmpty),
    )

private fun ReservationSnapshotResponseDto.toDomain(): TouristReservationSnapshot {
    val locale = Locale.getDefault()
    return TouristReservationSnapshot(
        tourId = tourId,
        guide =
            GuidePublicSummary(
                id = guide.guideId,
                displayName = guide.displayName,
                profileImageUrl = guide.avatar?.imageUrl,
            ),
        title = title,
        description = description,
        location =
            ReservationLocationSnapshot(
                countryCode = countryCode,
                country = LocaleSelectionCatalog.country(countryCode, locale)?.displayName ?: countryCode,
                cityPlaceId = cityPlaceId,
                city = cityName,
                timeZoneId = timeZoneId,
            ),
        category = categoryCode.toTourCategory(),
        languages = languageCodes.map { it.toTourLanguage(locale) },
        cover = cover?.let { media ->
            MediaReference(
                mediaAssetId = media.mediaAssetId,
                imageUrl = media.imageUrl,
            )
        },
        schedule =
            ReservationScheduleSnapshot(
                startsAt = Instant.parse(startsAt),
                durationMinutes = durationMinutes,
                meetingPoint = meetingPoint,
            ),
    )
}

private fun ReservationReviewResponseDto.toDomain(): SubmittedReview =
    SubmittedReview(
        id = reviewId,
        rating = rating,
        comment = comment.orEmpty(),
        submittedAt = Instant.parse(submittedAt),
    )
