package com.ahmetkaragunlu.guidemate.testing.wallet

import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.testing.common.emptyPage
import com.ahmetkaragunlu.guidemate.wallet.domain.model.WalletAccount
import com.ahmetkaragunlu.guidemate.wallet.domain.model.WalletTransaction
import com.ahmetkaragunlu.guidemate.wallet.domain.model.WalletTransactionDirection
import com.ahmetkaragunlu.guidemate.wallet.domain.model.WalletTransactionType
import com.ahmetkaragunlu.guidemate.wallet.domain.repository.WalletRepository
import java.time.Instant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

data class WalletTransactionsCall(
    val page: Int,
    val size: Int,
)

class FakeWalletRepository : WalletRepository {
    private val mutableWalletUpdates = MutableSharedFlow<WalletAccount>(extraBufferCapacity = 1)
    override val walletUpdates: Flow<WalletAccount> = mutableWalletUpdates.asSharedFlow()
    var walletResult: DataResult<WalletAccount> =
        DataResult.Success(WalletAccount(20_000, 20_000, "USD"))
    var transactionsResult: DataResult<PagedResult<WalletTransaction>> =
        DataResult.Success(emptyPage())
    val transactionResults = ArrayDeque<DataResult<PagedResult<WalletTransaction>>>()
    val transactionRequests = mutableListOf<WalletTransactionsCall>()
    var getWalletCalls: Int = 0

    override suspend fun getWallet(): DataResult<WalletAccount> {
        getWalletCalls++
        return walletResult.also { result ->
            if (result is DataResult.Success) mutableWalletUpdates.tryEmit(result.data)
        }
    }

    fun publishWallet(wallet: WalletAccount) {
        mutableWalletUpdates.tryEmit(wallet)
    }

    override suspend fun getTransactions(
        page: Int,
        size: Int,
    ): DataResult<PagedResult<WalletTransaction>> {
        transactionRequests += WalletTransactionsCall(page = page, size = size)
        return transactionResults.removeFirstOrNull() ?: transactionsResult
    }
}

fun testWalletTransaction(
    id: String,
    type: WalletTransactionType,
    occurredAt: Instant = Instant.parse("2026-01-01T00:00:00Z"),
): WalletTransaction =
    WalletTransaction(
        id = id,
        direction =
            if (type == WalletTransactionType.TOUR_PURCHASE ||
                type == WalletTransactionType.WITHDRAWAL
            ) {
                WalletTransactionDirection.DEBIT
            } else {
                WalletTransactionDirection.CREDIT
            },
        type = type,
        amountMinor = 1_000,
        currencyCode = "USD",
        referenceType = null,
        referenceId = null,
        referenceTitle = "Test transaction",
        occurredAt = occurredAt,
    )

fun walletTransactionPage(
    page: Int,
    isLast: Boolean,
    vararg transactions: WalletTransaction,
): DataResult<PagedResult<WalletTransaction>> =
    DataResult.Success(
        PagedResult(
            items = transactions.toList(),
            page = page,
            size = 20,
            totalElements = transactions.size.toLong() + if (isLast) 0 else 1,
            totalPages = if (isLast) page + 1 else page + 2,
            isFirst = page == 0,
            isLast = isLast,
        )
    )
