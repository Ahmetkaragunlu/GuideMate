package com.ahmetkaragunlu.guidemate.wallet.domain.model

import java.time.Instant

data class GuideEarning(
    val id: String,
    val reservationId: String,
    val grossMinor: Long,
    val platformFeeMinor: Long,
    val netMinor: Long,
    val currencyCode: String,
    val status: GuideEarningStatus,
    val availableAt: Instant?,
    val createdAt: Instant,
)

enum class GuideEarningStatus {
    PENDING,
    AVAILABLE,
    REVERSED,
}
