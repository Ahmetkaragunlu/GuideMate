package com.ahmetkaragunlu.guidemate.screens.tourist.chat


import androidx.lifecycle.ViewModel
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.tourist.chat.model.ChatUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TouristChatListViewModel @Inject constructor() : ViewModel() {

   //Mock Data
    private val _chatList = MutableStateFlow(
        listOf(
            ChatUiModel(
                id = "1",
                name = "Ahmet Rehber",
                lastMessage = "Tur sabah 09:00'da otel önünden kalkacak.",
                time = "14:30",
                avatarResId = R.drawable.example,
                unreadCount = 2
            ),
            ChatUiModel(
                id = "2",
                name = "Mehmet Yılmaz",
                lastMessage = "Tamamdır, teşekkürler.",
                time = "Dün",
                avatarResId = R.drawable.example,
                unreadCount = 0
            ),
            ChatUiModel(
                id = "3",
                name = "Ayşe Demir",
                lastMessage = "Konum bilgisini tekrar atabilir misiniz acaba?",
                time = "Pzt",
                avatarResId = R.drawable.example,
                unreadCount = 5
            ),
            ChatUiModel(
                id = "4",
                name = "Fatma Kaya",
                lastMessage = "Görüşmek üzere.",
                time = "12.05",
                avatarResId = R.drawable.example,
                unreadCount = 0
            )
        )
    )
    val chatList = _chatList.asStateFlow()
}