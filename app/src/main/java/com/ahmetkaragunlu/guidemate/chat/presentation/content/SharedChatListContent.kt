package com.ahmetkaragunlu.guidemate.chat.presentation.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.chat.presentation.components.SwipeRevealChatItem
import com.ahmetkaragunlu.guidemate.chat.presentation.model.ChatUiModel

@Composable
fun SharedChatListContent(
    chatList: List<ChatUiModel>,
    onChatClick: (String) -> Unit,
    onClearRequest: (ChatUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    var revealedChatId by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(chatList, revealedChatId) {
        if (revealedChatId != null && chatList.none { it.chatId == revealedChatId }) {
            revealedChatId = null
        }
    }

    if (chatList.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.chat_empty_history),
                style = MaterialTheme.typography.bodyMedium,
                color = colorResource(R.color.text_color),
            )
        }
        return
    }

    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(
            items = chatList,
            key = ChatUiModel::chatId,
        ) { chatItem ->
            SwipeRevealChatItem(
                chatItem = chatItem,
                isRevealed = revealedChatId == chatItem.chatId,
                onRevealChanged = { isRevealed ->
                    if (isRevealed) {
                        revealedChatId = chatItem.chatId
                    } else if (revealedChatId == chatItem.chatId) {
                        revealedChatId = null
                    }
                },
                onChatClick = { onChatClick(chatItem.chatId) },
                onClearRequest = { onClearRequest(chatItem) },
            )
            HorizontalDivider(
                thickness = 0.5.dp,
                color = Color.LightGray.copy(alpha = 0.4f),
            )
        }
    }
}
