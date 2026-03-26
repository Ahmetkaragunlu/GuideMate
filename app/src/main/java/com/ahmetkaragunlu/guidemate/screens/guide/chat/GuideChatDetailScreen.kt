package com.ahmetkaragunlu.guidemate.screens.guide.chat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.screens.common.chat.screens.SharedChatDetailContent

@Composable
fun GuideChatDetailScreen(
    viewModel: GuideChatDetailViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val inputText by viewModel.inputText.collectAsStateWithLifecycle()

    SharedChatDetailContent(
        messages = messages,
        inputText = inputText,
        onTextChanged = viewModel::onTextChange,
        onSendMessage = viewModel::sendMessage,
        modifier = modifier,
    )
}
