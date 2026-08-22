package com.ahmetkaragunlu.guidemate.wallet.data.remote.api

import com.ahmetkaragunlu.guidemate.common.network.model.ApiPageResponse
import com.ahmetkaragunlu.guidemate.wallet.data.remote.model.WalletResponseDto
import com.ahmetkaragunlu.guidemate.wallet.data.remote.model.WalletTransactionResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WalletApi {
    @GET("api/v1/wallet")
    suspend fun getWallet(): Response<WalletResponseDto>

    @GET("api/v1/wallet/transactions")
    suspend fun getTransactions(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<ApiPageResponse<WalletTransactionResponseDto>>
}
