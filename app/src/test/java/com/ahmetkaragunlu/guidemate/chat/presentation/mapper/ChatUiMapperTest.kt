package com.ahmetkaragunlu.guidemate.chat.presentation.mapper

import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatConversation
import com.ahmetkaragunlu.guidemate.chat.domain.model.ChatParticipant
import java.time.Instant
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ChatUiMapperTest {
    @Test
    fun `tourist chat preserves guide identity for profile navigation`() {
        val conversation =
            ChatConversation(
                chatId = "chat-1",
                guide =
                    ChatParticipant(
                        userId = 41L,
                        displayName = "Ada Guide",
                        avatarUrl = "https://example.com/guide.jpg",
                    ),
                tourist = ChatParticipant(userId = 7L, displayName = "Tourist"),
                lastMessage = null,
                unreadCount = 0,
                createdAt = Instant.parse("2026-01-01T00:00:00Z"),
                lastActivityAt = Instant.parse("2026-01-01T00:00:00Z"),
            )

        val uiModel = conversation.toChatUiModel(currentUserId = 7L)

        assertNotNull(uiModel)
        assertEquals(41L, uiModel?.remoteUserId)
        assertEquals("Ada Guide", uiModel?.name)
        assertEquals("https://example.com/guide.jpg", uiModel?.avatarUrl)
    }
}
