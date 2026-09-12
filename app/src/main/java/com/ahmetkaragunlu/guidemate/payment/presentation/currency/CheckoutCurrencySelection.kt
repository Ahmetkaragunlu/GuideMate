package com.ahmetkaragunlu.guidemate.payment.presentation.currency

import com.ahmetkaragunlu.guidemate.payment.domain.model.CheckoutCurrencies
import java.util.Currency
import java.util.Locale

fun CheckoutCurrencies.preferredChargeCurrencyCode(
    deviceCurrencyCode: String? = currentDeviceCurrencyCode(),
): String? =
    chargeCurrencies.firstOrNull { it.currencyCode == deviceCurrencyCode }?.currencyCode
        ?: chargeCurrencies.firstOrNull { it.currencyCode == baseCurrencyCode }?.currencyCode
        ?: chargeCurrencies.firstOrNull()?.currencyCode

private fun currentDeviceCurrencyCode(): String? =
    runCatching { Currency.getInstance(Locale.getDefault()).currencyCode }.getOrNull()
