package com.ahmetkaragunlu.guidemate.navigation.tourist

import kotlinx.serialization.Serializable

object TouristDestination {
    @Serializable data object Home

    @Serializable data object Explore

    @Serializable data object Trips

    @Serializable data object Chat

    @Serializable data object Profile

    @Serializable data object Filter

    @Serializable
    data class TourDetail(
        val sessionId: String,
    )

    @Serializable
    data class ReservationDetail(
        val reservationId: String,
    )

    @Serializable
    data class GuideProfile(
        val guideId: Long,
    )

    @Serializable
    data class GuideTours(
        val guideId: Long,
    )
}
