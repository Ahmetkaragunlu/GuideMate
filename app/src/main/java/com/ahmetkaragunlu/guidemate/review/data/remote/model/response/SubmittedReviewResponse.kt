package com.ahmetkaragunlu.guidemate.review.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class SubmittedReviewResponse(
    @SerializedName("reviewId") val reviewId: String,
    @SerializedName("rating") val rating: Int,
    @SerializedName("comment") val comment: String?,
    @SerializedName("submittedAt") val submittedAt: String,
)
