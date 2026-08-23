package com.ahmetkaragunlu.guidemate.wallet.data.remote.api

import com.ahmetkaragunlu.guidemate.common.network.model.ApiPageResponse
import com.ahmetkaragunlu.guidemate.wallet.data.remote.model.AddBankAccountRequestDto
import com.ahmetkaragunlu.guidemate.wallet.data.remote.model.BankAccountResponseDto
import com.ahmetkaragunlu.guidemate.wallet.data.remote.model.GuideEarningResponseDto
import com.ahmetkaragunlu.guidemate.wallet.data.remote.model.MonthlyGuideEarningResponseDto
import com.ahmetkaragunlu.guidemate.wallet.data.remote.model.WithdrawalRequestDto
import com.ahmetkaragunlu.guidemate.wallet.data.remote.model.WithdrawalResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GuideFinanceApi {
    @GET("api/v1/guide/earnings")
    suspend fun getEarnings(
        @Query("year") year: Int,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<ApiPageResponse<GuideEarningResponseDto>>

    @GET("api/v1/guide/earnings/monthly")
    suspend fun getMonthlyEarnings(
        @Query("year") year: Int,
    ): Response<List<MonthlyGuideEarningResponseDto>>

    @GET("api/v1/guide/bank-accounts")
    suspend fun getBankAccounts(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<ApiPageResponse<BankAccountResponseDto>>

    @POST("api/v1/guide/bank-accounts")
    suspend fun addBankAccount(
        @Body request: AddBankAccountRequestDto,
    ): Response<BankAccountResponseDto>

    @POST("api/v1/guide/bank-accounts/{bankAccountId}/default")
    suspend fun makeDefaultBankAccount(
        @Path("bankAccountId") bankAccountId: String,
    ): Response<BankAccountResponseDto>

    @DELETE("api/v1/guide/bank-accounts/{bankAccountId}")
    suspend fun deleteBankAccount(
        @Path("bankAccountId") bankAccountId: String,
    ): Response<Unit>

    @GET("api/v1/guide/withdrawals")
    suspend fun getWithdrawals(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<ApiPageResponse<WithdrawalResponseDto>>

    @POST("api/v1/guide/withdrawals")
    suspend fun requestWithdrawal(
        @Header("Idempotency-Key") idempotencyKey: String,
        @Body request: WithdrawalRequestDto,
    ): Response<WithdrawalResponseDto>
}
