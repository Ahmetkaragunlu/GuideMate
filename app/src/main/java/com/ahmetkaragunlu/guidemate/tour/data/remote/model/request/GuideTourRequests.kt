package com.ahmetkaragunlu.guidemate.tour.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class TourContentRequest(
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

data class TourSessionRequest(
    @SerializedName("meetingPoint") val meetingPoint: String,
    @SerializedName("startsAt") val startsAt: String,
    @SerializedName("durationMinutes") val durationMinutes: Int,
    @SerializedName("priceMinor") val priceMinor: Long,
    @SerializedName("capacity") val capacity: Int,
)

data class CreateGuideTourRequest(
    @SerializedName("tour") val tour: TourContentRequest,
    @SerializedName("session") val session: TourSessionRequest,
)

data class SubmitTourChangeRequest(
    @SerializedName("baseVersion") val baseVersion: Long,
    @SerializedName("proposedTour") val proposedTour: TourContentRequest,
)

data class UpdateTourSessionRequest(
    @SerializedName("version") val version: Long,
    @SerializedName("meetingPoint") val meetingPoint: String,
    @SerializedName("startsAt") val startsAt: String,
    @SerializedName("durationMinutes") val durationMinutes: Int,
    @SerializedName("priceMinor") val priceMinor: Long,
    @SerializedName("capacity") val capacity: Int,
)

data class CancelTourSessionRequest(
    @SerializedName("reason") val reason: String,
)
