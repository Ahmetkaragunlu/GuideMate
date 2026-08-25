package com.ahmetkaragunlu.guidemate.payment.presentation.recovery

import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.payment.domain.model.CheckoutCurrencies
import com.ahmetkaragunlu.guidemate.payment.domain.model.CheckoutLocale
import com.ahmetkaragunlu.guidemate.payment.domain.model.Payment
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentMethod
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentQuote
import com.ahmetkaragunlu.guidemate.payment.domain.repository.PaymentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PaymentRecoveryViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `pending payment is exposed once and can be marked handled`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository = FakePaymentRepository()
            val viewModel = PaymentRecoveryViewModel(repository)

            repository.pending.value = "payment-1"
            runCurrent()
            assertEquals("payment-1", viewModel.pendingPaymentId.value)

            viewModel.onRecoveryNavigationHandled()
            assertNull(viewModel.pendingPaymentId.value)
        }

    private class FakePaymentRepository : PaymentRepository {
        val pending = MutableStateFlow<String?>(null)
        override val pendingPaymentId: Flow<String?> = pending

        override suspend fun clearPendingPayment(paymentId: String) = Unit

        override suspend fun getCheckoutCurrencies(): DataResult<CheckoutCurrencies> =
            error("Not used")

        override suspend fun quoteTour(
            sessionId: String,
            participantCount: Int,
            chargeCurrencyCode: String,
        ): DataResult<PaymentQuote> = error("Not used")

        override suspend fun quoteWalletTopUp(
            amountMinor: Long,
            chargeCurrencyCode: String,
        ): DataResult<PaymentQuote> = error("Not used")

        override suspend fun checkoutTour(
            sessionId: String,
            participantCount: Int,
            method: PaymentMethod,
            quoteId: String?,
            locale: CheckoutLocale,
            idempotencyKey: String,
        ): DataResult<Payment> = error("Not used")

        override suspend fun checkoutWalletTopUp(
            quoteId: String,
            locale: CheckoutLocale,
            idempotencyKey: String,
        ): DataResult<Payment> = error("Not used")

        override suspend fun getPayment(paymentId: String): DataResult<Payment> = error("Not used")

        override suspend fun cancelPayment(paymentId: String): DataResult<Payment> =
            error("Not used")
    }
}
