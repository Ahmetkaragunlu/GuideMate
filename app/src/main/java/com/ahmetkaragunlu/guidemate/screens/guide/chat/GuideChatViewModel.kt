package com.ahmetkaragunlu.guidemate.screens.guide.chat

import androidx.lifecycle.ViewModel
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.common.chat.model.ChatUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class GuideChatViewModel @Inject constructor() : ViewModel() {


    //Mock Data
    private val _chatList = MutableStateFlow(
        listOf(
            ChatUiModel(
                id = "101", remoteUserId = "t1", name = "Hans Müller (Turist)",
                lastMessage = "Ne zaman buluşuyoruz?", time = "10:30",
                avatarResId = R.drawable.example, unreadCount = 1
            ),
            ChatUiModel(
                id = "102", remoteUserId = "t2", name = "John Doe",
                lastMessage = "Teşekkürler, harika turdu.", time = "Dün",
                avatarResId = R.drawable.example, unreadCount = 0
            )
        )
    )
    val chatList = _chatList.asStateFlow()
}