package com.ahmetkaragunlu.guidemate.payment.presentation.hosted

import androidx.lifecycle.SavedStateHandle
import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.navigation.tourist.payment.PAYMENT_ID_ARGUMENT
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentStatus
import com.ahmetkaragunlu.guidemate.testing.FakePaymentRepository
import com.ahmetkaragunlu.guidemate.testing.FakeResourceProvider
import com.ahmetkaragunlu.guidemate.testing.testTopUpPayment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HostedPaymentViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun callbackWaitsForMinimumDurationAndBackendStatusBeforeVerification() =
        runTest {
            val repository =
                FakePaymentRepository().apply {
                    paymentResults += DataResult.Success(testTopUpPayment())
                    paymentResults +=
                        DataResult.Success(testTopUpPayment().copy(status = PaymentStatus.SUCCEEDED))
                }
            val viewModel =
                HostedPaymentViewModel(
                    savedStateHandle =
                        SavedStateHandle(mapOf(PAYMENT_ID_ARGUMENT to "payment-1")),
                    paymentRepository = repository,
                    resourceProvider = FakeResourceProvider(),
                )
            runCurrent()

            assertEquals(ContentLoadState.CONTENT, viewModel.uiState.value.loadState)
            assertEquals(
                "https://sandbox.example.com/payment",
                viewModel.uiState.value.paymentPageUrl,
            )
            assertFalse(viewModel.uiState.value.shouldVerifyPayment)

            viewModel.onPageStarted("https://local.example/api/v1/payments/iyzico/callback")
            advanceTimeBy(2_001)
            runCurrent()

            assertTrue(viewModel.uiState.value.isVerifyingCallback)
            assertFalse(viewModel.uiState.value.shouldVerifyPayment)

            advanceTimeBy(999)
            runCurrent()

            assertTrue(viewModel.uiState.value.shouldVerifyPayment)
        }

    @Test
    fun callbackKeepsVerifyingWhenMinimumDurationEndsBeforeBackendResolves() =
        runTest {
            val repository =
                FakePaymentRepository().apply {
                    paymentResults += DataResult.Success(testTopUpPayment())
                    paymentResults += DataResult.Success(testTopUpPayment())
                    paymentResults +=
                        DataResult.Success(testTopUpPayment().copy(status = PaymentStatus.SUCCEEDED))
                }
            val viewModel = createViewModel(repository)
            runCurrent()

            viewModel.onPageStarted("https://local.example/api/v1/payments/iyzico/callback")
            advanceTimeBy(3_001)
            runCurrent()

            assertFalse(viewModel.uiState.value.shouldVerifyPayment)

            advanceTimeBy(3_000)
            runCurrent()

            assertTrue(viewModel.uiState.value.shouldVerifyPayment)
        }

    @Test
    fun ordinaryHostedPageDoesNotStartPaymentPolling() =
        runTest {
            val repository =
                FakePaymentRepository().apply {
                    paymentResults += DataResult.Success(testTopUpPayment())
                }
            val viewModel = createViewModel(repository)
            runCurrent()

            viewModel.onPageFinished("https://sandbox.example.com/payment")
            advanceTimeBy(60_000)
            runCurrent()

            assertEquals(listOf("payment-1"), repository.requestedPaymentIds)
            assertFalse(viewModel.uiState.value.shouldVerifyPayment)
        }

    @Test
    fun callbackPollingHandsOffToBoundedStatusVerificationWhenProviderStaysPending() =
        runTest {
            val repository =
                FakePaymentRepository().apply {
                    repeat(8) { paymentResults += DataResult.Success(testTopUpPayment()) }
                }
            val viewModel = createViewModel(repository)
            runCurrent()

            viewModel.onPageStarted("https://local.example/api/v1/payments/iyzico/callback")
            advanceTimeBy(30_001)
            runCurrent()

            assertTrue(viewModel.uiState.value.shouldVerifyPayment)
            assertTrue(repository.requestedPaymentIds.size > 1)
        }

    @Test
    fun cancellingHostedPaymentUsesBackendAndContinuesWithVerification() =
        runTest {
            val repository =
                FakePaymentRepository().apply {
                    paymentResults += DataResult.Success(testTopUpPayment())
                }
            val viewModel = createViewModel(repository)
            runCurrent()

            viewModel.cancelPayment()
            runCurrent()

            assertEquals("payment-1", repository.cancelledPaymentId)
            assertFalse(viewModel.uiState.value.isCancelling)
            assertTrue(viewModel.uiState.value.shouldVerifyPayment)
        }

    @Test
    fun failedCancellationKeepsHostedPaymentOpenAndShowsError() =
        runTest {
            val repository =
                FakePaymentRepository().apply {
                    paymentResults += DataResult.Success(testTopUpPayment())
                    cancelResult = DataResult.Error(AppError.NoInternet)
                }
            val viewModel = createViewModel(repository)
            runCurrent()

            viewModel.cancelPayment()
            runCurrent()

            assertFalse(viewModel.uiState.value.isCancelling)
            assertFalse(viewModel.uiState.value.shouldVerifyPayment)
            assertTrue(viewModel.uiState.value.pageErrorMessage?.isNotBlank() == true)
        }

    private fun createViewModel(repository: FakePaymentRepository) =
        HostedPaymentViewModel(
            savedStateHandle = SavedStateHandle(mapOf(PAYMENT_ID_ARGUMENT to "payment-1")),
            paymentRepository = repository,
            resourceProvider = FakeResourceProvider(),
        )
}
