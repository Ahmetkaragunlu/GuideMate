package com.ahmetkaragunlu.guidemate.wallet.data.repository

import com.ahmetkaragunlu.guidemate.common.network.error.ApiErrorParser
import com.ahmetkaragunlu.guidemate.common.network.error.NetworkExceptionMapper
import com.ahmetkaragunlu.guidemate.common.network.model.ApiPageResponse
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.wallet.data.remote.api.WalletApi
import com.ahmetkaragunlu.guidemate.wallet.data.remote.model.WalletResponseDto
import com.ahmetkaragunlu.guidemate.wallet.data.remote.model.WalletTransactionResponseDto
import com.ahmetkaragunlu.guidemate.wallet.domain.model.WalletTransactionDirection
import com.ahmetkaragunlu.guidemate.wallet.domain.model.WalletTransactionType
import com.google.gson.Gson
import java.time.Instant
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class WalletRepositoryImplTest {
    @Test
    fun `maps canonical wallet balance and currency`() = runBlocking {
        val repository = createRepository(FakeWalletApi())

        val result = repository.getWallet()

        assertTrue(result is DataResult.Success)
        val wallet = (result as DataResult.Success).data
        assertEquals(125_000L, wallet.balanceMinor)
        assertEquals(110_000L, wallet.availableBalanceMinor)
        assertEquals("USD", wallet.currencyCode)
    }

    @Test
    fun `forwards pagination and maps transaction metadata`() = runBlocking {
        val api = FakeWalletApi()
        val repository = createRepository(api)

        val result = repository.getTransactions(page = 2, size = 25)

        assertTrue(result is DataResult.Success)
        val page = (result as DataResult.Success).data
        val transaction = page.items.single()
        assertEquals(2, api.requestedPage)
        assertEquals(25, api.requestedSize)
        assertEquals(2, page.page)
        assertEquals(5, page.totalPages)
        assertEquals(WalletTransactionDirection.CREDIT, transaction.direction)
        assertEquals(WalletTransactionType.GUIDE_EARNING, transaction.type)
        assertEquals("tour-session", transaction.referenceType)
        assertEquals("Boğaz Turu", transaction.referenceTitle)
    }

    private fun createRepository(api: WalletApi): WalletRepositoryImpl =
        WalletRepositoryImpl(
            api = api,
            apiErrorParser = ApiErrorParser(Gson()),
            networkExceptionMapper = NetworkExceptionMapper(),
        )

    private class FakeWalletApi : WalletApi {
        var requestedPage: Int? = null
        var requestedSize: Int? = null

        override suspend fun getWallet(): Response<WalletResponseDto> =
            Response.success(
                WalletResponseDto(
                    balanceMinor = 125_000,
                    availableBalanceMinor = 110_000,
                    currencyCode = "USD",
                ),
            )

        override suspend fun getTransactions(
            page: Int,
            size: Int,
        ): Response<ApiPageResponse<WalletTransactionResponseDto>> {
            requestedPage = page
            requestedSize = size
            return Response.success(
                ApiPageResponse(
                    content =
                        listOf(
                            WalletTransactionResponseDto(
                                transactionId = "transaction-1",
                                direction = "CREDIT",
                                type = "GUIDE_EARNING",
                                amountMinor = 15_000,
                                currencyCode = "USD",
                                referenceType = "tour-session",
                                referenceId = "session-1",
                                referenceTitle = "Boğaz Turu",
                                occurredAt = Instant.parse("2026-08-23T10:15:30Z"),
                            ),
                        ),
                    page = page,
                    size = size,
                    totalElements = 101,
                    totalPages = 5,
                    isFirst = false,
                    isLast = false,
                ),
            )
        }
    }
}
