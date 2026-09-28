package com.ahmetkaragunlu.guidemate.reservation.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class CancelReservationRequest(
    @SerializedName("version") val version: Long,
    @SerializedName("reason") val reason: String?,
)
