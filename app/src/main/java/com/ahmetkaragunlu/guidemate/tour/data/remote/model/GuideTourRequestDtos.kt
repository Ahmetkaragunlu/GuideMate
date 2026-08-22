package com.ahmetkaragunlu.guidemate.tour.data.remote.model

import com.google.gson.annotations.SerializedName

data class TourContentRequestDto(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("countryCode") val countryCode: String,
    @SerializedName("cityPlaceId") val cityPlaceId: String,
    @SerializedName("cityName") val cityName: String,
    @SerializedName("timeZoneId") val timeZoneId: String,
    @SerializedName("categoryCode") val categoryCode: String,
    @SerializedName("languageCodes") val languageCodes: List<String>,
    @SerializedName("coverMediaId") val coverMediaId: String,
)

data class TourSessionRequestDto(
    @SerializedName("meetingPoint") val meetingPoint: String,
    @SerializedName("startsAt") val startsAt: String,
    @SerializedName("durationMinutes") val durationMinutes: Int,
    @SerializedName("priceMinor") val priceMinor: Long,
    @SerializedName("capacity") val capacity: Int,
)

data class CreateGuideTourRequestDto(
    @SerializedName("tour") val tour: TourContentRequestDto,
    @SerializedName("session") val session: TourSessionRequestDto,
)

data class SubmitTourChangeRequestDto(
    @SerializedName("baseVersion") val baseVersion: Long,
    @SerializedName("proposedTour") val proposedTour: TourContentRequestDto,
)

data class UpdateTourSessionRequestDto(
    @SerializedName("version") val version: Long,
    @SerializedName("meetingPoint") val meetingPoint: String,
    @SerializedName("startsAt") val startsAt: String,
    @SerializedName("durationMinutes") val durationMinutes: Int,
    @SerializedName("priceMinor") val priceMinor: Long,
    @SerializedName("capacity") val capacity: Int,
)

data class CancelTourSessionRequestDto(
    @SerializedName("reason") val reason: String,
)
