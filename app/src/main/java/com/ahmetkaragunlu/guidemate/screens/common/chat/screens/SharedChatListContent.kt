package com.ahmetkaragunlu.guidemate.screens.common.chat.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.screens.common.chat.components.ChatListItem
import com.ahmetkaragunlu.guidemate.screens.common.chat.model.ChatUiModel


@Composable
fun SharedChatListContent(
    chatList: List<ChatUiModel>,
    onChatClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(chatList) { chatItem ->
            ChatListItem(
                chatItem = chatItem,
                onClick = { onChatClick(chatItem.id) }
            )
            HorizontalDivider(
                thickness = 0.5.dp,
                color = Color.LightGray.copy(alpha = 0.4f),
            )
        }
    }
}