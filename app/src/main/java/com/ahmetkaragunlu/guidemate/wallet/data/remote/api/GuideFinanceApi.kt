package com.ahmetkaragunlu.guidemate.wallet.data.remote.api

import com.ahmetkaragunlu.guidemate.common.network.model.response.ApiPageResponse
import com.ahmetkaragunlu.guidemate.wallet.data.remote.model.request.AddBankAccountRequest
import com.ahmetkaragunlu.guidemate.wallet.data.remote.model.response.BankAccountResponse
import com.ahmetkaragunlu.guidemate.wallet.data.remote.model.response.GuideEarningResponse
import com.ahmetkaragunlu.guidemate.wallet.data.remote.model.response.MonthlyGuideEarningResponse
import com.ahmetkaragunlu.guidemate.wallet.data.remote.model.request.WithdrawalRequest
import com.ahmetkaragunlu.guidemate.wallet.data.remote.model.response.WithdrawalResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GuideFinanceApi {
    @GET("api/v1/guides/me/earnings")
    suspend fun getEarnings(
        @Query("year") year: Int,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<ApiPageResponse<GuideEarningResponse>>

    @GET("api/v1/guides/me/earnings/monthly")
    suspend fun getMonthlyEarnings(
        @Query("year") year: Int,
    ): Response<List<MonthlyGuideEarningResponse>>

    @GET("api/v1/guides/me/bank-accounts")
    suspend fun getBankAccounts(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<ApiPageResponse<BankAccountResponse>>

    @POST("api/v1/guides/me/bank-accounts")
    suspend fun addBankAccount(
        @Body request: AddBankAccountRequest,
    ): Response<BankAccountResponse>

    @POST("api/v1/guides/me/bank-accounts/{bankAccountId}/default")
    suspend fun makeDefaultBankAccount(
        @Path("bankAccountId") bankAccountId: String,
    ): Response<BankAccountResponse>

    @DELETE("api/v1/guides/me/bank-accounts/{bankAccountId}")
    suspend fun deleteBankAccount(
        @Path("bankAccountId") bankAccountId: String,
    ): Response<Unit>

    @GET("api/v1/guides/me/withdrawals")
    suspend fun getWithdrawals(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<ApiPageResponse<WithdrawalResponse>>

    @POST("api/v1/guides/me/withdrawals")
    suspend fun requestWithdrawal(
        @Header("Idempotency-Key") idempotencyKey: String,
        @Body request: WithdrawalRequest,
    ): Response<WithdrawalResponse>
}
