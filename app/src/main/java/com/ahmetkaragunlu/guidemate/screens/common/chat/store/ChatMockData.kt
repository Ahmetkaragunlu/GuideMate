package com.ahmetkaragunlu.guidemate.screens.common.chat.store

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.domain.model.UserRole
import com.ahmetkaragunlu.guidemate.screens.common.chat.model.ChatConversation
import com.ahmetkaragunlu.guidemate.screens.common.chat.model.ChatMessage
import com.ahmetkaragunlu.guidemate.screens.common.chat.model.ChatMessageDeliveryStatus
import com.ahmetkaragunlu.guidemate.screens.common.chat.model.ChatParticipant
import java.time.Instant

internal const val MOCK_GUIDE_USER_ID = "guide-current-user"
internal const val MOCK_TOURIST_USER_ID = "tourist-current-user"

internal fun mockCurrentUserId(role: UserRole?): String? =
    when (role) {
        UserRole.GUIDE -> MOCK_GUIDE_USER_ID
        UserRole.TOURIST -> MOCK_TOURIST_USER_ID
        UserRole.ADMIN,
        null,
        -> null
    }

// Mock data is kept in one source until the backend chat repository is connected.
internal fun initialChatConversations(): List<ChatConversation> {
    val now = Instant.now()
    val currentGuide = participant(MOCK_GUIDE_USER_ID, "Ahmet Karagünlü", UserRole.GUIDE)
    val currentTourist = participant(MOCK_TOURIST_USER_ID, "Elif Demir", UserRole.TOURIST)
    val hans = participant("tourist-hans", "Hans Müller", UserRole.TOURIST)
    val john = participant("tourist-john", "John Doe", UserRole.TOURIST)
    val ahmetGuide = participant("guide-ahmet", "Ahmet Yılmaz", UserRole.GUIDE)
    val mehmetGuide = participant("guide-mehmet", "Mehmet Yılmaz", UserRole.GUIDE)
    val ayseGuide = participant("guide-ayse", "Ayşe Demir", UserRole.GUIDE)
    val fatmaGuide = participant("guide-fatma", "Fatma Kaya", UserRole.GUIDE)

    return listOf(
        conversation(
            chatId = "101",
            guide = currentGuide,
            tourist = hans,
            createdAt = now.minusSeconds(7_200),
            messages =
                listOf(
                    message("101-1", "101", hans.userId, "Merhaba, tur programı belli oldu mu?", now.minusSeconds(1_200)),
                    message(
                        "101-2",
                        "101",
                        currentGuide.userId,
                        "Evet Hans Bey, sabah 09:00'da lobide buluşuyoruz.",
                        now.minusSeconds(1_080),
                    ),
                    message("101-3", "101", hans.userId, "Harika! Yanıma ne almalıyım?", now.minusSeconds(960)),
                    message(
                        "101-4",
                        "101",
                        currentGuide.userId,
                        "Rahat bir ayakkabı ve güneş gözlüğü yeterli olacaktır.",
                        now.minusSeconds(840),
                    ),
                    message("101-5", "101", hans.userId, "Ne zaman buluşuyoruz?", now.minusSeconds(720)),
                ),
            unreadCounts = mapOf(currentGuide.userId to 1),
        ),
        conversation(
            chatId = "102",
            guide = currentGuide,
            tourist = john,
            createdAt = now.minusSeconds(172_800),
            messages =
                listOf(
                    message(
                        "102-1",
                        "102",
                        john.userId,
                        "Teşekkürler, harika turdu.",
                        now.minusSeconds(86_400),
                    ),
                ),
        ),
        conversation(
            chatId = "201",
            guide = ahmetGuide,
            tourist = currentTourist,
            createdAt = now.minusSeconds(7_200),
            messages =
                listOf(
                    message("201-1", "201", currentTourist.userId, "Merhaba, tur programı belli oldu mu?", now.minusSeconds(1_500)),
                    message(
                        "201-2",
                        "201",
                        ahmetGuide.userId,
                        "Tur sabah 09:00'da otel önünden kalkacak.",
                        now.minusSeconds(900),
                    ),
                ),
            unreadCounts = mapOf(currentTourist.userId to 2),
        ),
        conversation(
            chatId = "202",
            guide = mehmetGuide,
            tourist = currentTourist,
            createdAt = now.minusSeconds(172_800),
            messages =
                listOf(
                    message(
                        "202-1",
                        "202",
                        currentTourist.userId,
                        "Tamamdır, teşekkürler.",
                        now.minusSeconds(86_400),
                    ),
                ),
        ),
        conversation(
            chatId = "203",
            guide = ayseGuide,
            tourist = currentTourist,
            createdAt = now.minusSeconds(259_200),
            messages =
                listOf(
                    message(
                        "203-1",
                        "203",
                        ayseGuide.userId,
                        "Konum bilgisini tekrar atabilir misiniz acaba?",
                        now.minusSeconds(172_800),
                    ),
                ),
            unreadCounts = mapOf(currentTourist.userId to 5),
        ),
        conversation(
            chatId = "204",
            guide = fatmaGuide,
            tourist = currentTourist,
            createdAt = now.minusSeconds(345_600),
            messages =
                listOf(
                    message(
                        "204-1",
                        "204",
                        fatmaGuide.userId,
                        "Görüşmek üzere.",
                        now.minusSeconds(259_200),
                    ),
                ),
        ),
    )
}

private fun participant(
    userId: String,
    displayName: String,
    role: UserRole,
): ChatParticipant =
    ChatParticipant(
        userId = userId,
        displayName = displayName,
        role = role,
        avatarResId = R.drawable.example,
    )

private fun conversation(
    chatId: String,
    guide: ChatParticipant,
    tourist: ChatParticipant,
    createdAt: Instant,
    messages: List<ChatMessage>,
    unreadCounts: Map<String, Int> = emptyMap(),
): ChatConversation =
    ChatConversation(
        chatId = chatId,
        guide = guide,
        tourist = tourist,
        createdAt = createdAt,
        messages = messages,
        unreadCounts = unreadCounts,
    )

private fun message(
    messageId: String,
    chatId: String,
    senderId: String,
    text: String,
    sentAt: Instant,
): ChatMessage =
    ChatMessage(
        messageId = messageId,
        chatId = chatId,
        senderId = senderId,
        clientMessageId = "mock-client-$messageId",
        text = text,
        sentAt = sentAt,
        deliveryStatus = ChatMessageDeliveryStatus.SENT,
    )
