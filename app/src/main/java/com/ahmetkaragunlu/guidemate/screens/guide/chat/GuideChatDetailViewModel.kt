package com.ahmetkaragunlu.guidemate.screens.guide.chat

import androidx.lifecycle.ViewModel
import com.ahmetkaragunlu.guidemate.screens.common.chat.model.MessageUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class GuideChatDetailViewModel
    @Inject
    constructor() : ViewModel() {
        private val _messages =
            MutableStateFlow(
                listOf(
                    MessageUiModel(
                        id = "1",
                        text = "Merhaba, tur programı belli oldu mu?",
                        time = "14:10",
                        isFromMe = false,
                    ),
                    MessageUiModel(
                        id = "2",
                        text = "Evet Hans Bey, sabah 09:00'da lobide buluşuyoruz.",
                        time = "14:12",
                        isFromMe = true,
                    ),
                    MessageUiModel(
                        id = "3",
                        text = "Harika! Yanıma ne almalıyım?",
                        time = "14:13",
                        isFromMe = false,
                    ),
                    MessageUiModel(
                        id = "4",
                        text = "Rahat bir ayakkabı ve güneş gözlüğü yeterli olacaktır.",
                        time = "14:15",
                        isFromMe = true,
                    ),
                    MessageUiModel(
                        id = "5",
                        text = "Tamamdır, teşekkürler.",
                        time = "14:16",
                        isFromMe = false,
                    ),
                ),
            )
        val messages = _messages.asStateFlow()

        private val _inputText = MutableStateFlow("")
        val inputText = _inputText.asStateFlow()

        fun onTextChange(text: String) {
            _inputText.value = text
        }

        fun sendMessage() {
            val currentText = _inputText.value
            if (currentText.isNotBlank()) {
                val newMessage =
                    MessageUiModel(
                        id = System.currentTimeMillis().toString(),
                        text = currentText,
                        time = "Şimdi",
                        isFromMe = true,
                    )
                _messages.update { it + newMessage }
                _inputText.value = ""
            }
        }
    }
