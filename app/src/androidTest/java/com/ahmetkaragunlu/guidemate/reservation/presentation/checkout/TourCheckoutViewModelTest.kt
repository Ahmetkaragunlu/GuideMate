package com.ahmetkaragunlu.guidemate.reservation.presentation.checkout

import androidx.lifecycle.SavedStateHandle
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.payment.domain.model.CheckoutCurrencies
import com.ahmetkaragunlu.guidemate.payment.domain.model.CheckoutCurrency
import com.ahmetkaragunlu.guidemate.payment.domain.model.CheckoutLocale
import com.ahmetkaragunlu.guidemate.payment.domain.model.Payment
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentMethod
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentPurpose
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentQuote
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentStatus
import com.ahmetkaragunlu.guidemate.payment.domain.repository.PaymentRepository
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuidePublicSummary
import com.ahmetkaragunlu.guidemate.tour.domain.model.Tour
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourApprovalStatus
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourDetails
import com.ahmetkaragunlu.guidemate.tour.domain.model.catalog.TourWithSession
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import com.ahmetkaragunlu.guidemate.tour.domain.model.discovery.TourSearchItem
import com.ahmetkaragunlu.guidemate.tour.domain.model.discovery.TourSearchQuery
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.TourSession
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.TourSessionStatus
import com.ahmetkaragunlu.guidemate.tour.domain.repository.TourDiscoveryRepository
import com.ahmetkaragunlu.guidemate.wallet.domain.model.WalletAccount
import com.ahmetkaragunlu.guidemate.wallet.domain.model.WalletTransaction
import com.ahmetkaragunlu.guidemate.wallet.domain.repository.WalletRepository
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TourCheckoutViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `checkout requires terms before requesting quote or payment`() =
        runTest(mainDispatcherRule.dispatcher) {
            val paymentRepository = FakePaymentRepository()
            val viewModel = createViewModel(paymentRepository)
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.uiState.collect()
            }
            advanceUntilIdle()

            assertEquals(ContentLoadState.CONTENT, viewModel.uiState.value.loadState)
            viewModel.continueCheckout()
            advanceUntilIdle()

            assertEquals(R.string.checkout_error_terms_required, viewModel.uiState.value.validationErrorResId)
            assertEquals(0, paymentRepository.quoteCalls)
            assertEquals(0, paymentRepository.checkoutCalls)
        }

    @Test
    fun `hosted card requests quote before initializing payment`() =
        runTest(mainDispatcherRule.dispatcher) {
            val paymentRepository = FakePaymentRepository()
            val viewModel = createViewModel(paymentRepository)
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.uiState.collect()
            }
            advanceUntilIdle()
            viewModel.onTermsAcceptedChange(true)
            advanceUntilIdle()

            viewModel.continueCheckout()
            advanceUntilIdle()

            assertEquals(1, paymentRepository.quoteCalls)
            assertEquals(0, paymentRepository.checkoutCalls)
            assertNotNull(viewModel.uiState.value.quote)

            viewModel.continueCheckout()
            advanceUntilIdle()

            assertEquals(1, paymentRepository.checkoutCalls)
            assertEquals("payment-1", viewModel.uiState.value.paymentLaunch?.paymentId)
            assertNull(viewModel.uiState.value.validationErrorResId)
        }

    private fun createViewModel(paymentRepository: PaymentRepository): TourCheckoutViewModel =
        TourCheckoutViewModel(
            savedStateHandle = SavedStateHandle(mapOf("sessionId" to "session-1")),
            tourRepository = FakeTourRepository(),
            walletRepository = FakeWalletRepository(),
            paymentRepository = paymentRepository,
            resourceProvider = FakeResourceProvider(),
        )

    private class FakePaymentRepository : PaymentRepository {
        override val pendingPaymentId: Flow<String?> = MutableStateFlow(null)
        var quoteCalls = 0
        var checkoutCalls = 0

        override suspend fun getCheckoutCurrencies(): DataResult<CheckoutCurrencies> =
            DataResult.Success(
                CheckoutCurrencies(
                    baseCurrencyCode = "USD",
                    chargeCurrencies = listOf(CheckoutCurrency("TRY", 2)),
                ),
            )

        override suspend fun quoteTour(
            sessionId: String,
            participantCount: Int,
            chargeCurrencyCode: String,
        ): DataResult<PaymentQuote> {
            quoteCalls++
            return DataResult.Success(quote())
        }

        override suspend fun checkoutTour(
            sessionId: String,
            participantCount: Int,
            method: PaymentMethod,
            quoteId: String?,
            locale: CheckoutLocale,
            idempotencyKey: String,
        ): DataResult<Payment> {
            checkoutCalls++
            return DataResult.Success(payment())
        }

        override suspend fun quoteWalletTopUp(
            amountMinor: Long,
            chargeCurrencyCode: String,
        ): DataResult<PaymentQuote> = error("Not used")

        override suspend fun checkoutWalletTopUp(
            quoteId: String,
            locale: CheckoutLocale,
            idempotencyKey: String,
        ): DataResult<Payment> = error("Not used")

        override suspend fun getPayment(paymentId: String): DataResult<Payment> = error("Not used")

        override suspend fun cancelPayment(paymentId: String): DataResult<Payment> = error("Not used")

        override suspend fun clearPendingPayment(paymentId: String) = Unit

        private fun quote(): PaymentQuote =
            PaymentQuote(
                id = "quote-1",
                purpose = PaymentPurpose.TOUR_BOOKING,
                baseAmountMinor = 10_000,
                baseCurrencyCode = "USD",
                chargeAmountMinor = 325_000,
                chargeCurrencyCode = "TRY",
                fxRate = BigDecimal("32.5"),
                rateSource = "TEST",
                rateDate = LocalDate.parse("2026-08-26"),
                quotedAt = NOW,
                expiresAt = Instant.parse("2099-01-01T11:00:00Z"),
            )

        private fun payment(): Payment =
            Payment(
                id = "payment-1",
                purpose = PaymentPurpose.TOUR_BOOKING,
                method = PaymentMethod.HOSTED_CARD,
                status = PaymentStatus.REQUIRES_ACTION,
                amountMinor = 10_000,
                currencyCode = "USD",
                quoteId = "quote-1",
                chargeAmountMinor = 325_000,
                chargeCurrencyCode = "TRY",
                fxRate = BigDecimal("32.5"),
                fxRateSource = "TEST",
                fxQuotedAt = NOW,
                paymentPageUrl = "https://sandbox.iyzipay.com/checkout",
                expiresAt = null,
                reservationId = null,
                reservationStatus = null,
                refundId = null,
                refundStatus = null,
                refundAmountMinor = null,
                refundChargeAmountMinor = null,
                refundChargeCurrencyCode = null,
                failureCode = null,
                createdAt = NOW,
                updatedAt = NOW,
            )
    }

    private class FakeTourRepository : TourDiscoveryRepository {
        override suspend fun getSession(sessionId: String): DataResult<TourWithSession> =
            DataResult.Success(bookableTour())

        override suspend fun searchTours(
            query: TourSearchQuery,
            page: Int,
            size: Int,
        ): DataResult<PagedResult<TourSearchItem>> = error("Not used")

        override suspend fun getPopularTours(
            page: Int,
            size: Int,
        ): DataResult<PagedResult<TourSearchItem>> = error("Not used")

        override suspend fun getPopularToursForGuide(
            guideId: Long,
            page: Int,
            size: Int,
        ): DataResult<PagedResult<TourSearchItem>> = error("Not used")

        override suspend fun getTour(tourId: String): DataResult<TourDetails> = error("Not used")
    }

    private class FakeWalletRepository : WalletRepository {
        override suspend fun getWallet(): DataResult<WalletAccount> =
            DataResult.Success(
                WalletAccount(
                    balanceMinor = 50_000,
                    availableBalanceMinor = 50_000,
                    currencyCode = "USD",
                ),
            )

        override suspend fun getTransactions(
            page: Int,
            size: Int,
        ): DataResult<PagedResult<WalletTransaction>> = error("Not used")
    }

    private class FakeResourceProvider : ResourceProvider {
        override fun getString(id: Int): String = "error"

        override fun getString(id: Int, vararg args: Any): String = "error"

        override fun getQuantityString(id: Int, quantity: Int, vararg args: Any): String = "error"
    }

    private companion object {
        val NOW: Instant = Instant.parse("2026-08-26T10:00:00Z")

        fun bookableTour(): TourWithSession =
            TourWithSession(
                tour =
                    Tour(
                        id = "tour-1",
                        guide = GuidePublicSummary("guide-1", "Guide Name"),
                        title = "City Walk",
                        description = "Description",
                        country = "Turkiye",
                        city = "Istanbul",
                        timeZoneId = "Europe/Istanbul",
                        category = TourCategory.CULTURE,
                        languages = emptyList(),
                        approvalStatus = TourApprovalStatus.APPROVED,
                    ),
                session =
                    TourSession(
                        id = "session-1",
                        tourId = "tour-1",
                        meetingPoint = "Square",
                        startsAt = Instant.parse("2099-01-01T12:00:00Z"),
                        durationMinutes = 120,
                        priceMinor = 10_000,
                        capacity = 10,
                        bookedCount = 2,
                        status = TourSessionStatus.OPEN_FOR_BOOKING,
                    ),
            )
    }
}
