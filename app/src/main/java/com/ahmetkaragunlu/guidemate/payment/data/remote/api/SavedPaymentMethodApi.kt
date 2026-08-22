package com.ahmetkaragunlu.guidemate.payment.data.remote.api

import com.ahmetkaragunlu.guidemate.payment.data.remote.model.SavedPaymentMethodResponseDto
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface SavedPaymentMethodApi {
    @GET("api/v1/payment-methods/cards")
    suspend fun getCards(): Response<List<SavedPaymentMethodResponseDto>>

    @PUT("api/v1/payment-methods/cards/{savedPaymentMethodId}/default")
    suspend fun makeDefault(
        @Path("savedPaymentMethodId") savedPaymentMethodId: String,
    ): Response<SavedPaymentMethodResponseDto>

    @DELETE("api/v1/payment-methods/cards/{savedPaymentMethodId}")
    suspend fun delete(
        @Path("savedPaymentMethodId") savedPaymentMethodId: String,
    ): Response<Unit>
}
