package com.ahmetkaragunlu.guidemate.wallet.presentation.tourist

import androidx.lifecycle.SavedStateHandle
import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.testing.payment.FakePaymentRepository
import com.ahmetkaragunlu.guidemate.testing.common.FakeResourceProvider
import com.ahmetkaragunlu.guidemate.testing.payment.FakeSavedPaymentMethodRepository
import com.ahmetkaragunlu.guidemate.testing.payment.WalletTopUpQuoteCall
import com.ahmetkaragunlu.guidemate.testing.wallet.FakeWalletRepository
import com.ahmetkaragunlu.guidemate.testing.payment.testTopUpQuote
import com.ahmetkaragunlu.guidemate.wallet.domain.model.WalletAccount
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNotEquals
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

            assertEquals(
                WalletTopUpQuoteCall(amountMinor = 5_000, currencyCode = "USD"),
                paymentRepository.calls.topUpQuote,
            )
            assertNotNull(viewModel.uiState.value.topUpQuote)
            assertNull(viewModel.uiState.value.paymentLaunch)

            viewModel.continueTopUp(5_000)
            runCurrent()

            assertEquals("quote-1", paymentRepository.calls.topUpCheckout?.quoteId)
            assertNotNull(paymentRepository.calls.topUpCheckout?.idempotencyKey)
            assertEquals("payment-1", viewModel.uiState.value.paymentLaunch?.paymentId)
            assertFalse(viewModel.uiState.value.isPaymentActionInProgress)
        }

    @Test
    fun canonicalWalletUpdateRefreshesVisibleBalanceWithoutReopeningScreen() =
        runTest {
            val walletRepository = FakeWalletRepository()
            val viewModel =
                TouristWalletViewModel(
                    savedStateHandle = SavedStateHandle(),
                    walletRepository = walletRepository,
                    savedPaymentMethodRepository = FakeSavedPaymentMethodRepository(),
                    paymentRepository = FakePaymentRepository(),
                    resourceProvider = FakeResourceProvider(),
                )
            runCurrent()

            walletRepository.publishWallet(WalletAccount(45_000, 45_000, "USD"))
            runCurrent()

            assertEquals(45_000L, viewModel.uiState.value.balanceMinor)
        }

    @Test
    fun failedTopUpRetryReusesKeyUntilAmountChanges() =
        runTest {
            val paymentRepository =
                FakePaymentRepository().apply {
                    results.topUpCheckout = DataResult.Error(AppError.NoInternet)
                }
            val viewModel =
                TouristWalletViewModel(
                    savedStateHandle = SavedStateHandle(),
                    walletRepository = FakeWalletRepository(),
                    savedPaymentMethodRepository = FakeSavedPaymentMethodRepository(),
                    paymentRepository = paymentRepository,
                    resourceProvider = FakeResourceProvider(),
                )
            runCurrent()

            viewModel.continueTopUp(5_000)
            runCurrent()
            viewModel.continueTopUp(5_000)
            runCurrent()
            viewModel.continueTopUp(5_000)
            runCurrent()

            val firstAttemptKey = paymentRepository.calls.topUpCheckouts.first().idempotencyKey
            assertEquals(
                listOf(firstAttemptKey, firstAttemptKey),
                paymentRepository.calls.topUpCheckouts.map { it.idempotencyKey },
            )

            viewModel.onTopUpAmountChange("60")
            paymentRepository.results.topUpQuote =
                DataResult.Success(testTopUpQuote(id = "quote-2", baseAmountMinor = 6_000))
            viewModel.continueTopUp(6_000)
            runCurrent()
            viewModel.continueTopUp(6_000)
            runCurrent()

            val changedAmountKey = paymentRepository.calls.topUpCheckouts.last().idempotencyKey
            assertNotEquals(firstAttemptKey, changedAmountKey)
            assertFalse(viewModel.uiState.value.isPaymentActionInProgress)
        }
}
