package com.ahmetkaragunlu.guidemate.chat.data.remote.model

import com.google.gson.annotations.SerializedName

data class SendChatMessageRequestDto(
    @SerializedName("clientMessageId") val clientMessageId: String,
    @SerializedName("body") val body: String,
)

data class ClearChatRequestDto(
    @SerializedName("clientRequestId") val clientRequestId: String,
)
