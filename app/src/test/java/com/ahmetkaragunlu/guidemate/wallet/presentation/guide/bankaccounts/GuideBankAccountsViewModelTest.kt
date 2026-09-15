package com.ahmetkaragunlu.guidemate.wallet.presentation.guide.bankaccounts

import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.testing.FakeGuideFinanceRepository
import com.ahmetkaragunlu.guidemate.testing.FakeResourceProvider
import com.ahmetkaragunlu.guidemate.testing.emptyPage
import com.ahmetkaragunlu.guidemate.testing.testBankAccount
import com.ahmetkaragunlu.guidemate.wallet.domain.model.BankAccount
import com.ahmetkaragunlu.guidemate.wallet.domain.iban.TurkishBankCatalog
import com.ahmetkaragunlu.guidemate.wallet.domain.iban.TurkishIbanValidator
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
class GuideBankAccountsViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun validAccountIsNormalizedAndSubmittedThenFormCloses() =
        runTest {
            val repository =
                FakeGuideFinanceRepository().apply {
                    bankAccountsResult = DataResult.Success(emptyPage())
                }
            val validator = TurkishIbanValidator()
            val viewModel =
                GuideBankAccountsViewModel(
                    repository = repository,
                    ibanValidator = validator,
                    bankCatalog = TurkishBankCatalog(),
                    resourceProvider = FakeResourceProvider(),
                )
            runCurrent()

            viewModel.showAddAccountSheet()
            viewModel.onAccountHolderNameChange("  Ada Guide  ")
            viewModel.onIbanChange("TR470000100100000350930001")
            viewModel.confirmAddAccount()
            runCurrent()

            assertEquals(
                "TR470000100100000350930001",
                repository.addBankAccountRequest?.first,
            )
            assertEquals("Ada Guide", repository.addBankAccountRequest?.second)
            assertFalse(viewModel.uiState.value.isAddAccountSheetVisible)
        }

    @Test
    fun successfulDefaultMutationRefreshesCanonicalAccounts() =
        runTest {
            val repository =
                FakeGuideFinanceRepository().apply {
                    bankAccountsResult =
                        bankAccountPage(
                            testBankAccount(id = "bank-1"),
                            testBankAccount(id = "bank-2", isDefault = false),
                        )
                }
            val viewModel = createViewModel(repository)
            runCurrent()
            repository.bankAccountsResult =
                bankAccountPage(
                    testBankAccount(id = "bank-1", isDefault = false),
                    testBankAccount(id = "bank-2"),
                )

            viewModel.showMakeDefaultDialog("bank-2")
            viewModel.confirmMakeDefaultAccount()
            runCurrent()

            assertEquals(listOf("bank-2"), repository.makeDefaultBankAccountRequests)
            assertEquals(2, repository.getBankAccountsCalls)
            assertEquals(
                "bank-2",
                viewModel.uiState.value.bankAccounts.single { it.isDefault }.bankAccountId,
            )
            assertNull(viewModel.uiState.value.showMakeDefaultDialogFor)
            assertFalse(viewModel.uiState.value.isMutationInProgress)
        }

    @Test
    fun failedDeleteUnlocksMutationAndCanRetryWithCanonicalRefresh() =
        runTest {
            val repository =
                FakeGuideFinanceRepository().apply {
                    bankAccountsResult =
                        bankAccountPage(
                            testBankAccount(id = "bank-1"),
                            testBankAccount(id = "bank-2", isDefault = false),
                        )
                    deleteBankAccountResult = DataResult.Error(AppError.NoInternet)
                }
            val viewModel = createViewModel(repository)
            runCurrent()

            viewModel.showDeleteDialog("bank-2")
            viewModel.confirmDeleteAccount()
            runCurrent()

            assertFalse(viewModel.uiState.value.isMutationInProgress)
            assertNotNull(viewModel.uiState.value.errorMessage)
            assertEquals("bank-2", viewModel.uiState.value.showDeleteDialogFor)

            repository.deleteBankAccountResult = DataResult.Success(Unit)
            repository.bankAccountsResult = bankAccountPage(testBankAccount(id = "bank-1"))
            viewModel.confirmDeleteAccount()
            runCurrent()

            assertEquals(listOf("bank-2", "bank-2"), repository.deleteBankAccountRequests)
            assertEquals(2, repository.getBankAccountsCalls)
            assertEquals(
                listOf("bank-1"),
                viewModel.uiState.value.bankAccounts.map { it.bankAccountId },
            )
            assertNull(viewModel.uiState.value.showDeleteDialogFor)
            assertFalse(viewModel.uiState.value.isMutationInProgress)
        }

    private fun createViewModel(repository: FakeGuideFinanceRepository): GuideBankAccountsViewModel =
        GuideBankAccountsViewModel(
            repository = repository,
            ibanValidator = TurkishIbanValidator(),
            bankCatalog = TurkishBankCatalog(),
            resourceProvider = FakeResourceProvider(),
        )

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
