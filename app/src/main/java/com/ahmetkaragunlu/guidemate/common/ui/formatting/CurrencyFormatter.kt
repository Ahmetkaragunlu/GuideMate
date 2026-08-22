package com.ahmetkaragunlu.guidemate.common.ui.formatting

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

const val PLATFORM_CURRENCY_CODE = "USD"

private val platformCurrency: Currency = Currency.getInstance(PLATFORM_CURRENCY_CODE)
private val currencyInputPattern = Regex("""^\d*(?:[.,]\d{0,2})?$""")

fun Long.toPlatformCurrencyFromMinorUnit(locale: Locale = Locale.getDefault()): String {
    return toCurrencyFromMinorUnit(
        currencyCode = PLATFORM_CURRENCY_CODE,
        locale = locale,
    )
}

fun Long.toCurrencyFromMinorUnit(
    currencyCode: String,
    locale: Locale = Locale.getDefault(),
): String {
    val currency = Currency.getInstance(currencyCode)
    val fractionDigits = currency.defaultFractionDigits
    val formatter =
        NumberFormat.getCurrencyInstance(locale).apply {
            this.currency = currency
            minimumFractionDigits = fractionDigits
            maximumFractionDigits = fractionDigits
        }
    return formatter.format(BigDecimal.valueOf(this, fractionDigits))
}

fun Long.toCurrencyInput(locale: Locale = Locale.getDefault()): String {
    val fractionDigits = platformCurrency.defaultFractionDigits
    val formatter =
        NumberFormat.getNumberInstance(locale).apply {
            isGroupingUsed = false
            minimumFractionDigits = 0
            maximumFractionDigits = fractionDigits
        }
    return formatter.format(BigDecimal.valueOf(this, fractionDigits))
}

fun String.isValidCurrencyInput(): Boolean = isEmpty() || currencyInputPattern.matches(this)

fun String.toCurrencyMinorUnitsOrNull(): Long? {
    if (!currencyInputPattern.matches(this) || this.isBlank()) return null

    val fractionDigits = platformCurrency.defaultFractionDigits
    return runCatching {
        replace(',', '.')
            .toBigDecimal()
            .movePointRight(fractionDigits)
            .longValueExact()
    }.getOrNull()
}

fun platformCurrencySymbol(locale: Locale = Locale.getDefault()): String =
    platformCurrency.getSymbol(locale)
