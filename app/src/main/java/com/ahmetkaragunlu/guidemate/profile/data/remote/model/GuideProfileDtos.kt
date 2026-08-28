package com.ahmetkaragunlu.guidemate.profile.data.remote.model

import com.ahmetkaragunlu.guidemate.media.data.remote.model.MediaReferenceResponseDto
import com.google.gson.annotations.SerializedName

data class GuideProfileResponseDto(
    @SerializedName("guideId") val guideId: Long,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("displayName") val displayName: String,
    @SerializedName("specialtyTitle") val specialtyTitle: String,
    @SerializedName("biography") val biography: String,
    @SerializedName("languageCodes") val languageCodes: List<String>,
    @SerializedName("avatar") val avatar: MediaReferenceResponseDto?,
    @SerializedName("performance") val performance: GuidePerformanceResponseDto,
)

data class GuidePerformanceResponseDto(
    @SerializedName("completedSessionCount") val completedSessionCount: Long,
    @SerializedName("totalParticipantCount") val totalParticipantCount: Long,
    @SerializedName("averageRating") val averageRating: Double,
    @SerializedName("reviewCount") val reviewCount: Long,
    @SerializedName("level") val level: String,
)

data class GuideSearchItemResponseDto(
    @SerializedName("guideId") val guideId: Long,
    @SerializedName("displayName") val displayName: String,
    @SerializedName("specialtyTitle") val specialtyTitle: String,
    @SerializedName("avatar") val avatar: MediaReferenceResponseDto?,
    @SerializedName("languageCodes") val languageCodes: List<String>,
    @SerializedName("completedSessionCount") val completedSessionCount: Long,
    @SerializedName("totalParticipantCount") val totalParticipantCount: Long,
    @SerializedName("averageRating") val averageRating: Double,
    @SerializedName("reviewCount") val reviewCount: Long,
    @SerializedName("level") val level: String,
)

data class UpdateGuideProfileRequestDto(
    @SerializedName("specialtyTitle") val specialtyTitle: String,
    @SerializedName("biography") val biography: String,
    @SerializedName("languageCodes") val languageCodes: List<String>,
)
