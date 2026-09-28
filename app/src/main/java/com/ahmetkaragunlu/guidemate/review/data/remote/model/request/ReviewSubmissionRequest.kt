package com.ahmetkaragunlu.guidemate.review.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class ReviewSubmissionRequest(
    @SerializedName("rating") val rating: Int,
    @SerializedName("comment") val comment: String?,
)
