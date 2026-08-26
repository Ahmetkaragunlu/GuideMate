package com.ahmetkaragunlu.guidemate.wallet.presentation.guide

import androidx.lifecycle.SavedStateHandle
import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.testing.FakeGuideFinanceRepository
import com.ahmetkaragunlu.guidemate.testing.FakeResourceProvider
import com.ahmetkaragunlu.guidemate.testing.FakeWalletRepository
import com.ahmetkaragunlu.guidemate.testing.testBankAccount
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
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
                    bankAccountsResult =
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
                    resourceProvider = FakeResourceProvider(),
                    savedStateHandle = SavedStateHandle(),
                )
            val collection = backgroundScope.launch { viewModel.uiState.collect {} }
            runCurrent()

            assertEquals(ContentLoadState.CONTENT, viewModel.uiState.value.loadState)
            assertEquals("bank-1", viewModel.uiState.value.selectedBankAccountId)

            viewModel.requestWithdrawal(5_000)
            runCurrent()

            assertEquals("bank-1", financeRepository.withdrawalRequest?.first)
            assertEquals(5_000L, financeRepository.withdrawalRequest?.second)
            assertNotNull(financeRepository.withdrawalRequest?.third)
            assertTrue(viewModel.uiState.value.isWithdrawalRequestSubmitted)
            assertFalse(viewModel.uiState.value.isWithdrawalInProgress)
            collection.cancel()
        }
}
