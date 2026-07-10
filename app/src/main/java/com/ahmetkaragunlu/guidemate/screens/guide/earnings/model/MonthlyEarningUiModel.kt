package com.ahmetkaragunlu.guidemate.screens.guide.earnings.model

import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

data class MonthlyEarningUiModel(
    val year: Int,
    val month: Int,
    val amount: Double,
)

fun MonthlyEarningUiModel.toPeriodLabel(locale: Locale = Locale.getDefault()): String =
    DateTimeFormatter
        .ofPattern("MMMM yyyy", locale)
        .format(YearMonth.of(year, month))
        .replaceFirstChar(Char::uppercaseChar)
