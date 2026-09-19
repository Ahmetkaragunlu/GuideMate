package com.ahmetkaragunlu.guidemate.notification.data.remote.model

import com.ahmetkaragunlu.guidemate.di.NetworkModule
import java.time.Instant
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class NotificationResponseDtoSerializationTest {
    private val gson = NetworkModule.provideGson()

    @Test
    fun `notification response parses ISO instants and nullable read time`() {
        val response =
            gson.fromJson(
                """
                {
                  "id": "notification-1",
                  "type": "CHAT_MESSAGE",
                  "actorId": 7,
                  "actorDisplayName": "Ada",
                  "payload": null,
                  "read": false,
                  "readAt": null,
                  "createdAt": "2026-08-31T10:15:30Z"
                }
                """.trimIndent(),
                NotificationResponseDto::class.java,
            )

        assertEquals(Instant.parse("2026-08-31T10:15:30Z"), response.createdAt)
        assertNull(response.readAt)
    }
}
