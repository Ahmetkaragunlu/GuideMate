package com.ahmetkaragunlu.guidemate.chat.data.mapper

import com.ahmetkaragunlu.guidemate.chat.data.remote.model.ChatConversationResponseDto
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.ChatMessageResponseDto
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.ChatParticipantResponseDto
import java.time.Instant
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ChatMapperTest {
    @Test
    fun `maps conversation projection without inventing participant data`() {
        val sentAt = Instant.parse("2026-08-25T10:15:30Z")
        val response =
            ChatConversationResponseDto(
                chatId = "chat-1",
                guide = ChatParticipantResponseDto(10, "Ahmet Karagünlü", null),
                tourist = ChatParticipantResponseDto(20, "Elif Demir", "https://example.com/a.jpg"),
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

    private fun messageResponse(sentAt: Instant): ChatMessageResponseDto =
        ChatMessageResponseDto(
            messageId = "message-1",
            chatId = "chat-1",
            senderId = 20,
            clientMessageId = "client-1",
            body = "Merhaba",
            sentAt = sentAt,
            deliveryStatus = "SENT",
        )
}
