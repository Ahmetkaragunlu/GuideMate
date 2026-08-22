package com.ahmetkaragunlu.guidemate.reservation.data.remote.model

import com.ahmetkaragunlu.guidemate.media.data.remote.model.MediaReferenceResponseDto
import com.google.gson.annotations.SerializedName

data class ReservationResponseDto(
    @SerializedName("reservationId") val reservationId: String,
    @SerializedName("sessionId") val sessionId: String,
    @SerializedName("version") val version: Long,
    @SerializedName("participantCount") val participantCount: Int,
    @SerializedName("unitPriceMinor") val unitPriceMinor: Long,
    @SerializedName("totalPriceMinor") val totalPriceMinor: Long,
    @SerializedName("currencyCode") val currencyCode: String,
    @SerializedName("status") val status: String,
    @SerializedName("holdExpiresAt") val holdExpiresAt: String?,
    @SerializedName("cancellationActor") val cancellationActor: String?,
    @SerializedName("cancellationReason") val cancellationReason: String?,
    @SerializedName("cancelledAt") val cancelledAt: String?,
    @SerializedName("cancellationRefundEligibility") val cancellationRefundEligibility: String,
    @SerializedName("cancellationPolicyCode") val cancellationPolicyCode: String,
    @SerializedName("cancellationPolicyVersion") val cancellationPolicyVersion: Int,
    @SerializedName("snapshot") val snapshot: ReservationSnapshotResponseDto,
    @SerializedName("review") val review: ReservationReviewResponseDto?,
)

data class ReservationSnapshotResponseDto(
    @SerializedName("tourId") val tourId: String,
    @SerializedName("guide") val guide: ReservationGuideResponseDto,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("countryCode") val countryCode: String,
    @SerializedName("cityPlaceId") val cityPlaceId: String,
    @SerializedName("cityName") val cityName: String,
    @SerializedName("timeZoneId") val timeZoneId: String,
    @SerializedName("categoryCode") val categoryCode: String,
    @SerializedName("languageCodes") val languageCodes: List<String>,
    @SerializedName("cover") val cover: MediaReferenceResponseDto?,
    @SerializedName("startsAt") val startsAt: String,
    @SerializedName("durationMinutes") val durationMinutes: Int,
    @SerializedName("meetingPoint") val meetingPoint: String,
    @SerializedName("unitPriceMinor") val unitPriceMinor: Long,
)

data class ReservationGuideResponseDto(
    @SerializedName("guideId") val guideId: Long,
    @SerializedName("displayName") val displayName: String,
    @SerializedName("avatar") val avatar: MediaReferenceResponseDto?,
)

data class ReservationReviewResponseDto(
    @SerializedName("reviewId") val reviewId: String,
    @SerializedName("rating") val rating: Int,
    @SerializedName("comment") val comment: String?,
    @SerializedName("submittedAt") val submittedAt: String,
)

data class ReservationCancellationResponseDto(
    @SerializedName("reservation") val reservation: ReservationResponseDto,
    @SerializedName("refundEligibility") val refundEligibility: String,
    @SerializedName("refundId") val refundId: String?,
    @SerializedName("refundStatus") val refundStatus: String?,
)

data class CancelReservationRequestDto(
    @SerializedName("version") val version: Long,
    @SerializedName("reason") val reason: String?,
)
