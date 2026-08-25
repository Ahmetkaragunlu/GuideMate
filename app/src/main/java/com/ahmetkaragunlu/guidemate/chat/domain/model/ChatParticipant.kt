package com.ahmetkaragunlu.guidemate.chat.domain.model

data class ChatParticipant(
    val userId: Long,
    val displayName: String,
    val avatarUrl: String? = null,
)
