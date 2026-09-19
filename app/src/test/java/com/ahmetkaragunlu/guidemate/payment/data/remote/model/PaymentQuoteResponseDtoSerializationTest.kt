package com.ahmetkaragunlu.guidemate.payment.data.remote.model

import com.ahmetkaragunlu.guidemate.di.NetworkModule
import java.time.Instant
import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test

class PaymentQuoteResponseDtoSerializationTest {
    private val gson = NetworkModule.provideGson()

    @Test
    fun `payment quote round trips ISO instant and local date fields`() {
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

        val restored = gson.fromJson(gson.toJson(original), PaymentQuoteResponseDto::class.java)

        assertEquals(original.rateDate, restored.rateDate)
        assertEquals(original.quotedAt, restored.quotedAt)
        assertEquals(original.expiresAt, restored.expiresAt)
    }
}
