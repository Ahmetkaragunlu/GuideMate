package com.ahmetkaragunlu.guidemate.wallet.presentation.guide.bankaccounts

import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.testing.FakeGuideFinanceRepository
import com.ahmetkaragunlu.guidemate.testing.FakeResourceProvider
import com.ahmetkaragunlu.guidemate.testing.emptyPage
import com.ahmetkaragunlu.guidemate.wallet.domain.iban.TurkishBankCatalog
import com.ahmetkaragunlu.guidemate.wallet.domain.iban.TurkishIbanValidator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
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
}
