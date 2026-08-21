package com.ahmetkaragunlu.guidemate.common.network.error

import com.google.gson.annotations.SerializedName

data class ApiErrorResponse(
    @SerializedName("code") val code: String?,
    @SerializedName("message") val message: String?,
    @SerializedName("timestamp") val timestamp: String?,
    @SerializedName("fieldErrors") val fieldErrors: List<ApiFieldErrorResponse> = emptyList(),
)

data class ApiFieldErrorResponse(
    @SerializedName("field") val field: String,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String?,
)
