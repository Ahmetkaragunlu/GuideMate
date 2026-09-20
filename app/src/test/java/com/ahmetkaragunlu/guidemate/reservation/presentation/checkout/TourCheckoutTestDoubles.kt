package com.ahmetkaragunlu.guidemate.reservation.presentation.checkout

import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider
import com.ahmetkaragunlu.guidemate.payment.domain.model.CheckoutCurrencies
import com.ahmetkaragunlu.guidemate.payment.domain.model.CheckoutCurrency
import com.ahmetkaragunlu.guidemate.payment.domain.model.CheckoutLocale
import com.ahmetkaragunlu.guidemate.payment.domain.model.HostedPaymentDetails
import com.ahmetkaragunlu.guidemate.payment.domain.model.Payment
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentChargeDetails
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentMethod
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentPurpose
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentQuote
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentReservation
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentReservationStatus
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentStatus
import com.ahmetkaragunlu.guidemate.payment.domain.repository.PaymentRepository
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuidePublicSummary
import com.ahmetkaragunlu.guidemate.tour.domain.model.Tour
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourApprovalStatus
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourDetails
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourLocation
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourPublication
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow

private val NOW: Instant = Instant.parse("2026-08-26T10:00:00Z")

internal class FakePaymentRepository : PaymentRepository {
    override val pendingPaymentId: Flow<String?> = MutableStateFlow(null)
    var quoteCalls = 0
    var checkoutCalls = 0
    val checkedOutQuoteIds = mutableListOf<String?>()
    val checkoutIdempotencyKeys = mutableListOf<String>()

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
        return DataResult.Success(quote("quote-$quoteCalls"))
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
        checkedOutQuoteIds += quoteId
        checkoutIdempotencyKeys += idempotencyKey
        return DataResult.Success(payment(method = method, quoteId = quoteId))
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

    override suspend fun clearPendingPayment() = Unit

    private fun quote(id: String): PaymentQuote =
        PaymentQuote(
            id = id,
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

    private fun payment(
        method: PaymentMethod,
        quoteId: String?,
    ): Payment =
        Payment(
            id = "payment-1",
            purpose = PaymentPurpose.TOUR_BOOKING,
            method = method,
            status =
                if (method == PaymentMethod.WALLET) {
                    PaymentStatus.SUCCEEDED
                } else {
                    PaymentStatus.REQUIRES_ACTION
                },
            amountMinor = 10_000,
            currencyCode = "USD",
            chargeDetails =
                PaymentChargeDetails(
                    quoteId = quoteId,
                    amountMinor = 325_000,
                    currencyCode = "TRY",
                    fxRate = BigDecimal("32.5"),
                    fxRateSource = "TEST",
                    fxQuotedAt = NOW,
                ),
            hostedPayment =
                HostedPaymentDetails(
                    pageUrl = "https://sandbox.iyzipay.com/checkout",
                    expiresAt = null,
                ),
            reservation =
                if (method == PaymentMethod.WALLET) {
                    PaymentReservation(
                        id = null,
                        status = PaymentReservationStatus.CONFIRMED,
                    )
                } else {
                    null
                },
            refund = null,
            failureCode = null,
            createdAt = NOW,
            updatedAt = NOW,
        )
}

internal class FakeTourRepository : TourDiscoveryRepository {
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

internal class FakeWalletRepository : WalletRepository {
    override val walletUpdates: Flow<WalletAccount> = emptyFlow()

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

internal class FakeResourceProvider : ResourceProvider {
    override fun getString(id: Int): String = "error"

    override fun getString(id: Int, vararg args: Any): String = "error"

    override fun getQuantityString(id: Int, quantity: Int, vararg args: Any): String = "error"
}

private fun bookableTour(): TourWithSession =
    TourWithSession(
        tour =
            Tour(
                id = "tour-1",
                guide = GuidePublicSummary(1L, "Guide Name"),
                title = "City Walk",
                description = "Description",
                location =
                    TourLocation(
                        country = "Turkiye",
                        city = "Istanbul",
                        timeZoneId = "Europe/Istanbul",
                    ),
                category = TourCategory.CULTURE,
                languages = emptyList(),
                publication = TourPublication(approvalStatus = TourApprovalStatus.APPROVED),
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
