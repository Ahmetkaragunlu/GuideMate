package com.ahmetkaragunlu.guidemate.screens.tourist.chat

import androidx.lifecycle.ViewModel
import com.ahmetkaragunlu.guidemate.screens.tourist.chat.model.MessageUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TouristChatDetailViewModel @Inject constructor() : ViewModel() {

    private val _messages = MutableStateFlow(
        listOf(
            MessageUiModel("1", "Merhaba, tur programı belli oldu mu?", "14:10", isFromMe = true),
            MessageUiModel("2", "Evet Ahmet Bey, sabah 09:00'da lobide buluşuyoruz.", "14:12", isFromMe = false),
            MessageUiModel("3", "Harika! Yanıma ne almalıyım?", "14:13", isFromMe = true),
            MessageUiModel("4", "Rahat bir ayakkabı ve güneş gözlüğü yeterli olacaktır.", "14:15", isFromMe = false),
            MessageUiModel("5", "Tamamdır, teşekkürler.", "14:16", isFromMe = true)
        )
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
            val newMessage = MessageUiModel(
                id = System.currentTimeMillis().toString(),
                text = currentText,
                time = "Şimdi",
                isFromMe = true
            )
            _messages.update { it + newMessage }
            _inputText.value = ""
        }
    }
}