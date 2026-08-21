package com.ahmetkaragunlu.guidemate.chat.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.auth.domain.model.UserRole
import com.ahmetkaragunlu.guidemate.chat.presentation.content.SharedChatDetailContent
import com.ahmetkaragunlu.guidemate.chat.presentation.viewmodel.ChatDetailViewModel

@Composable
fun ChatDetailScreen(
    viewerRole: UserRole,
    modifier: Modifier = Modifier,
    viewModel: ChatDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewerRole) {
        viewModel.setViewerRole(viewerRole)
    }

    SharedChatDetailContent(
        messages = uiState.messages,
        inputText = uiState.inputText,
        onTextChanged = viewModel::onTextChange,
        onSendMessage = viewModel::sendMessage,
        modifier = modifier,
    )
}
