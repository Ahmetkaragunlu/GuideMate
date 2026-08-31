package com.ahmetkaragunlu.guidemate.common.network.serialization

import com.ahmetkaragunlu.guidemate.di.NetworkModule
import com.ahmetkaragunlu.guidemate.notification.data.remote.model.NotificationResponseDto
import com.ahmetkaragunlu.guidemate.payment.data.remote.model.PaymentQuoteResponseDto
import com.google.gson.JsonParseException
import java.time.Instant
import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Test

class JavaTimeTypeAdaptersTest {
    private val gson = NetworkModule.provideGson()

    @Test
    fun notificationResponse_parsesIsoInstantAndNullableInstant() {
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

    @Test
    fun paymentQuote_roundTripsIsoInstantAndLocalDate() {
        val original =
            PaymentQuoteResponseDto(
                quoteId = "quote-1",
                purpose = "TOUR_BOOKING",
                baseAmountMinor = 10_000,
                baseCurrencyCode = "USD",
                chargeAmountMinor = 410_000,
                chargeCurrencyCode = "TRY",
                fxRate = "41.0".toBigDecimal(),
                rateSource = "TEST",
                rateDate = LocalDate.parse("2026-08-31"),
                quotedAt = Instant.parse("2026-08-31T10:15:30Z"),
                expiresAt = Instant.parse("2026-08-31T10:20:30Z"),
            )

        val restored =
            gson.fromJson(gson.toJson(original), PaymentQuoteResponseDto::class.java)

        assertEquals(original.rateDate, restored.rateDate)
        assertEquals(original.quotedAt, restored.quotedAt)
        assertEquals(original.expiresAt, restored.expiresAt)
    }

    @Test
    fun stringBackedDateField_isNotChangedByJavaTimeAdapters() {
        val response =
            gson.fromJson(
                """{"startsAt":"2026-08-31T10:15:30Z"}""",
                StringDateResponse::class.java,
            )

        assertEquals("2026-08-31T10:15:30Z", response.startsAt)
    }

    @Test
    fun invalidInstant_failsInsteadOfUsingFallback() {
        assertThrows(JsonParseException::class.java) {
            gson.fromJson(
                """
                {
                  "id": "notification-1",
                  "type": "CHAT_MESSAGE",
                  "actorId": null,
                  "actorDisplayName": null,
                  "payload": null,
                  "read": false,
                  "readAt": null,
                  "createdAt": "not-an-instant"
                }
                """.trimIndent(),
                NotificationResponseDto::class.java,
            )
        }
    }

    private data class StringDateResponse(
        val startsAt: String,
    )
}
