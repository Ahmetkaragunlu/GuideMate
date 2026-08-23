package com.ahmetkaragunlu.guidemate.payment.domain.repository

import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.payment.domain.model.CheckoutCurrencies
import com.ahmetkaragunlu.guidemate.payment.domain.model.CheckoutLocale
import com.ahmetkaragunlu.guidemate.payment.domain.model.Payment
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentMethod
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentQuote
import kotlinx.coroutines.flow.Flow

interface PaymentRepository {
    val pendingPaymentId: Flow<String?>

    suspend fun getCheckoutCurrencies(): DataResult<CheckoutCurrencies>

    suspend fun quoteTour(
        sessionId: String,
        participantCount: Int,
        chargeCurrencyCode: String,
    ): DataResult<PaymentQuote>

    suspend fun quoteWalletTopUp(
        amountMinor: Long,
        chargeCurrencyCode: String,
    ): DataResult<PaymentQuote>

    suspend fun checkoutTour(
        sessionId: String,
        participantCount: Int,
        method: PaymentMethod,
        quoteId: String?,
        locale: CheckoutLocale,
        idempotencyKey: String,
    ): DataResult<Payment>

    suspend fun checkoutWalletTopUp(
        quoteId: String,
        locale: CheckoutLocale,
        idempotencyKey: String,
    ): DataResult<Payment>

    suspend fun getPayment(paymentId: String): DataResult<Payment>

    suspend fun cancelPayment(paymentId: String): DataResult<Payment>

    suspend fun clearPendingPayment(paymentId: String)
}
