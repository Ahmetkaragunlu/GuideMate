package com.ahmetkaragunlu.guidemate.common.network.serialization

import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import java.time.Instant
import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Test

class JavaTimeTypeAdaptersTest {
    private val gson =
        GsonBuilder()
            .registerTypeAdapter(Instant::class.java, InstantTypeAdapter.nullSafe())
            .registerTypeAdapter(LocalDate::class.java, LocalDateTypeAdapter.nullSafe())
            .create()

    @Test
    fun `java time values round trip ISO values and nullable instant`() {
        val original =
            JavaTimeResponse(
                occurredAt = Instant.parse("2026-08-31T10:15:30Z"),
                optionalAt = null,
                rateDate = LocalDate.parse("2026-08-31"),
            )

        val response = gson.fromJson(gson.toJson(original), JavaTimeResponse::class.java)

        assertEquals(original.occurredAt, response.occurredAt)
        assertNull(response.optionalAt)
        assertEquals(original.rateDate, response.rateDate)
    }

    @Test
    fun `string backed date field is not changed by java time adapters`() {
        val response =
            gson.fromJson(
                """{"startsAt":"2026-08-31T10:15:30Z"}""",
                StringDateResponse::class.java,
            )

        assertEquals("2026-08-31T10:15:30Z", response.startsAt)
    }

    @Test
    fun `invalid instant fails instead of using fallback`() {
        assertThrows(JsonParseException::class.java) {
            gson.fromJson(
                """
                {
                  "occurredAt": "not-an-instant",
                  "optionalAt": null,
                  "rateDate": "2026-08-31"
                }
                """.trimIndent(),
                JavaTimeResponse::class.java,
            )
        }
    }

    private data class JavaTimeResponse(
        val occurredAt: Instant,
        val optionalAt: Instant?,
        val rateDate: LocalDate,
    )

    private data class StringDateResponse(
        val startsAt: String,
    )
}
