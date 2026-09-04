package com.ahmetkaragunlu.guidemate.chat.presentation

import android.widget.Toast
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.chat.presentation.content.SharedChatListContent
import com.ahmetkaragunlu.guidemate.chat.presentation.model.ChatListUiState
import com.ahmetkaragunlu.guidemate.chat.presentation.model.ChatUiModel
import com.ahmetkaragunlu.guidemate.common.ui.components.EditAlertDialog
import com.ahmetkaragunlu.guidemate.common.ui.components.GuideMateContentState

@Composable
fun ChatListScreen(
    uiState: ChatListUiState,
    onNavigateToDetail: (String) -> Unit,
    onRetry: () -> Unit,
    onClearChat: (String) -> Unit,
    onMessageShown: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var chatPendingClear by remember { mutableStateOf<ChatUiModel?>(null) }

    LaunchedEffect(uiState.userMessage) {
        uiState.userMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            onMessageShown()
        }
    }

    GuideMateContentState(
        state = uiState.loadState,
        onRetry = onRetry,
        modifier = modifier,
        errorMessage = uiState.errorMessage,
    ) {
        SharedChatListContent(
            chatList = uiState.chats,
            onChatClick = onNavigateToDetail,
            onClearRequest = { chatPendingClear = it },
            modifier = modifier,
        )
    }

    chatPendingClear?.let { chat ->
        EditAlertDialog(
            title = R.string.clear_chat_title,
            text = R.string.clear_chat_message,
            textFormatArguments = listOf(chat.name),
            onDismissRequest = { chatPendingClear = null },
            confirmButton = {
                TextButton(
                    onClick = {
                        onClearChat(chat.chatId)
                        chatPendingClear = null
                    },
                    colors =
                        ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error,
                        ),
                ) {
                    Text(text = stringResource(R.string.clear_chat_confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { chatPendingClear = null },
                    colors =
                        ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.outline,
                        ),
                ) {
                    Text(text = stringResource(R.string.clear_chat_cancel))
                }
            },
        )
    }
}
