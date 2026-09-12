package com.ahmetkaragunlu.guidemate.payment.presentation.savedpaymentmethod

import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.payment.domain.model.SavedPaymentMethod
import com.ahmetkaragunlu.guidemate.payment.domain.repository.SavedPaymentMethodRepository
import com.ahmetkaragunlu.guidemate.testing.FakeResourceProvider
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TouristSavedCardsViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `successful delete refreshes cards and releases mutation lock`() = runTest {
        val repository = FakeRepository().apply {
            methodResults += DataResult.Success(listOf(card()))
            methodResults += DataResult.Success(emptyList())
        }
        val viewModel = createViewModel(repository)
        runCurrent()

        viewModel.onShowDeleteDialog("card-1")
        viewModel.onConfirmDeleteCard()
        runCurrent()

        assertEquals(listOf("card-1"), repository.deletedIds)
        assertTrue(viewModel.uiState.value.savedCards.isEmpty())
        assertFalse(viewModel.uiState.value.isMutationInProgress)
        assertEquals(ContentLoadState.CONTENT, viewModel.uiState.value.loadState)
    }

    @Test
    fun `delete failure releases mutation lock and keeps content`() = runTest {
        val repository = FakeRepository().apply {
            methodResults += DataResult.Success(listOf(card()))
            deleteResult = DataResult.Error(AppError.NoInternet)
        }
        val viewModel = createViewModel(repository)
        runCurrent()

        viewModel.onShowDeleteDialog("card-1")
        viewModel.onConfirmDeleteCard()
        runCurrent()

        assertFalse(viewModel.uiState.value.isMutationInProgress)
        assertEquals(1, viewModel.uiState.value.savedCards.size)
        assertTrue(viewModel.uiState.value.errorMessage?.isNotBlank() == true)
    }

    @Test
    fun `refresh failure after delete releases lock and exposes load error`() = runTest {
        val repository = FakeRepository().apply {
            methodResults += DataResult.Success(listOf(card()))
            methodResults += DataResult.Error(AppError.NoInternet)
        }
        val viewModel = createViewModel(repository)
        runCurrent()

        viewModel.onShowDeleteDialog("card-1")
        viewModel.onConfirmDeleteCard()
        runCurrent()

        assertFalse(viewModel.uiState.value.isMutationInProgress)
        assertEquals(ContentLoadState.ERROR, viewModel.uiState.value.loadState)
    }

    @Test
    fun `second delete is ignored while first mutation is running`() = runTest {
        val repository = FakeRepository().apply {
            methodResults += DataResult.Success(listOf(card("card-1"), card("card-2")))
            methodResults += DataResult.Success(emptyList())
            deleteGate = CompletableDeferred()
        }
        val viewModel = createViewModel(repository)
        runCurrent()

        viewModel.onShowDeleteDialog("card-1")
        viewModel.onConfirmDeleteCard()
        runCurrent()
        viewModel.onShowDeleteDialog("card-2")
        viewModel.onConfirmDeleteCard()
        runCurrent()

        assertEquals(listOf("card-1"), repository.deletedIds)

        repository.deleteGate?.complete(Unit)
        runCurrent()
        assertFalse(viewModel.uiState.value.isMutationInProgress)
    }

    private fun createViewModel(repository: SavedPaymentMethodRepository) =
        TouristSavedCardsViewModel(repository, FakeResourceProvider())

    private fun card(id: String = "card-1") =
        SavedPaymentMethod(
            id = id,
            alias = null,
            bankName = "Test Bank",
            bankCode = null,
            cardFamily = null,
            cardAssociation = "VISA",
            cardType = null,
            lastFourDigits = "1234",
            cardHolderName = null,
            expiryMonth = null,
            expiryYear = null,
        )

    private class FakeRepository : SavedPaymentMethodRepository {
        override val paymentMethodChanges: Flow<Unit> = emptyFlow()
        val methodResults = ArrayDeque<DataResult<List<SavedPaymentMethod>>>()
        var deleteResult: DataResult<Unit> = DataResult.Success(Unit)
        var deleteGate: CompletableDeferred<Unit>? = null
        val deletedIds = mutableListOf<String>()

        override suspend fun getSavedPaymentMethods(): DataResult<List<SavedPaymentMethod>> =
            methodResults.removeFirst()

        override suspend fun delete(savedPaymentMethodId: String): DataResult<Unit> {
            deletedIds += savedPaymentMethodId
            deleteGate?.await()
            return deleteResult
        }
    }
}
