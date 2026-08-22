package com.ahmetkaragunlu.guidemate.tour.data.remote.model

import com.ahmetkaragunlu.guidemate.media.data.remote.model.MediaReferenceResponseDto
import com.google.gson.annotations.SerializedName

data class TourSearchItemResponseDto(
    @SerializedName("tourId") val tourId: String,
    @SerializedName("sessionId") val sessionId: String,
    @SerializedName("title") val title: String,
    @SerializedName("categoryCode") val categoryCode: String,
    @SerializedName("cityName") val cityName: String,
    @SerializedName("countryCode") val countryCode: String,
    @SerializedName("cityPlaceId") val cityPlaceId: String,
    @SerializedName("startsAt") val startsAt: String,
    @SerializedName("timeZoneId") val timeZoneId: String,
    @SerializedName("durationMinutes") val durationMinutes: Int,
    @SerializedName("priceMinor") val priceMinor: Long,
    @SerializedName("currencyCode") val currencyCode: String,
    @SerializedName("availableCapacity") val availableCapacity: Int,
    @SerializedName("languageCodes") val languageCodes: List<String>,
    @SerializedName("cover") val cover: MediaReferenceResponseDto,
    @SerializedName("averageRating") val averageRating: Double,
    @SerializedName("reviewCount") val reviewCount: Long,
    @SerializedName("guide") val guide: PublicGuideSummaryResponseDto,
)
