package com.ahmetkaragunlu.guidemate.review.data.remote.model

import com.ahmetkaragunlu.guidemate.media.data.remote.model.MediaReferenceResponseDto
import com.google.gson.annotations.SerializedName

data class TourReviewResponseDto(
    @SerializedName("reviewId") val reviewId: String,
    @SerializedName("reviewerDisplayName") val reviewerDisplayName: String,
    @SerializedName("reviewerAvatar") val reviewerAvatar: MediaReferenceResponseDto?,
    @SerializedName("rating") val rating: Int,
    @SerializedName("comment") val comment: String?,
    @SerializedName("submittedAt") val submittedAt: String,
)
