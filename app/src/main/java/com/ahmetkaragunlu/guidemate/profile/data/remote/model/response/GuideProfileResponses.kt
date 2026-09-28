package com.ahmetkaragunlu.guidemate.profile.data.remote.model.response

import com.ahmetkaragunlu.guidemate.media.data.remote.model.response.MediaReferenceResponse
import com.google.gson.annotations.SerializedName

data class GuideProfileResponse(
    @SerializedName("guideId") val guideId: Long,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("displayName") val displayName: String,
    @SerializedName("specialtyTitle") val specialtyTitle: String,
    @SerializedName("biography") val biography: String,
    @SerializedName("languageCodes") val languageCodes: List<String>,
    @SerializedName("avatar") val avatar: MediaReferenceResponse?,
    @SerializedName("performance") val performance: GuidePerformanceResponse,
)

data class GuidePerformanceResponse(
    @SerializedName("completedSessionCount") val completedSessionCount: Long,
    @SerializedName("totalParticipantCount") val totalParticipantCount: Long,
    @SerializedName("averageRating") val averageRating: Double,
    @SerializedName("reviewCount") val reviewCount: Long,
    @SerializedName("level") val level: String,
)

data class GuideSearchItemResponse(
    @SerializedName("guideId") val guideId: Long,
    @SerializedName("displayName") val displayName: String,
    @SerializedName("specialtyTitle") val specialtyTitle: String,
    @SerializedName("avatar") val avatar: MediaReferenceResponse?,
    @SerializedName("languageCodes") val languageCodes: List<String>,
    @SerializedName("completedSessionCount") val completedSessionCount: Long,
    @SerializedName("totalParticipantCount") val totalParticipantCount: Long,
    @SerializedName("averageRating") val averageRating: Double,
    @SerializedName("reviewCount") val reviewCount: Long,
    @SerializedName("level") val level: String,
)
