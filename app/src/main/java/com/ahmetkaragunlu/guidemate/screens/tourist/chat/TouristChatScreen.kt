package com.ahmetkaragunlu.guidemate.screens.tourist.chat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.screens.common.chat.content.SharedChatListContent

@Composable
fun TouristChatScreen(
    viewModel: TouristChatViewModel = hiltViewModel(),
    onNavigateToDetail: (String) -> Unit,
) {
    val chatList by viewModel.chatList.collectAsStateWithLifecycle()

    SharedChatListContent(
        chatList = chatList,
        onChatClick = onNavigateToDetail,
    )
}
