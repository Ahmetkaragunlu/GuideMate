package com.ahmetkaragunlu.guidemate.wallet.presentation.guide

import androidx.lifecycle.SavedStateHandle
import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationType
import com.ahmetkaragunlu.guidemate.testing.wallet.FakeGuideFinanceRepository
import com.ahmetkaragunlu.guidemate.testing.notification.FakeNotificationRepository
import com.ahmetkaragunlu.guidemate.testing.common.FakeResourceProvider
import com.ahmetkaragunlu.guidemate.testing.wallet.FakeWalletRepository
import com.ahmetkaragunlu.guidemate.testing.wallet.testBankAccount
import com.ahmetkaragunlu.guidemate.testing.notification.testNotification
import com.ahmetkaragunlu.guidemate.wallet.domain.model.BankAccount
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GuideMyWalletViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun withdrawalUsesDefaultBankAccountAndPublishesConfirmation() =
        runTest {
            val financeRepository =
                FakeGuideFinanceRepository().apply {
                    results.bankAccounts =
                        DataResult.Success(
                            PagedResult(
                                items = listOf(testBankAccount()),
                                page = 0,
                                size = 50,
                                totalElements = 1,
                                totalPages = 1,
                                isFirst = true,
                                isLast = true,
                            )
                        )
                }
            val viewModel =
                GuideMyWalletViewModel(
                    walletRepository = FakeWalletRepository(),
                    financeRepository = financeRepository,
                    notificationRepository = FakeNotificationRepository(),
                    resourceProvider = FakeResourceProvider(),
                    savedStateHandle = SavedStateHandle(),
                )
            val collection = backgroundScope.launch { viewModel.uiState.collect {} }
            runCurrent()

            assertEquals(ContentLoadState.CONTENT, viewModel.uiState.value.loadState)
            assertEquals("bank-1", viewModel.uiState.value.selectedBankAccountId)

            viewModel.requestWithdrawal(5_000)
            runCurrent()

            assertEquals("bank-1", financeRepository.calls.withdrawal?.bankAccountId)
            assertEquals(5_000L, financeRepository.calls.withdrawal?.amountMinor)
            assertNotNull(financeRepository.calls.withdrawal?.idempotencyKey)
            assertTrue(viewModel.uiState.value.isWithdrawalRequestSubmitted)
            assertFalse(viewModel.uiState.value.isWithdrawalInProgress)
            collection.cancel()
        }

    @Test
    fun guideEarningNotificationsRefreshCanonicalWalletData() =
        runTest {
            val walletRepository = FakeWalletRepository()
            val notificationRepository = FakeNotificationRepository()
            val viewModel =
                GuideMyWalletViewModel(
                    walletRepository = walletRepository,
                    financeRepository = FakeGuideFinanceRepository(),
                    notificationRepository = notificationRepository,
                    resourceProvider = FakeResourceProvider(),
                    savedStateHandle = SavedStateHandle(),
                )
            val collection = backgroundScope.launch { viewModel.uiState.collect {} }
            runCurrent()

            assertEquals(1, walletRepository.getWalletCalls)

            notificationRepository.state.notifications.value =
                listOf(testNotification(type = NotificationType.CHAT_MESSAGE))
            runCurrent()

            assertEquals(1, walletRepository.getWalletCalls)

            notificationRepository.state.notifications.value =
                listOf(
                    testNotification(id = "purchase", type = NotificationType.TOUR_PURCHASED),
                    testNotification(id = "chat", type = NotificationType.CHAT_MESSAGE),
                )
            runCurrent()

            assertEquals(2, walletRepository.getWalletCalls)

            notificationRepository.state.notifications.value =
                listOf(
                    testNotification(id = "earning", type = NotificationType.EARNING_AVAILABLE),
                    testNotification(id = "purchase", type = NotificationType.TOUR_PURCHASED),
                )
            runCurrent()

            assertEquals(3, walletRepository.getWalletCalls)
            collection.cancel()
        }

    @Test
    fun failedWithdrawalRetryReusesKeyUntilAccountOrAmountChanges() =
        runTest {
            val financeRepository =
                FakeGuideFinanceRepository().apply {
                    results.bankAccounts =
                        bankAccountPage(
                            testBankAccount(id = "bank-1"),
                            testBankAccount(id = "bank-2", isDefault = false),
                        )
                    results.withdrawal = DataResult.Error(AppError.NoInternet)
                }
            val viewModel =
                GuideMyWalletViewModel(
                    walletRepository = FakeWalletRepository(),
                    financeRepository = financeRepository,
                    notificationRepository = FakeNotificationRepository(),
                    resourceProvider = FakeResourceProvider(),
                    savedStateHandle = SavedStateHandle(),
                )
            val collection = backgroundScope.launch { viewModel.uiState.collect {} }
            runCurrent()

            viewModel.requestWithdrawal(5_000)
            runCurrent()
            viewModel.requestWithdrawal(5_000)
            runCurrent()

            val firstAttemptKey = financeRepository.calls.withdrawals.first().idempotencyKey
            assertEquals(
                listOf(firstAttemptKey, firstAttemptKey),
                financeRepository.calls.withdrawals.map { it.idempotencyKey },
            )

            viewModel.selectNextBankAccount()
            runCurrent()
            viewModel.requestWithdrawal(5_000)
            runCurrent()
            val changedAccountKey = financeRepository.calls.withdrawals.last().idempotencyKey
            assertNotEquals(firstAttemptKey, changedAccountKey)

            viewModel.requestWithdrawal(6_000)
            runCurrent()
            val changedAmountKey = financeRepository.calls.withdrawals.last().idempotencyKey
            assertNotEquals(changedAccountKey, changedAmountKey)
            assertFalse(viewModel.uiState.value.isWithdrawalInProgress)
            collection.cancel()
        }

    private fun bankAccountPage(vararg accounts: BankAccount): DataResult<PagedResult<BankAccount>> =
        DataResult.Success(
            PagedResult(
                items = accounts.toList(),
                page = 0,
                size = 50,
                totalElements = accounts.size.toLong(),
                totalPages = 1,
                isFirst = true,
                isLast = true,
            )
        )
}
