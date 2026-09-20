package com.ahmetkaragunlu.guidemate.review.data.remote.model

import com.google.gson.annotations.SerializedName

data class ReviewSubmissionRequestDto(
    @SerializedName("rating") val rating: Int,
    @SerializedName("comment") val comment: String?,
)
