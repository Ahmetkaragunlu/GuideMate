package com.ahmetkaragunlu.guidemate.reservation.presentation.checkout

import androidx.lifecycle.SavedStateHandle
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentMethod
import com.ahmetkaragunlu.guidemate.payment.domain.repository.PaymentRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class TourCheckoutViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `checkout requires terms before requesting quote or payment`() =
        runTest(mainDispatcherRule.dispatcher) {
            val paymentRepository = FakePaymentRepository()
            val viewModel = createObservedViewModel(paymentRepository)
            advanceUntilIdle()

            assertEquals(ContentLoadState.CONTENT, viewModel.uiState.value.loadState)
            viewModel.continueCheckout()
            advanceUntilIdle()

            assertEquals(
                R.string.checkout_error_terms_required,
                viewModel.uiState.value.submission.validationErrorResId,
            )
            assertEquals(0, paymentRepository.quoteCalls)
            assertEquals(0, paymentRepository.checkoutCalls)
        }

    @Test
    fun `hosted card requests quote before initializing payment`() =
        runTest(mainDispatcherRule.dispatcher) {
            val paymentRepository = FakePaymentRepository()
            val viewModel = createObservedViewModel(paymentRepository)
            advanceUntilIdle()
            acceptTerms(viewModel)
            advanceUntilIdle()

            viewModel.continueCheckout()
            advanceUntilIdle()

            assertEquals(1, paymentRepository.quoteCalls)
            assertEquals(0, paymentRepository.checkoutCalls)
            assertNotNull(viewModel.uiState.value.payment.quote)

            viewModel.continueCheckout()
            advanceUntilIdle()

            assertEquals(1, paymentRepository.checkoutCalls)
            assertEquals("payment-1", viewModel.uiState.value.submission.paymentLaunch?.paymentId)
            assertNull(viewModel.uiState.value.payment.quote)
            assertNull(viewModel.uiState.value.submission.validationErrorResId)
        }

    @Test
    fun `completed hosted attempt requires a new quote and idempotency key for retry`() =
        runTest(mainDispatcherRule.dispatcher) {
            val paymentRepository = FakePaymentRepository()
            val viewModel = createObservedViewModel(paymentRepository)
            advanceUntilIdle()
            acceptTerms(viewModel)
            runCurrent()

            viewModel.continueCheckout()
            advanceUntilIdle()
            viewModel.continueCheckout()
            advanceUntilIdle()
            viewModel.onPaymentNavigationHandled()
            runCurrent()

            viewModel.continueCheckout()
            advanceUntilIdle()
            assertEquals(2, paymentRepository.quoteCalls)
            assertEquals(1, paymentRepository.checkoutCalls)
            assertEquals("quote-2", viewModel.uiState.value.payment.quote?.id)

            viewModel.continueCheckout()
            advanceUntilIdle()

            assertEquals(listOf("quote-1", "quote-2"), paymentRepository.checkedOutQuoteIds)
            assertEquals(2, paymentRepository.checkoutIdempotencyKeys.distinct().size)
        }

    @Test
    fun `wallet checkout keeps verification visible for the minimum transition`() =
        runTest(mainDispatcherRule.dispatcher) {
            val paymentRepository = FakePaymentRepository()
            val viewModel = createObservedViewModel(paymentRepository)
            advanceUntilIdle()
            acceptTerms(viewModel)
            viewModel.onPaymentMethodSelected(PaymentMethod.WALLET)
            runCurrent()

            viewModel.continueCheckout()
            runCurrent()

            assertTrue(viewModel.uiState.value.isWalletPaymentVerifying)
            assertNull(viewModel.uiState.value.submission.paymentLaunch)

            advanceTimeBy(599)
            runCurrent()
            assertNull(viewModel.uiState.value.submission.paymentLaunch)

            advanceTimeBy(1)
            runCurrent()
            assertFalse(viewModel.uiState.value.isWalletPaymentVerifying)
            assertFalse(
                requireNotNull(viewModel.uiState.value.submission.paymentLaunch)
                    .requiresHostedCheckout,
            )
        }

    @Test
    fun `checkout terms require reading before acceptance and can be declined`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = createObservedViewModel(FakePaymentRepository())
            advanceUntilIdle()

            viewModel.onTermsCheckboxClicked()
            runCurrent()
            assertTrue(viewModel.uiState.value.terms.isSheetVisible)

            viewModel.acceptTerms()
            runCurrent()
            assertFalse(viewModel.uiState.value.terms.isAccepted)

            viewModel.markTermsAsRead()
            viewModel.acceptTerms()
            runCurrent()
            assertTrue(viewModel.uiState.value.terms.isAccepted)
            assertFalse(viewModel.uiState.value.terms.isSheetVisible)

            viewModel.onTermsCheckboxClicked()
            runCurrent()
            assertFalse(viewModel.uiState.value.terms.isAccepted)
        }

    private fun TestScope.createObservedViewModel(
        paymentRepository: PaymentRepository,
    ): TourCheckoutViewModel =
        createViewModel(paymentRepository).also { viewModel ->
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.uiState.collect()
            }
        }

    private fun createViewModel(paymentRepository: PaymentRepository): TourCheckoutViewModel =
        TourCheckoutViewModel(
            savedStateHandle = SavedStateHandle(mapOf("sessionId" to "session-1")),
            tourRepository = FakeTourRepository(),
            walletRepository = FakeWalletRepository(),
            paymentRepository = paymentRepository,
            resourceProvider = FakeResourceProvider(),
        )

    private fun acceptTerms(viewModel: TourCheckoutViewModel) {
        viewModel.onTermsCheckboxClicked()
        viewModel.markTermsAsRead()
        viewModel.acceptTerms()
    }
}
