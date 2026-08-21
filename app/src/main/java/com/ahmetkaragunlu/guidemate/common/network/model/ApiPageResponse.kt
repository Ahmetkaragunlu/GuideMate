package com.ahmetkaragunlu.guidemate.common.network.model

import com.google.gson.annotations.SerializedName

data class ApiPageResponse<T>(
    @SerializedName("content") val content: List<T>,
    @SerializedName("page") val page: Int,
    @SerializedName("size") val size: Int,
    @SerializedName("totalElements") val totalElements: Long,
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("first") val isFirst: Boolean,
    @SerializedName("last") val isLast: Boolean,
)
