package com.ahmetkaragunlu.guidemate.wallet.presentation.guide.transactions

import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.testing.wallet.FakeGuideFinanceRepository
import com.ahmetkaragunlu.guidemate.testing.common.FakeResourceProvider
import com.ahmetkaragunlu.guidemate.testing.wallet.FakeWalletRepository
import com.ahmetkaragunlu.guidemate.testing.wallet.WalletTransactionsCall
import com.ahmetkaragunlu.guidemate.testing.wallet.testWalletTransaction
import com.ahmetkaragunlu.guidemate.testing.wallet.walletTransactionPage
import com.ahmetkaragunlu.guidemate.wallet.domain.model.WalletTransactionType
import java.time.Instant
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GuideWalletTransactionsViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun firstAndNextPageMergeThenLastPageBlocksFurtherRequests() =
        runTest {
            val repository =
                FakeWalletRepository().apply {
                    transactionResults +=
                        walletTransactionPage(
                            page = 0,
                            isLast = false,
                            testWalletTransaction("earning-1", WalletTransactionType.GUIDE_EARNING),
                        )
                    transactionResults +=
                        walletTransactionPage(
                            page = 1,
                            isLast = true,
                            testWalletTransaction(
                                "withdrawal-1",
                                WalletTransactionType.WITHDRAWAL,
                                Instant.parse("2026-02-01T00:00:00Z"),
                            ),
                        )
                }
            val viewModel = createViewModel(repository)
            runCurrent()

            viewModel.loadNextPage()
            runCurrent()
            viewModel.loadNextPage()
            runCurrent()

            assertEquals(
                listOf(
                    WalletTransactionsCall(page = 0, size = 20),
                    WalletTransactionsCall(page = 1, size = 20),
                ),
                repository.transactionRequests,
            )
            assertEquals(
                listOf("withdrawal-1", "earning-1"),
                viewModel.uiState.value.transactions.map { it.id },
            )
            assertEquals(1, viewModel.uiState.value.page)
            assertFalse(viewModel.uiState.value.isAppending)
        }

    @Test
    fun appendFailureKeepsExistingTransactionsAndRestoresContentState() =
        runTest {
            val repository =
                FakeWalletRepository().apply {
                    transactionResults +=
                        walletTransactionPage(
                            page = 0,
                            isLast = false,
                            testWalletTransaction("earning-1", WalletTransactionType.GUIDE_EARNING),
                        )
                    transactionResults += DataResult.Error(AppError.NoInternet)
                }
            val viewModel = createViewModel(repository)
            runCurrent()

            viewModel.loadNextPage()
            runCurrent()

            assertEquals(listOf("earning-1"), viewModel.uiState.value.transactions.map { it.id })
            assertEquals(ContentLoadState.CONTENT, viewModel.uiState.value.loadState)
            assertFalse(viewModel.uiState.value.isAppending)
            assertNotNull(viewModel.uiState.value.errorMessage)
        }

    private fun createViewModel(repository: FakeWalletRepository) =
        GuideWalletTransactionsViewModel(
            repository = repository,
            financeRepository = FakeGuideFinanceRepository(),
            resourceProvider = FakeResourceProvider(),
        )
}
