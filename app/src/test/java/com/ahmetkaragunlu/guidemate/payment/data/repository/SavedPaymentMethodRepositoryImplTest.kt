package com.ahmetkaragunlu.guidemate.payment.data.repository

import com.ahmetkaragunlu.guidemate.common.network.error.ApiErrorParser
import com.ahmetkaragunlu.guidemate.common.network.error.NetworkExceptionMapper
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.payment.data.remote.api.SavedPaymentMethodApi
import com.ahmetkaragunlu.guidemate.payment.data.remote.model.SavedPaymentMethodResponseDto
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class SavedPaymentMethodRepositoryImplTest {
    @Test
    fun `maps only provider backed safe card metadata`() = runBlocking {
        val repository = createRepository(FakeSavedPaymentMethodApi())

        val result = repository.getSavedPaymentMethods()

        assertTrue(result is DataResult.Success)
        val card = (result as DataResult.Success).data.single()
        assertEquals("saved-card-1", card.id)
        assertEquals("Visa", card.cardAssociation)
        assertEquals("1234", card.lastFourDigits)
        assertEquals(12, card.expiryMonth)
        assertEquals(2030, card.expiryYear)
        assertNull(card.cardHolderName)
        assertTrue(card.isDefault)
    }

    @Test
    fun `default mutation forwards id and emits refresh signal`() = runBlocking {
        val api = FakeSavedPaymentMethodApi()
        val repository = createRepository(api)
        val change =
            async(start = CoroutineStart.UNDISPATCHED) {
                repository.paymentMethodChanges.first()
            }

        val result = repository.makeDefault("saved-card-2")

        assertTrue(result is DataResult.Success)
        assertEquals("saved-card-2", api.defaultCardId)
        change.await()
    }

    @Test
    fun `delete mutation forwards id and emits refresh signal`() = runBlocking {
        val api = FakeSavedPaymentMethodApi()
        val repository = createRepository(api)
        val change =
            async(start = CoroutineStart.UNDISPATCHED) {
                repository.paymentMethodChanges.first()
            }

        val result = repository.delete("saved-card-1")

        assertTrue(result is DataResult.Success)
        assertEquals("saved-card-1", api.deletedCardId)
        change.await()
    }

    private fun createRepository(api: SavedPaymentMethodApi): SavedPaymentMethodRepositoryImpl =
        SavedPaymentMethodRepositoryImpl(
            api = api,
            apiErrorParser = ApiErrorParser(Gson()),
            networkExceptionMapper = NetworkExceptionMapper(),
        )

    private class FakeSavedPaymentMethodApi : SavedPaymentMethodApi {
        var defaultCardId: String? = null
        var deletedCardId: String? = null

        override suspend fun getCards(): Response<List<SavedPaymentMethodResponseDto>> =
            Response.success(listOf(cardResponse()))

        override suspend fun makeDefault(
            savedPaymentMethodId: String,
        ): Response<SavedPaymentMethodResponseDto> {
            defaultCardId = savedPaymentMethodId
            return Response.success(cardResponse(id = savedPaymentMethodId))
        }

        override suspend fun delete(savedPaymentMethodId: String): Response<Unit> {
            deletedCardId = savedPaymentMethodId
            return Response.success(Unit)
        }

        private fun cardResponse(id: String = "saved-card-1"): SavedPaymentMethodResponseDto =
            SavedPaymentMethodResponseDto(
                savedPaymentMethodId = id,
                alias = "Seyahat kartım",
                bankName = "Test Bankası",
                bankCode = "001",
                cardFamily = "Bonus",
                cardAssociation = "Visa",
                cardType = "CREDIT_CARD",
                lastFourDigits = "1234",
                cardHolderName = null,
                expiryMonth = 12,
                expiryYear = 2030,
                defaultMethod = true,
            )
    }
}
