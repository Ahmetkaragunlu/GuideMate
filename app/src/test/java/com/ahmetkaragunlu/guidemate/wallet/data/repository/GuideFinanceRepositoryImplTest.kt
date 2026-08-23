package com.ahmetkaragunlu.guidemate.wallet.data.repository

import com.ahmetkaragunlu.guidemate.common.network.error.ApiErrorParser
import com.ahmetkaragunlu.guidemate.common.network.error.NetworkExceptionMapper
import com.ahmetkaragunlu.guidemate.common.network.model.ApiPageResponse
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.wallet.data.remote.api.GuideFinanceApi
import com.ahmetkaragunlu.guidemate.wallet.data.remote.model.AddBankAccountRequestDto
import com.ahmetkaragunlu.guidemate.wallet.data.remote.model.BankAccountResponseDto
import com.ahmetkaragunlu.guidemate.wallet.data.remote.model.GuideEarningResponseDto
import com.ahmetkaragunlu.guidemate.wallet.data.remote.model.MonthlyGuideEarningResponseDto
import com.ahmetkaragunlu.guidemate.wallet.data.remote.model.WithdrawalRequestDto
import com.ahmetkaragunlu.guidemate.wallet.data.remote.model.WithdrawalResponseDto
import com.ahmetkaragunlu.guidemate.wallet.domain.model.PayoutMode
import com.ahmetkaragunlu.guidemate.wallet.domain.model.WithdrawalStatus
import com.google.gson.Gson
import java.time.Instant
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class GuideFinanceRepositoryImplTest {
    @Test
    fun `maps backend monthly earning projection without client aggregation`() = runBlocking {
        val repository = createRepository(FakeGuideFinanceApi())

        val result = repository.getMonthlyEarnings(year = 2026)

        assertTrue(result is DataResult.Success)
        val earning = (result as DataResult.Success).data.single()
        assertEquals(2026, earning.year)
        assertEquals(8, earning.month)
        assertEquals(45_000L, earning.netEarningsMinor)
        assertEquals("USD", earning.currencyCode)
    }

    @Test
    fun `forwards earning pagination parameters`() = runBlocking {
        val api = FakeGuideFinanceApi()
        val repository = createRepository(api)

        val result = repository.getEarnings(year = 2025, page = 2, size = 25)

        assertTrue(result is DataResult.Success)
        assertEquals(2025, api.earningsYear)
        assertEquals(2, api.earningsPage)
        assertEquals(25, api.earningsSize)
    }

    @Test
    fun `sends withdrawal target amount and idempotency key`() = runBlocking {
        val api = FakeGuideFinanceApi()
        val repository = createRepository(api)

        val result =
            repository.requestWithdrawal(
                bankAccountId = "bank-account-1",
                amountMinor = 30_000,
                idempotencyKey = "withdrawal-key-1",
            )

        assertTrue(result is DataResult.Success)
        assertEquals("withdrawal-key-1", api.withdrawalIdempotencyKey)
        assertEquals("bank-account-1", api.withdrawalRequest?.bankAccountId)
        assertEquals(30_000L, api.withdrawalRequest?.amountMinor)
        val withdrawal = (result as DataResult.Success).data
        assertEquals(WithdrawalStatus.COMPLETED, withdrawal.status)
        assertEquals(PayoutMode.SIMULATED, withdrawal.payoutMode)
    }

    private fun createRepository(api: GuideFinanceApi): GuideFinanceRepositoryImpl =
        GuideFinanceRepositoryImpl(
            api = api,
            apiErrorParser = ApiErrorParser(Gson()),
            networkExceptionMapper = NetworkExceptionMapper(),
        )

    private class FakeGuideFinanceApi : GuideFinanceApi {
        var earningsYear: Int? = null
        var earningsPage: Int? = null
        var earningsSize: Int? = null
        var withdrawalIdempotencyKey: String? = null
        var withdrawalRequest: WithdrawalRequestDto? = null

        override suspend fun getEarnings(
            year: Int,
            page: Int,
            size: Int,
        ): Response<ApiPageResponse<GuideEarningResponseDto>> {
            earningsYear = year
            earningsPage = page
            earningsSize = size
            return Response.success(emptyPage(page, size))
        }

        override suspend fun getMonthlyEarnings(
            year: Int,
        ): Response<List<MonthlyGuideEarningResponseDto>> =
            Response.success(
                listOf(
                    MonthlyGuideEarningResponseDto(
                        year = year,
                        month = 8,
                        netEarningsMinor = 45_000,
                        currencyCode = "USD",
                    ),
                ),
            )

        override suspend fun getBankAccounts(
            page: Int,
            size: Int,
        ): Response<ApiPageResponse<BankAccountResponseDto>> = Response.success(emptyPage(page, size))

        override suspend fun addBankAccount(
            request: AddBankAccountRequestDto,
        ): Response<BankAccountResponseDto> = Response.success(bankAccount())

        override suspend fun makeDefaultBankAccount(
            bankAccountId: String,
        ): Response<BankAccountResponseDto> = Response.success(bankAccount())

        override suspend fun deleteBankAccount(bankAccountId: String): Response<Unit> =
            Response.success(Unit)

        override suspend fun getWithdrawals(
            page: Int,
            size: Int,
        ): Response<ApiPageResponse<WithdrawalResponseDto>> = Response.success(emptyPage(page, size))

        override suspend fun requestWithdrawal(
            idempotencyKey: String,
            request: WithdrawalRequestDto,
        ): Response<WithdrawalResponseDto> {
            withdrawalIdempotencyKey = idempotencyKey
            withdrawalRequest = request
            return Response.success(withdrawal())
        }

        private fun bankAccount(): BankAccountResponseDto =
            BankAccountResponseDto(
                bankAccountId = "bank-account-1",
                maskedIban = "TR** **** 1234",
                bankCode = "00010",
                bankName = "Banka",
                accountHolderName = "Ahmet Karagunlu",
                defaultAccount = true,
                createdAt = TEST_INSTANT,
            )

        private fun withdrawal(): WithdrawalResponseDto =
            WithdrawalResponseDto(
                withdrawalId = "withdrawal-1",
                bankAccountId = "bank-account-1",
                maskedIban = "TR** **** 1234",
                amountMinor = 30_000,
                currencyCode = "USD",
                status = "COMPLETED",
                payoutMode = "SIMULATED",
                requestedAt = TEST_INSTANT,
                completedAt = TEST_INSTANT,
                failureCode = null,
            )

        private fun <T> emptyPage(
            page: Int,
            size: Int,
        ): ApiPageResponse<T> =
            ApiPageResponse(
                content = emptyList(),
                page = page,
                size = size,
                totalElements = 0,
                totalPages = 0,
                isFirst = true,
                isLast = true,
            )

        private companion object {
            val TEST_INSTANT: Instant = Instant.parse("2026-08-23T10:15:30Z")
        }
    }
}
