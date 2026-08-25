package com.ahmetkaragunlu.guidemate.chat.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.chat.presentation.content.SharedChatDetailContent
import com.ahmetkaragunlu.guidemate.chat.presentation.viewmodel.ChatDetailViewModel
import com.ahmetkaragunlu.guidemate.common.ui.components.GuideMateContentState

@Composable
fun ChatDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: ChatDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    GuideMateContentState(
        state = uiState.loadState,
        onRetry = viewModel::refresh,
        modifier = modifier,
        errorMessage = uiState.errorMessage,
    ) {
        SharedChatDetailContent(
            messages = uiState.messages,
            inputText = uiState.inputText,
            canLoadMore = uiState.canLoadMore,
            isLoadingMore = uiState.isLoadingMore,
            olderLoadFailed = uiState.olderLoadFailed,
            onTextChanged = viewModel::onTextChange,
            onSendMessage = viewModel::sendMessage,
            onLoadOlder = viewModel::loadOlderMessages,
            onRetryMessage = viewModel::retryMessage,
            modifier = modifier,
        )
    }
}
