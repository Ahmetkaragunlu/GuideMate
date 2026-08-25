package com.ahmetkaragunlu.guidemate.chat.presentation.model

import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState

data class ChatDetailUiState(
    val messages: List<MessageUiModel> = emptyList(),
    val inputText: String = "",
    val loadState: ContentLoadState = ContentLoadState.LOADING,
    val errorMessage: String? = null,
    val canLoadMore: Boolean = false,
    val isLoadingMore: Boolean = false,
    val olderLoadFailed: Boolean = false,
)
