package com.ahmetkaragunlu.guidemate.wallet.domain.repository

import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.wallet.domain.model.WalletAccount
import com.ahmetkaragunlu.guidemate.wallet.domain.model.WalletTransaction
import kotlinx.coroutines.flow.Flow

interface WalletRepository {
    val walletUpdates: Flow<WalletAccount>

    suspend fun getWallet(): DataResult<WalletAccount>

    suspend fun getTransactions(
        page: Int,
        size: Int,
    ): DataResult<PagedResult<WalletTransaction>>
}
