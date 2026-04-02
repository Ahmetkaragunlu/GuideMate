package com.ahmetkaragunlu.guidemate.components

import java.text.NumberFormat
import java.util.Locale

fun Double.toLocalCurrency(): String {
    val format = NumberFormat.getCurrencyInstance(Locale.getDefault())
    return format.format(this)
}
