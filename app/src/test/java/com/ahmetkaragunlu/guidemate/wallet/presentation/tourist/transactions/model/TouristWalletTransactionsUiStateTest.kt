package com.ahmetkaragunlu.guidemate.wallet.presentation.tourist.transactions.model

import com.ahmetkaragunlu.guidemate.wallet.data.mock.tourist.model.TouristWalletTransactionStatus
import com.ahmetkaragunlu.guidemate.wallet.data.mock.tourist.model.TouristWalletTransactionType
import com.ahmetkaragunlu.guidemate.wallet.data.mock.tourist.model.TouristWalletTransactionUiModel
import java.time.Instant
import org.junit.Assert.assertEquals
import org.junit.Test

class TouristWalletTransactionsUiStateTest {
    @Test
    fun `selected filter returns only matching transaction type`() {
        val topUp = transaction("top-up", TouristWalletTransactionType.TOP_UP)
        val purchase = transaction("purchase", TouristWalletTransactionType.TOUR_PURCHASE)
        val state =
            TouristWalletTransactionsUiState(
                transactions = listOf(topUp, purchase),
                selectedFilter = TouristWalletTransactionFilter.TOP_UP,
            )

        assertEquals(listOf(topUp), state.filteredTransactions)
    }

    @Test
    fun `all filter returns every transaction`() {
        val transactions =
            listOf(
                transaction("top-up", TouristWalletTransactionType.TOP_UP),
                transaction("refund", TouristWalletTransactionType.REFUND),
            )
        val state = TouristWalletTransactionsUiState(transactions = transactions)

        assertEquals(transactions, state.filteredTransactions)
    }

    private fun transaction(
        id: String,
        type: TouristWalletTransactionType,
    ): TouristWalletTransactionUiModel =
        TouristWalletTransactionUiModel(
            transactionId = id,
            title = id,
            amountMinor = 10_000,
            type = type,
            status = TouristWalletTransactionStatus.COMPLETED,
            createdAt = Instant.parse("2026-07-26T12:00:00Z"),
        )
}
