package com.ahmetkaragunlu.guidemate.reservation.data.remote.model

import com.google.gson.annotations.SerializedName

data class CancelReservationRequestDto(
    @SerializedName("version") val version: Long,
    @SerializedName("reason") val reason: String?,
)
