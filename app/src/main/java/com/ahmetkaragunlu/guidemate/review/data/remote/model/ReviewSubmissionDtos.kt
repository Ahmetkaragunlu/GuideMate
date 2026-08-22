package com.ahmetkaragunlu.guidemate.review.data.remote.model

import com.google.gson.annotations.SerializedName

data class ReviewSubmissionRequestDto(
    @SerializedName("rating") val rating: Int,
    @SerializedName("comment") val comment: String?,
)

data class SubmittedReviewResponseDto(
    @SerializedName("reviewId") val reviewId: String,
    @SerializedName("rating") val rating: Int,
    @SerializedName("comment") val comment: String?,
    @SerializedName("submittedAt") val submittedAt: String,
)
