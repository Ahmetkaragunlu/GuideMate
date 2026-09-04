package com.ahmetkaragunlu.guidemate.payment.presentation.status

import androidx.lifecycle.SavedStateHandle
import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.BackendErrorCode
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider
import com.ahmetkaragunlu.guidemate.navigation.tourist.payment.OPEN_HOSTED_IF_REQUIRED_ARGUMENT
import com.ahmetkaragunlu.guidemate.navigation.tourist.payment.PAYMENT_ID_ARGUMENT
import com.ahmetkaragunlu.guidemate.payment.domain.model.CheckoutCurrencies
import com.ahmetkaragunlu.guidemate.payment.domain.model.CheckoutLocale
import com.ahmetkaragunlu.guidemate.payment.domain.model.Payment
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentMethod
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentPurpose
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentQuote
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentReservationStatus
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentStatus
import com.ahmetkaragunlu.guidemate.payment.domain.repository.PaymentRepository
import com.ahmetkaragunlu.guidemate.payment.presentation.status.model.PaymentUiStatus
import com.ahmetkaragunlu.guidemate.testing.FakeNotificationRepository
import com.ahmetkaragunlu.guidemate.wallet.domain.model.WalletAccount
import com.ahmetkaragunlu.guidemate.wallet.domain.model.WalletTransaction
import com.ahmetkaragunlu.guidemate.wallet.domain.repository.WalletRepository
import java.time.Instant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PaymentStatusViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `requires action opens hosted checkout only with backend url`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository = FakePaymentRepository(payment(status = PaymentStatus.REQUIRES_ACTION))
            val viewModel = createViewModel(repository, openHostedIfRequired = true)

            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.shouldOpenHostedCheckout)
            assertEquals(PaymentUiStatus.VERIFYING, viewModel.uiState.value.payment?.status)
            assertFalse(repository.clearedPaymentIds.contains("payment-1"))
        }

    @Test
    fun `polling clears pending payment only after confirmed success`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository =
                FakePaymentRepository(
                    payment(status = PaymentStatus.VERIFYING),
                    payment(
                        status = PaymentStatus.SUCCEEDED,
                        reservationStatus = PaymentReservationStatus.CONFIRMED,
                    ),
                )
            val notificationRepository = FakeNotificationRepository()
            val viewModel = createViewModel(repository, notificationRepository = notificationRepository)

            runCurrent()
            assertEquals(PaymentUiStatus.VERIFYING, viewModel.uiState.value.payment?.status)
            assertTrue(repository.clearedPaymentIds.isEmpty())

            advanceTimeBy(2_000)
            runCurrent()

            assertEquals(PaymentUiStatus.SUCCEEDED, viewModel.uiState.value.payment?.status)
            assertEquals(listOf("payment-1"), repository.clearedPaymentIds)
            assertEquals(1, notificationRepository.markedRelatedTargets.size)
            assertEquals("payment-1", notificationRepository.markedRelatedTargets.single().targetId)
        }

    @Test
    fun `payment not found clears unrecoverable pending id`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository =
                FakePaymentRepository(
                    DataResult.Error(
                        AppError.Backend(
                            code = BackendErrorCode.PAYMENT_NOT_FOUND,
                            fallbackMessage = null,
                        ),
                    ),
                )
            val viewModel = createViewModel(repository)

            advanceUntilIdle()

            assertEquals(listOf("payment-1"), repository.clearedPaymentIds)
            assertFalse(viewModel.uiState.value.isLoading)
            assertEquals("error", viewModel.uiState.value.errorMessage)
        }

    private fun createViewModel(
        repository: PaymentRepository,
        openHostedIfRequired: Boolean = false,
        notificationRepository: FakeNotificationRepository = FakeNotificationRepository(),
    ): PaymentStatusViewModel =
        PaymentStatusViewModel(
            savedStateHandle =
                SavedStateHandle(
                    mapOf(
                        PAYMENT_ID_ARGUMENT to "payment-1",
                        OPEN_HOSTED_IF_REQUIRED_ARGUMENT to openHostedIfRequired,
                    ),
                ),
            paymentRepository = repository,
            walletRepository = SuccessfulWalletRepository(),
            notificationRepository = notificationRepository,
            resourceProvider = FakeResourceProvider(),
        )

    private fun payment(
        status: PaymentStatus,
        reservationStatus: PaymentReservationStatus? = null,
    ): Payment =
        Payment(
            id = "payment-1",
            purpose = PaymentPurpose.TOUR_BOOKING,
            method = PaymentMethod.HOSTED_CARD,
            status = status,
            amountMinor = 10_000,
            currencyCode = "USD",
            quoteId = "quote-1",
            chargeAmountMinor = 325_000,
            chargeCurrencyCode = "TRY",
            fxRate = null,
            fxRateSource = null,
            fxQuotedAt = null,
            paymentPageUrl = "https://sandbox.iyzipay.com/checkout",
            expiresAt = null,
            reservationId = reservationStatus?.let { "reservation-1" },
            reservationStatus = reservationStatus,
            refundId = null,
            refundStatus = null,
            refundAmountMinor = null,
            refundChargeAmountMinor = null,
            refundChargeCurrencyCode = null,
            failureCode = null,
            createdAt = TEST_INSTANT,
            updatedAt = TEST_INSTANT,
        )

    private class FakePaymentRepository(vararg results: Any) : PaymentRepository {
        private val responses = ArrayDeque(results.toList())
        val clearedPaymentIds = mutableListOf<String>()
        override val pendingPaymentId: Flow<String?> = MutableStateFlow("payment-1")

        override suspend fun getPayment(paymentId: String): DataResult<Payment> {
            val next = if (responses.size > 1) responses.removeFirst() else responses.first()
            @Suppress("UNCHECKED_CAST") return next as? DataResult<Payment>
                ?: DataResult.Success(next as Payment)
        }

        override suspend fun clearPendingPayment(paymentId: String) {
            clearedPaymentIds += paymentId
        }

        override suspend fun clearPendingPayment() = Unit

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

        override suspend fun cancelPayment(paymentId: String): DataResult<Payment> =
            error("Not used")
    }

    private class SuccessfulWalletRepository : WalletRepository {
        override suspend fun getWallet(): DataResult<WalletAccount> = error("Not used")

        override suspend fun getTransactions(
            page: Int,
            size: Int,
        ): DataResult<PagedResult<WalletTransaction>> = error("Not used")
    }

    private class FakeResourceProvider : ResourceProvider {
        override fun getString(id: Int): String = "error"

        override fun getString(
            id: Int,
            vararg args: Any,
        ): String = "error"

        override fun getQuantityString(
            id: Int,
            quantity: Int,
            vararg args: Any,
        ): String = "error"
    }

    private companion object {
        val TEST_INSTANT: Instant = Instant.parse("2026-08-25T12:00:00Z")
    }
}
