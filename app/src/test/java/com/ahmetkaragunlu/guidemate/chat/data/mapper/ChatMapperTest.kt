package com.ahmetkaragunlu.guidemate.chat.data.mapper

import com.ahmetkaragunlu.guidemate.chat.data.remote.model.response.ChatConversationResponse
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.response.ChatMessageResponse
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.response.ChatParticipantResponse
import java.time.Instant
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ChatMapperTest {
    @Test
    fun `maps conversation projection without inventing participant data`() {
        val sentAt = Instant.parse("2026-08-25T10:15:30Z")
        val response =
            ChatConversationResponse(
                chatId = "chat-1",
                guide = ChatParticipantResponse(10, "Ahmet Karagünlü", null),
                tourist = ChatParticipantResponse(20, "Elif Demir", "https://example.com/a.jpg"),
                lastMessage = messageResponse(sentAt),
                unreadCount = 3,
                createdAt = sentAt.minusSeconds(60),
                lastActivityAt = sentAt,
            )

        val conversation = response.toDomain()

        assertEquals(10L, conversation.guide.userId)
        assertNull(conversation.guide.avatarUrl)
        assertEquals("https://example.com/a.jpg", conversation.tourist.avatarUrl)
        assertEquals(3, conversation.unreadCount)
        assertEquals("Merhaba", conversation.lastMessage?.text)
    }

    private fun messageResponse(sentAt: Instant): ChatMessageResponse =
        ChatMessageResponse(
            messageId = "message-1",
            chatId = "chat-1",
            senderId = 20,
            clientMessageId = "client-1",
            body = "Merhaba",
            sentAt = sentAt,
            deliveryStatus = "SENT",
        )
}
