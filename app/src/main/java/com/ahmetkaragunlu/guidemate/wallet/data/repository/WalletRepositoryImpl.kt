package com.ahmetkaragunlu.guidemate.wallet.data.repository

import com.ahmetkaragunlu.guidemate.common.network.ApiCallExecutor
import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.wallet.data.mapper.toDomain
import com.ahmetkaragunlu.guidemate.wallet.data.remote.api.WalletApi
import com.ahmetkaragunlu.guidemate.wallet.domain.model.WalletAccount
import com.ahmetkaragunlu.guidemate.wallet.domain.model.WalletTransaction
import com.ahmetkaragunlu.guidemate.wallet.domain.repository.WalletRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class WalletRepositoryImpl @Inject constructor(
    private val api: WalletApi,
    private val apiCallExecutor: ApiCallExecutor,
) : WalletRepository {
    private val mutableWalletUpdates = MutableSharedFlow<WalletAccount>(extraBufferCapacity = 1)
    override val walletUpdates: Flow<WalletAccount> = mutableWalletUpdates.asSharedFlow()

    override suspend fun getWallet(): DataResult<WalletAccount> =
        apiCallExecutor.execute(request = api::getWallet, transform = { it.toDomain() })
            .also { result ->
                if (result is DataResult.Success) mutableWalletUpdates.tryEmit(result.data)
            }

    override suspend fun getTransactions(
        page: Int,
        size: Int,
    ): DataResult<PagedResult<WalletTransaction>> =
        apiCallExecutor.execute(
            request = { api.getTransactions(page = page, size = size) },
            transform = { it.toDomain() },
        )
}
