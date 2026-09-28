package com.ahmetkaragunlu.guidemate.chat.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class SendChatMessageRequest(
    @SerializedName("clientMessageId") val clientMessageId: String,
    @SerializedName("body") val body: String,
)

data class ClearChatRequest(
    @SerializedName("clientRequestId") val clientRequestId: String,
)
