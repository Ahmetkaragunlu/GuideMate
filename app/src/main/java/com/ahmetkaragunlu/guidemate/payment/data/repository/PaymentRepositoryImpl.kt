package com.ahmetkaragunlu.guidemate.payment.data.repository

import com.ahmetkaragunlu.guidemate.common.network.ApiCallExecutor
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.payment.data.local.PendingPaymentStorage
import com.ahmetkaragunlu.guidemate.payment.data.mapper.toDomain
import com.ahmetkaragunlu.guidemate.payment.data.remote.api.PaymentApi
import com.ahmetkaragunlu.guidemate.payment.data.remote.model.TourCheckoutRequestDto
import com.ahmetkaragunlu.guidemate.payment.data.remote.model.TourPaymentQuoteRequestDto
import com.ahmetkaragunlu.guidemate.payment.data.remote.model.WalletTopUpQuoteRequestDto
import com.ahmetkaragunlu.guidemate.payment.data.remote.model.WalletTopUpRequestDto
import com.ahmetkaragunlu.guidemate.payment.domain.model.CheckoutCurrencies
import com.ahmetkaragunlu.guidemate.payment.domain.model.CheckoutLocale
import com.ahmetkaragunlu.guidemate.payment.domain.model.Payment
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentMethod
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentQuote
import com.ahmetkaragunlu.guidemate.payment.domain.repository.PaymentRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class PaymentRepositoryImpl @Inject constructor(
    private val api: PaymentApi,
    private val pendingPaymentStorage: PendingPaymentStorage,
    private val apiCallExecutor: ApiCallExecutor,
) : PaymentRepository {
    override val pendingPaymentId: Flow<String?> = pendingPaymentStorage.paymentId

    override suspend fun getCheckoutCurrencies(): DataResult<CheckoutCurrencies> =
        apiCallExecutor.execute(api::getCheckoutCurrencies) { it.toDomain() }

    override suspend fun quoteTour(
        sessionId: String,
        participantCount: Int,
        chargeCurrencyCode: String,
    ): DataResult<PaymentQuote> =
        apiCallExecutor.execute(
            request = {
                api.quoteTour(
                    TourPaymentQuoteRequestDto(
                        sessionId = sessionId,
                        participantCount = participantCount,
                        chargeCurrencyCode = chargeCurrencyCode,
                    ),
                )
            },
            transform = { it.toDomain() },
        )

    override suspend fun quoteWalletTopUp(
        amountMinor: Long,
        chargeCurrencyCode: String,
    ): DataResult<PaymentQuote> =
        apiCallExecutor.execute(
            request = {
                api.quoteWalletTopUp(
                    WalletTopUpQuoteRequestDto(
                        amountMinor = amountMinor,
                        chargeCurrencyCode = chargeCurrencyCode,
                    ),
                )
            },
            transform = { it.toDomain() },
        )

    override suspend fun checkoutTour(
        sessionId: String,
        participantCount: Int,
        method: PaymentMethod,
        quoteId: String?,
        locale: CheckoutLocale,
        idempotencyKey: String,
    ): DataResult<Payment> =
        executeTrackedPayment {
            api.checkoutTour(
                idempotencyKey = idempotencyKey,
                request =
                    TourCheckoutRequestDto(
                        sessionId = sessionId,
                        participantCount = participantCount,
                        method = method.name,
                        quoteId = quoteId,
                        locale = locale.name,
                    ),
            )
        }

    override suspend fun checkoutWalletTopUp(
        quoteId: String,
        locale: CheckoutLocale,
        idempotencyKey: String,
    ): DataResult<Payment> =
        executeTrackedPayment {
            api.checkoutWalletTopUp(
                idempotencyKey = idempotencyKey,
                request = WalletTopUpRequestDto(quoteId = quoteId, locale = locale.name),
            )
        }

    override suspend fun getPayment(paymentId: String): DataResult<Payment> =
        executePayment { api.getPayment(paymentId) }

    override suspend fun cancelPayment(paymentId: String): DataResult<Payment> =
        executePayment { api.cancelPayment(paymentId) }

    override suspend fun clearPendingPayment(paymentId: String) {
        pendingPaymentStorage.clear(paymentId)
    }

    private suspend fun executeTrackedPayment(
        request: suspend () -> Response<com.ahmetkaragunlu.guidemate.payment.data.remote.model.PaymentResponseDto>,
    ): DataResult<Payment> =
        executePayment(request).also { result ->
            if (result is DataResult.Success) pendingPaymentStorage.save(result.data.id)
        }

    private suspend fun executePayment(
        request: suspend () -> Response<com.ahmetkaragunlu.guidemate.payment.data.remote.model.PaymentResponseDto>,
    ): DataResult<Payment> =
        apiCallExecutor.execute(request = request, transform = { it.toDomain() })
}
