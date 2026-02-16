package com.ahmetkaragunlu.guidemate.screens.common.chat.model

data class MessageUiModel(
    val id: String,
    val text: String,
    val time: String,
    val isFromMe: Boolean,
    val senderId: String = ""
)