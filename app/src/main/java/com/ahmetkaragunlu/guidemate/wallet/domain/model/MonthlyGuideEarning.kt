package com.ahmetkaragunlu.guidemate.wallet.domain.model

data class MonthlyGuideEarning(
    val year: Int,
    val month: Int,
    val netEarningsMinor: Long,
    val currencyCode: String,
)
