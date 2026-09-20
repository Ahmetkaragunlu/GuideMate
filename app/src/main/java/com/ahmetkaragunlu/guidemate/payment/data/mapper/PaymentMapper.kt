package com.ahmetkaragunlu.guidemate.payment.data.mapper

import com.ahmetkaragunlu.guidemate.payment.data.remote.model.CheckoutCurrenciesResponseDto
import com.ahmetkaragunlu.guidemate.payment.data.remote.model.PaymentQuoteResponseDto
import com.ahmetkaragunlu.guidemate.payment.data.remote.model.PaymentResponseDto
import com.ahmetkaragunlu.guidemate.payment.domain.model.CheckoutCurrencies
import com.ahmetkaragunlu.guidemate.payment.domain.model.CheckoutCurrency
import com.ahmetkaragunlu.guidemate.payment.domain.model.HostedPaymentDetails
import com.ahmetkaragunlu.guidemate.payment.domain.model.Payment
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentChargeDetails
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentMethod
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentPurpose
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentQuote
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentRefund
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentRefundStatus
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentReservation
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
        chargeDetails = toChargeDetails(),
        hostedPayment = toHostedPaymentDetails(),
        reservation = toPaymentReservation(),
        refund = toPaymentRefund(),
        failureCode = failureCode,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

private fun PaymentResponseDto.toChargeDetails(): PaymentChargeDetails? =
    if (
        quoteId == null &&
            chargeAmountMinor == null &&
            chargeCurrencyCode == null &&
            fxRate == null &&
            fxRateSource == null &&
            fxQuotedAt == null
    ) {
        null
    } else {
        PaymentChargeDetails(
            quoteId = quoteId,
            amountMinor = chargeAmountMinor,
            currencyCode = chargeCurrencyCode,
            fxRate = fxRate,
            fxRateSource = fxRateSource,
            fxQuotedAt = fxQuotedAt,
        )
    }

private fun PaymentResponseDto.toHostedPaymentDetails(): HostedPaymentDetails? =
    if (paymentPageUrl == null && expiresAt == null) {
        null
    } else {
        HostedPaymentDetails(
            pageUrl = paymentPageUrl,
            expiresAt = expiresAt,
        )
    }

private fun PaymentResponseDto.toPaymentReservation(): PaymentReservation? =
    if (reservationId == null && reservationStatus == null) {
        null
    } else {
        PaymentReservation(
            id = reservationId,
            status = reservationStatus?.let { enumValueOf<PaymentReservationStatus>(it) },
        )
    }

private fun PaymentResponseDto.toPaymentRefund(): PaymentRefund? =
    if (
        refundId == null &&
            refundStatus == null &&
            refundAmountMinor == null &&
            refundChargeAmountMinor == null &&
            refundChargeCurrencyCode == null
    ) {
        null
    } else {
        PaymentRefund(
            id = refundId,
            status = refundStatus?.let { enumValueOf<PaymentRefundStatus>(it) },
            amountMinor = refundAmountMinor,
            chargeAmountMinor = refundChargeAmountMinor,
            chargeCurrencyCode = refundChargeCurrencyCode,
        )
    }
