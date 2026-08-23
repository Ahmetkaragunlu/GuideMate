package com.ahmetkaragunlu.guidemate.wallet.presentation.guide.earnings.model

import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

data class MonthlyEarningUiModel(
    val year: Int,
    val month: Int,
    val amountMinor: Long,
    val currencyCode: String,
)

fun MonthlyEarningUiModel.toPeriodLabel(locale: Locale = Locale.getDefault()): String =
    DateTimeFormatter
        .ofPattern("MMMM yyyy", locale)
        .format(YearMonth.of(year, month))
        .replaceFirstChar(Char::uppercaseChar)
