package com.ahmetkaragunlu.guidemate.screens.common.chat.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.common.chat.components.ChatListItem
import com.ahmetkaragunlu.guidemate.screens.common.chat.model.ChatUiModel

@Composable
fun SharedChatListContent(
    chatList: List<ChatUiModel>,
    onChatClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
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
            ChatListItem(
                chatItem = chatItem,
                onClick = { onChatClick(chatItem.chatId) },
            )
            HorizontalDivider(
                thickness = 0.5.dp,
                color = Color.LightGray.copy(alpha = 0.4f),
            )
        }
    }
}
