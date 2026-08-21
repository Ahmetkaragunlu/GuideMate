package com.ahmetkaragunlu.guidemate.chat.presentation.model

data class ChatDetailUiState(
    val messages: List<MessageUiModel> = emptyList(),
    val inputText: String = "",
)
