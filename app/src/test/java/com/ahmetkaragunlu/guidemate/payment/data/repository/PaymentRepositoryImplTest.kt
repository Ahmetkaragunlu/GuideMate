package com.ahmetkaragunlu.guidemate.payment.data.repository

import com.ahmetkaragunlu.guidemate.common.network.testApiCallExecutor
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.payment.data.local.PendingPaymentStorage
import com.ahmetkaragunlu.guidemate.payment.data.remote.api.PaymentApi
import com.ahmetkaragunlu.guidemate.payment.data.remote.model.CheckoutCurrenciesResponseDto
import com.ahmetkaragunlu.guidemate.payment.data.remote.model.CheckoutCurrencyOptionResponseDto
import com.ahmetkaragunlu.guidemate.payment.data.remote.model.PaymentQuoteResponseDto
import com.ahmetkaragunlu.guidemate.payment.data.remote.model.PaymentResponseDto
import com.ahmetkaragunlu.guidemate.payment.data.remote.model.TourCheckoutRequestDto
import com.ahmetkaragunlu.guidemate.payment.data.remote.model.TourPaymentQuoteRequestDto
import com.ahmetkaragunlu.guidemate.payment.data.remote.model.WalletTopUpQuoteRequestDto
import com.ahmetkaragunlu.guidemate.payment.data.remote.model.WalletTopUpRequestDto
import com.ahmetkaragunlu.guidemate.payment.domain.model.CheckoutLocale
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentMethod
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentStatus
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class PaymentRepositoryImplTest {
    @Test
    fun `hosted checkout forwards canonical request and stores pending payment`() = runBlocking {
        val api = FakePaymentApi()
        val storage = FakePendingPaymentStorage()
        val repository = createRepository(api, storage)

        val result =
            repository.checkoutTour(
                sessionId = "session-1",
                participantCount = 2,
                method = PaymentMethod.HOSTED_CARD,
                quoteId = "quote-1",
                locale = CheckoutLocale.TR,
                idempotencyKey = "idempotency-1",
            )

        assertTrue(result is DataResult.Success)
        assertEquals("idempotency-1", api.checkoutIdempotencyKey)
        assertEquals("session-1", api.checkoutRequest?.sessionId)
        assertEquals(2, api.checkoutRequest?.participantCount)
        assertEquals("HOSTED_CARD", api.checkoutRequest?.method)
        assertEquals("quote-1", api.checkoutRequest?.quoteId)
        assertEquals("TR", api.checkoutRequest?.locale)
        assertEquals("payment-1", storage.value.value)
    }

    @Test
    fun `pending recovery is cleared only after canonical UI verification`() = runBlocking {
        val api = FakePaymentApi(paymentStatus = "SUCCEEDED", reservationStatus = "CONFIRMED")
        val storage = FakePendingPaymentStorage(initialPaymentId = "payment-1")
        val repository = createRepository(api, storage)

        val result = repository.getPayment("payment-1")
        repository.clearPendingPayment("payment-1")

        assertTrue(result is DataResult.Success)
        assertEquals(PaymentStatus.SUCCEEDED, (result as DataResult.Success).data.status)
        assertNull(storage.value.value)
    }

    @Test
    fun `maps backend currency and quote without recalculating on Android`() = runBlocking {
        val repository = createRepository(FakePaymentApi(), FakePendingPaymentStorage())

        val currencies = repository.getCheckoutCurrencies()
        val quote = repository.quoteWalletTopUp(amountMinor = 10_000, chargeCurrencyCode = "TRY")

        assertTrue(currencies is DataResult.Success)
        assertEquals("USD", (currencies as DataResult.Success).data.baseCurrencyCode)
        assertEquals("TRY", currencies.data.chargeCurrencies.single().currencyCode)
        assertTrue(quote is DataResult.Success)
        assertEquals(325_000L, (quote as DataResult.Success).data.chargeAmountMinor)
        assertEquals("TRY", quote.data.chargeCurrencyCode)
    }

    private fun createRepository(
        api: PaymentApi,
        storage: PendingPaymentStorage,
    ): PaymentRepositoryImpl =
        PaymentRepositoryImpl(
            api = api,
            pendingPaymentStorage = storage,
            apiCallExecutor = testApiCallExecutor(),
        )

    private class FakePendingPaymentStorage(initialPaymentId: String? = null) :
        PendingPaymentStorage {
        val value = MutableStateFlow(initialPaymentId)
        override val paymentId: Flow<String?> = value

        override suspend fun save(paymentId: String) {
            value.value = paymentId
        }

        override suspend fun clear(paymentId: String) {
            if (value.value == paymentId) value.value = null
        }
    }

    private class FakePaymentApi(
        private val paymentStatus: String = "REQUIRES_ACTION",
        private val reservationStatus: String? = "PENDING_PAYMENT",
    ) : PaymentApi {
        var checkoutIdempotencyKey: String? = null
        var checkoutRequest: TourCheckoutRequestDto? = null

        override suspend fun getCheckoutCurrencies(): Response<CheckoutCurrenciesResponseDto> =
            Response.success(
                CheckoutCurrenciesResponseDto(
                    baseCurrencyCode = "USD",
                    chargeCurrencies =
                        listOf(
                            CheckoutCurrencyOptionResponseDto(
                                currencyCode = "TRY",
                                fractionDigits = 2,
                            ),
                        ),
                ),
            )

        override suspend fun quoteTour(
            request: TourPaymentQuoteRequestDto,
        ): Response<PaymentQuoteResponseDto> = Response.success(quoteResponse())

        override suspend fun quoteWalletTopUp(
            request: WalletTopUpQuoteRequestDto,
        ): Response<PaymentQuoteResponseDto> = Response.success(quoteResponse())

        override suspend fun checkoutTour(
            idempotencyKey: String,
            request: TourCheckoutRequestDto,
        ): Response<PaymentResponseDto> {
            checkoutIdempotencyKey = idempotencyKey
            checkoutRequest = request
            return Response.success(paymentResponse())
        }

        override suspend fun checkoutWalletTopUp(
            idempotencyKey: String,
            request: WalletTopUpRequestDto,
        ): Response<PaymentResponseDto> = Response.success(paymentResponse())

        override suspend fun getPayment(paymentId: String): Response<PaymentResponseDto> =
            Response.success(paymentResponse())

        override suspend fun cancelPayment(paymentId: String): Response<PaymentResponseDto> =
            Response.success(paymentResponse(status = "CANCELLED"))

        private fun quoteResponse(): PaymentQuoteResponseDto =
            PaymentQuoteResponseDto(
                quoteId = "quote-1",
                purpose = "WALLET_TOP_UP",
                baseAmountMinor = 10_000,
                baseCurrencyCode = "USD",
                chargeAmountMinor = 325_000,
                chargeCurrencyCode = "TRY",
                fxRate = BigDecimal("32.5"),
                rateSource = "TEST",
                rateDate = LocalDate.parse("2026-08-23"),
                quotedAt = Instant.parse("2026-08-23T10:00:00Z"),
                expiresAt = Instant.parse("2026-08-23T10:10:00Z"),
            )

        private fun paymentResponse(status: String = paymentStatus): PaymentResponseDto =
            PaymentResponseDto(
                paymentId = "payment-1",
                purpose = "TOUR_BOOKING",
                method = "HOSTED_CARD",
                paymentStatus = status,
                amountMinor = 10_000,
                currencyCode = "USD",
                quoteId = "quote-1",
                chargeAmountMinor = 325_000,
                chargeCurrencyCode = "TRY",
                fxRate = BigDecimal("32.5"),
                fxRateSource = "TEST",
                fxQuotedAt = Instant.parse("2026-08-23T10:00:00Z"),
                paymentPageUrl = "https://sandbox.iyzipay.com/checkout",
                expiresAt = Instant.parse("2026-08-23T10:30:00Z"),
                reservationId = "reservation-1",
                reservationStatus = reservationStatus,
                refundId = null,
                refundStatus = null,
                refundAmountMinor = null,
                refundChargeAmountMinor = null,
                refundChargeCurrencyCode = null,
                failureCode = null,
                createdAt = Instant.parse("2026-08-23T10:00:00Z"),
                updatedAt = Instant.parse("2026-08-23T10:00:01Z"),
            )
    }
}
