package com.ahmetkaragunlu.guidemate.payment.data.remote.api

import com.ahmetkaragunlu.guidemate.payment.data.remote.model.response.CheckoutCurrenciesResponse
import com.ahmetkaragunlu.guidemate.payment.data.remote.model.response.PaymentQuoteResponse
import com.ahmetkaragunlu.guidemate.payment.data.remote.model.response.PaymentResponse
import com.ahmetkaragunlu.guidemate.payment.data.remote.model.request.TourCheckoutRequest
import com.ahmetkaragunlu.guidemate.payment.data.remote.model.request.TourPaymentQuoteRequest
import com.ahmetkaragunlu.guidemate.payment.data.remote.model.request.WalletTopUpQuoteRequest
import com.ahmetkaragunlu.guidemate.payment.data.remote.model.request.WalletTopUpRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface PaymentApi {
    @GET("api/v1/payments/checkout/currencies")
    suspend fun getCheckoutCurrencies(): Response<CheckoutCurrenciesResponse>

    @POST("api/v1/payments/checkout/tour/quote")
    suspend fun quoteTour(
        @Body request: TourPaymentQuoteRequest,
    ): Response<PaymentQuoteResponse>

    @POST("api/v1/payments/checkout/wallet-top-up/quote")
    suspend fun quoteWalletTopUp(
        @Body request: WalletTopUpQuoteRequest,
    ): Response<PaymentQuoteResponse>

    @POST("api/v1/payments/checkout/tour")
    suspend fun checkoutTour(
        @Header("Idempotency-Key") idempotencyKey: String,
        @Body request: TourCheckoutRequest,
    ): Response<PaymentResponse>

    @POST("api/v1/payments/checkout/wallet-top-up")
    suspend fun checkoutWalletTopUp(
        @Header("Idempotency-Key") idempotencyKey: String,
        @Body request: WalletTopUpRequest,
    ): Response<PaymentResponse>

    @GET("api/v1/payments/{paymentId}")
    suspend fun getPayment(
        @Path("paymentId") paymentId: String,
    ): Response<PaymentResponse>

    @POST("api/v1/payments/{paymentId}/cancel")
    suspend fun cancelPayment(
        @Path("paymentId") paymentId: String,
    ): Response<PaymentResponse>
}

