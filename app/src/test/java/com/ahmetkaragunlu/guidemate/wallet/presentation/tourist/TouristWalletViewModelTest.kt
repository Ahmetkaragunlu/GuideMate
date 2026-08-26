package com.ahmetkaragunlu.guidemate.wallet.presentation.tourist

import androidx.lifecycle.SavedStateHandle
import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.testing.FakePaymentRepository
import com.ahmetkaragunlu.guidemate.testing.FakeResourceProvider
import com.ahmetkaragunlu.guidemate.testing.FakeSavedPaymentMethodRepository
import com.ahmetkaragunlu.guidemate.testing.FakeWalletRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TouristWalletViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun topUpRequiresFreshQuoteBeforeLaunchingHostedPayment() =
        runTest {
            val paymentRepository = FakePaymentRepository()
            val viewModel =
                TouristWalletViewModel(
                    savedStateHandle = SavedStateHandle(),
                    walletRepository = FakeWalletRepository(),
                    savedPaymentMethodRepository = FakeSavedPaymentMethodRepository(),
                    paymentRepository = paymentRepository,
                    resourceProvider = FakeResourceProvider(),
                )
            runCurrent()

            assertEquals(ContentLoadState.CONTENT, viewModel.uiState.value.loadState)
            viewModel.continueTopUp(5_000)
            runCurrent()

            assertEquals(5_000L to "USD", paymentRepository.quotedTopUp)
            assertNotNull(viewModel.uiState.value.topUpQuote)
            assertNull(viewModel.uiState.value.paymentLaunch)

            viewModel.continueTopUp(5_000)
            runCurrent()

            assertEquals("quote-1", paymentRepository.checkedOutQuoteId)
            assertNotNull(paymentRepository.checkoutIdempotencyKey)
            assertEquals("payment-1", viewModel.uiState.value.paymentLaunch?.paymentId)
            assertFalse(viewModel.uiState.value.isPaymentActionInProgress)
        }
}
