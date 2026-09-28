package com.ahmetkaragunlu.guidemate.tour.data.remote.model.response

import com.ahmetkaragunlu.guidemate.media.data.remote.model.response.MediaReferenceResponse
import com.google.gson.annotations.SerializedName

data class PublicGuideSummaryResponse(
    @SerializedName("guideId") val guideId: Long,
    @SerializedName("displayName") val displayName: String,
    @SerializedName("avatar") val avatar: MediaReferenceResponse?,
)

data class TourSessionResponse(
    @SerializedName("sessionId") val sessionId: String,
    @SerializedName("tourId") val tourId: String,
    @SerializedName("version") val version: Long,
    @SerializedName("meetingPoint") val meetingPoint: String,
    @SerializedName("startsAt") val startsAt: String,
    @SerializedName("durationMinutes") val durationMinutes: Int,
    @SerializedName("priceMinor") val priceMinor: Long,
    @SerializedName("currencyCode") val currencyCode: String,
    @SerializedName("capacity") val capacity: Int,
    @SerializedName("bookedCount") val bookedCount: Int,
    @SerializedName("availableCapacity") val availableCapacity: Int,
    @SerializedName("status") val status: String,
    @SerializedName("cancellationActor") val cancellationActor: String?,
    @SerializedName("cancellationReason") val cancellationReason: String?,
    @SerializedName("cancelledAt") val cancelledAt: String?,
)

data class TourDetailResponse(
    @SerializedName("tourId") val tourId: String,
    @SerializedName("version") val version: Long,
    @SerializedName("guide") val guide: PublicGuideSummaryResponse,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("countryCode") val countryCode: String,
    @SerializedName("cityPlaceId") val cityPlaceId: String,
    @SerializedName("cityName") val cityName: String,
    @SerializedName("timeZoneId") val timeZoneId: String,
    @SerializedName("categoryCode") val categoryCode: String,
    @SerializedName("languageCodes") val languageCodes: List<String>,
    @SerializedName("cover") val cover: MediaReferenceResponse,
    @SerializedName("approvalStatus") val approvalStatus: String,
    @SerializedName("submittedAt") val submittedAt: String?,
    @SerializedName("publishedAt") val publishedAt: String?,
    @SerializedName("reviewedAt") val reviewedAt: String?,
    @SerializedName("rejectionReason") val rejectionReason: String?,
    @SerializedName("averageRating") val averageRating: Double,
    @SerializedName("reviewCount") val reviewCount: Long,
    @SerializedName("sessions") val sessions: List<TourSessionResponse>,
)

data class GuideTourCardResponse(
    @SerializedName("tourId") val tourId: String,
    @SerializedName("sessionId") val sessionId: String,
    @SerializedName("tourVersion") val tourVersion: Long,
    @SerializedName("sessionVersion") val sessionVersion: Long,
    @SerializedName("title") val title: String,
    @SerializedName("cityName") val cityName: String,
    @SerializedName("countryCode") val countryCode: String,
    @SerializedName("timeZoneId") val timeZoneId: String,
    @SerializedName("categoryCode") val categoryCode: String,
    @SerializedName("languageCodes") val languageCodes: List<String>,
    @SerializedName("cover") val cover: MediaReferenceResponse,
    @SerializedName("startsAt") val startsAt: String,
    @SerializedName("durationMinutes") val durationMinutes: Int,
    @SerializedName("priceMinor") val priceMinor: Long,
    @SerializedName("currencyCode") val currencyCode: String,
    @SerializedName("bookedCount") val bookedCount: Int,
    @SerializedName("capacity") val capacity: Int,
    @SerializedName("averageRating") val averageRating: Double,
    @SerializedName("reviewCount") val reviewCount: Long,
    @SerializedName("netEarningsMinor") val netEarningsMinor: Long?,
    @SerializedName("approvalStatus") val approvalStatus: String,
    @SerializedName("sessionStatus") val sessionStatus: String,
    @SerializedName("rejectionReason") val rejectionReason: String?,
    @SerializedName("canArchive") val canArchive: Boolean,
)

data class GuideDashboardResponse(
    @SerializedName("activeSessionCount") val activeSessionCount: Long,
    @SerializedName("pendingReviewCount") val pendingReviewCount: Long,
    @SerializedName("completedSessionCount") val completedSessionCount: Long,
    @SerializedName("totalParticipantCount") val totalParticipantCount: Long,
    @SerializedName("averageRating") val averageRating: Double,
    @SerializedName("reviewCount") val reviewCount: Long,
    @SerializedName("level") val level: String,
    @SerializedName("currentMonthEarningsMinor") val currentMonthEarningsMinor: Long,
    @SerializedName("currencyCode") val currencyCode: String,
)

data class TourReviewSubmissionResponse(
    @SerializedName("reviewId") val reviewId: String,
    @SerializedName("reviewType") val reviewType: String,
    @SerializedName("reviewStatus") val reviewStatus: String,
    @SerializedName("tour") val tour: TourDetailResponse,
)
