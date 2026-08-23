package com.ahmetkaragunlu.guidemate.payment.data.remote.api

import com.ahmetkaragunlu.guidemate.payment.data.remote.model.CheckoutCurrenciesResponseDto
import com.ahmetkaragunlu.guidemate.payment.data.remote.model.PaymentQuoteResponseDto
import com.ahmetkaragunlu.guidemate.payment.data.remote.model.PaymentResponseDto
import com.ahmetkaragunlu.guidemate.payment.data.remote.model.TourCheckoutRequestDto
import com.ahmetkaragunlu.guidemate.payment.data.remote.model.TourPaymentQuoteRequestDto
import com.ahmetkaragunlu.guidemate.payment.data.remote.model.WalletTopUpQuoteRequestDto
import com.ahmetkaragunlu.guidemate.payment.data.remote.model.WalletTopUpRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface PaymentApi {
    @GET("api/v1/payments/checkout/currencies")
    suspend fun getCheckoutCurrencies(): Response<CheckoutCurrenciesResponseDto>

    @POST("api/v1/payments/checkout/tour/quote")
    suspend fun quoteTour(
        @Body request: TourPaymentQuoteRequestDto,
    ): Response<PaymentQuoteResponseDto>

    @POST("api/v1/payments/checkout/wallet-top-up/quote")
    suspend fun quoteWalletTopUp(
        @Body request: WalletTopUpQuoteRequestDto,
    ): Response<PaymentQuoteResponseDto>

    @POST("api/v1/payments/checkout/tour")
    suspend fun checkoutTour(
        @Header("Idempotency-Key") idempotencyKey: String,
        @Body request: TourCheckoutRequestDto,
    ): Response<PaymentResponseDto>

    @POST("api/v1/payments/checkout/wallet-top-up")
    suspend fun checkoutWalletTopUp(
        @Header("Idempotency-Key") idempotencyKey: String,
        @Body request: WalletTopUpRequestDto,
    ): Response<PaymentResponseDto>

    @GET("api/v1/payments/{paymentId}")
    suspend fun getPayment(
        @Path("paymentId") paymentId: String,
    ): Response<PaymentResponseDto>

    @POST("api/v1/payments/{paymentId}/cancel")
    suspend fun cancelPayment(
        @Path("paymentId") paymentId: String,
    ): Response<PaymentResponseDto>
}

