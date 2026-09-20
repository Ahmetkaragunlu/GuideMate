package com.ahmetkaragunlu.guidemate.wallet.presentation.tourist.transactions

import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.testing.wallet.FakeWalletRepository
import com.ahmetkaragunlu.guidemate.testing.wallet.WalletTransactionsCall
import com.ahmetkaragunlu.guidemate.testing.wallet.testWalletTransaction
import com.ahmetkaragunlu.guidemate.testing.wallet.walletTransactionPage
import com.ahmetkaragunlu.guidemate.wallet.domain.model.WalletTransactionType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TouristWalletTransactionsViewModelTest {
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
                            testWalletTransaction("top-up-1", WalletTransactionType.TOP_UP),
                        )
                    transactionResults +=
                        walletTransactionPage(
                            page = 1,
                            isLast = true,
                            testWalletTransaction("refund-1", WalletTransactionType.REFUND),
                        )
                }
            val viewModel = TouristWalletTransactionsViewModel(repository)
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
                listOf("top-up-1", "refund-1"),
                viewModel.uiState.value.transactions.map { it.transactionId },
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
                            testWalletTransaction("top-up-1", WalletTransactionType.TOP_UP),
                        )
                    transactionResults += DataResult.Error(AppError.NoInternet)
                }
            val viewModel = TouristWalletTransactionsViewModel(repository)
            runCurrent()

            viewModel.loadNextPage()
            runCurrent()

            assertEquals(
                listOf("top-up-1"),
                viewModel.uiState.value.transactions.map { it.transactionId },
            )
            assertEquals(ContentLoadState.CONTENT, viewModel.uiState.value.loadState)
            assertFalse(viewModel.uiState.value.isAppending)
        }
}
