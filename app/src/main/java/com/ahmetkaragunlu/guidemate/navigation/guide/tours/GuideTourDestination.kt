package com.ahmetkaragunlu.guidemate.navigation.guide.tours

import kotlinx.serialization.Serializable

object GuideTourDestination {
    @Serializable data object MyTours

    @Serializable
    data class Detail(
        val tourId: String,
        val sessionId: String,
    )

    @Serializable
    data class Edit(
        val tourId: String,
        val sessionId: String,
    )

    @Serializable data object PublishStep1

    @Serializable data object PublishStep2

    @Serializable data object PublishStep3

    @Serializable data object PublishStep4
}
