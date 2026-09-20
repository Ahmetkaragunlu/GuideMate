package com.ahmetkaragunlu.guidemate.testing.payment

import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.payment.domain.model.CheckoutCurrencies
import com.ahmetkaragunlu.guidemate.payment.domain.model.CheckoutCurrency
import com.ahmetkaragunlu.guidemate.payment.domain.model.CheckoutLocale
import com.ahmetkaragunlu.guidemate.payment.domain.model.HostedPaymentDetails
import com.ahmetkaragunlu.guidemate.payment.domain.model.Payment
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentChargeDetails
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentMethod
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentPurpose
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentQuote
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentStatus
import com.ahmetkaragunlu.guidemate.payment.domain.model.SavedPaymentMethod
import com.ahmetkaragunlu.guidemate.payment.domain.repository.PaymentRepository
import com.ahmetkaragunlu.guidemate.payment.domain.repository.SavedPaymentMethodRepository
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

data class WalletTopUpQuoteCall(
    val amountMinor: Long,
    val currencyCode: String,
)

data class WalletTopUpCheckoutCall(
    val quoteId: String,
    val idempotencyKey: String,
)

class FakeSavedPaymentMethodRepository : SavedPaymentMethodRepository {
    override val paymentMethodChanges: Flow<Unit> = MutableSharedFlow()
    var methodsResult: DataResult<List<SavedPaymentMethod>> = DataResult.Success(emptyList())

    override suspend fun getSavedPaymentMethods(): DataResult<List<SavedPaymentMethod>> =
        methodsResult

    override suspend fun delete(savedPaymentMethodId: String): DataResult<Unit> =
        error("Not required by this test fixture")
}

class PaymentFakeState {
    val pendingPaymentId = MutableStateFlow<String?>(null)
}

class PaymentFakeResults {
    var currencies: DataResult<CheckoutCurrencies> =
        DataResult.Success(
            CheckoutCurrencies(
                baseCurrencyCode = "USD",
                chargeCurrencies = listOf(CheckoutCurrency("USD", 2)),
            )
        )
    var topUpQuote: DataResult<PaymentQuote> = DataResult.Success(testTopUpQuote())
    var topUpCheckout: DataResult<Payment> = DataResult.Success(testTopUpPayment())
    val payments = ArrayDeque<DataResult<Payment>>()
    var cancel: DataResult<Payment> = DataResult.Success(testTopUpPayment())
}

class PaymentFakeCalls {
    var topUpQuote: WalletTopUpQuoteCall? = null
    var topUpCheckout: WalletTopUpCheckoutCall? = null
    val topUpCheckouts = mutableListOf<WalletTopUpCheckoutCall>()
    val requestedPaymentIds = mutableListOf<String>()
    var cancelledPaymentId: String? = null
    var clearAllPendingPayments = 0
}

class PaymentFakeFailures {
    var clearAllPendingPayments: Throwable? = null
}

class FakePaymentRepository : PaymentRepository {
    val state = PaymentFakeState()
    val results = PaymentFakeResults()
    val calls = PaymentFakeCalls()
    val failures = PaymentFakeFailures()
    override val pendingPaymentId: Flow<String?> = state.pendingPaymentId

    override suspend fun getCheckoutCurrencies(): DataResult<CheckoutCurrencies> = results.currencies

    override suspend fun quoteTour(
        sessionId: String,
        participantCount: Int,
        chargeCurrencyCode: String,
    ): DataResult<PaymentQuote> = error("Not required by this test fixture")

    override suspend fun quoteWalletTopUp(
        amountMinor: Long,
        chargeCurrencyCode: String,
    ): DataResult<PaymentQuote> {
        calls.topUpQuote =
            WalletTopUpQuoteCall(
                amountMinor = amountMinor,
                currencyCode = chargeCurrencyCode,
            )
        return results.topUpQuote
    }

    override suspend fun checkoutTour(
        sessionId: String,
        participantCount: Int,
        method: PaymentMethod,
        quoteId: String?,
        locale: CheckoutLocale,
        idempotencyKey: String,
    ): DataResult<Payment> = error("Not required by this test fixture")

    override suspend fun checkoutWalletTopUp(
        quoteId: String,
        locale: CheckoutLocale,
        idempotencyKey: String,
    ): DataResult<Payment> {
        val call = WalletTopUpCheckoutCall(quoteId = quoteId, idempotencyKey = idempotencyKey)
        calls.topUpCheckout = call
        calls.topUpCheckouts += call
        return results.topUpCheckout
    }

    override suspend fun getPayment(paymentId: String): DataResult<Payment> {
        calls.requestedPaymentIds += paymentId
        return results.payments.removeFirst()
    }

    override suspend fun cancelPayment(paymentId: String): DataResult<Payment> {
        calls.cancelledPaymentId = paymentId
        return results.cancel
    }

    override suspend fun clearPendingPayment(paymentId: String) = Unit

    override suspend fun clearPendingPayment() {
        calls.clearAllPendingPayments++
        failures.clearAllPendingPayments?.let { throw it }
    }
}

fun testTopUpQuote(
    id: String = "quote-1",
    baseAmountMinor: Long = 5_000,
): PaymentQuote =
    PaymentQuote(
        id = id,
        purpose = PaymentPurpose.WALLET_TOP_UP,
        baseAmountMinor = baseAmountMinor,
        baseCurrencyCode = "USD",
        chargeAmountMinor = baseAmountMinor,
        chargeCurrencyCode = "USD",
        fxRate = BigDecimal.ONE,
        rateSource = "TEST",
        rateDate = LocalDate.of(2026, 1, 1),
        quotedAt = Instant.parse("2026-01-01T00:00:00Z"),
        expiresAt = Instant.parse("2099-01-01T00:00:00Z"),
    )

fun testTopUpPayment(): Payment =
    Payment(
        id = "payment-1",
        purpose = PaymentPurpose.WALLET_TOP_UP,
        method = PaymentMethod.HOSTED_CARD,
        status = PaymentStatus.REQUIRES_ACTION,
        amountMinor = 5_000,
        currencyCode = "USD",
        chargeDetails =
            PaymentChargeDetails(
                quoteId = "quote-1",
                amountMinor = 5_000,
                currencyCode = "USD",
                fxRate = BigDecimal.ONE,
                fxRateSource = "TEST",
                fxQuotedAt = Instant.parse("2026-01-01T00:00:00Z"),
            ),
        hostedPayment =
            HostedPaymentDetails(
                pageUrl = "https://sandbox.example.com/payment",
                expiresAt = Instant.parse("2099-01-01T00:00:00Z"),
            ),
        reservation = null,
        refund = null,
        failureCode = null,
        createdAt = Instant.parse("2026-01-01T00:00:00Z"),
        updatedAt = Instant.parse("2026-01-01T00:00:00Z"),
    )
