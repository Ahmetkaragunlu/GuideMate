package com.ahmetkaragunlu.guidemate.review.data.remote.model.response

import com.ahmetkaragunlu.guidemate.media.data.remote.model.response.MediaReferenceResponse
import com.google.gson.annotations.SerializedName

data class TourReviewResponse(
    @SerializedName("reviewId") val reviewId: String,
    @SerializedName("reviewerDisplayName") val reviewerDisplayName: String,
    @SerializedName("reviewerAvatar") val reviewerAvatar: MediaReferenceResponse?,
    @SerializedName("rating") val rating: Int,
    @SerializedName("comment") val comment: String?,
    @SerializedName("submittedAt") val submittedAt: String,
)
