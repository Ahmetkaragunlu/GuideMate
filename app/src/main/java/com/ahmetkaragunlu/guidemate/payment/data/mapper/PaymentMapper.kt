package com.ahmetkaragunlu.guidemate.payment.data.mapper

import com.ahmetkaragunlu.guidemate.payment.data.remote.model.CheckoutCurrenciesResponseDto
import com.ahmetkaragunlu.guidemate.payment.data.remote.model.PaymentQuoteResponseDto
import com.ahmetkaragunlu.guidemate.payment.data.remote.model.PaymentResponseDto
import com.ahmetkaragunlu.guidemate.payment.domain.model.CheckoutCurrencies
import com.ahmetkaragunlu.guidemate.payment.domain.model.CheckoutCurrency
import com.ahmetkaragunlu.guidemate.payment.domain.model.Payment
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentMethod
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentPurpose
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentQuote
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentRefundStatus
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentReservationStatus
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentStatus

internal fun CheckoutCurrenciesResponseDto.toDomain(): CheckoutCurrencies =
    CheckoutCurrencies(
        baseCurrencyCode = baseCurrencyCode,
        chargeCurrencies =
            chargeCurrencies.map {
                CheckoutCurrency(
                    currencyCode = it.currencyCode,
                    fractionDigits = it.fractionDigits,
                )
            },
    )

internal fun PaymentQuoteResponseDto.toDomain(): PaymentQuote =
    PaymentQuote(
        id = quoteId,
        purpose = enumValueOf(purpose),
        baseAmountMinor = baseAmountMinor,
        baseCurrencyCode = baseCurrencyCode,
        chargeAmountMinor = chargeAmountMinor,
        chargeCurrencyCode = chargeCurrencyCode,
        fxRate = fxRate,
        rateSource = rateSource,
        rateDate = rateDate,
        quotedAt = quotedAt,
        expiresAt = expiresAt,
    )

internal fun PaymentResponseDto.toDomain(): Payment =
    Payment(
        id = paymentId,
        purpose = enumValueOf<PaymentPurpose>(purpose),
        method = enumValueOf<PaymentMethod>(method),
        status = enumValueOf<PaymentStatus>(paymentStatus),
        amountMinor = amountMinor,
        currencyCode = currencyCode,
        quoteId = quoteId,
        chargeAmountMinor = chargeAmountMinor,
        chargeCurrencyCode = chargeCurrencyCode,
        fxRate = fxRate,
        fxRateSource = fxRateSource,
        fxQuotedAt = fxQuotedAt,
        paymentPageUrl = paymentPageUrl,
        expiresAt = expiresAt,
        reservationId = reservationId,
        reservationStatus = reservationStatus?.let { enumValueOf<PaymentReservationStatus>(it) },
        refundId = refundId,
        refundStatus = refundStatus?.let { enumValueOf<PaymentRefundStatus>(it) },
        refundAmountMinor = refundAmountMinor,
        refundChargeAmountMinor = refundChargeAmountMinor,
        refundChargeCurrencyCode = refundChargeCurrencyCode,
        failureCode = failureCode,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
