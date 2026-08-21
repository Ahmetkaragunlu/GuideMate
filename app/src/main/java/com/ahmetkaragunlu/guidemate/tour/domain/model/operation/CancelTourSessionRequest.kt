package com.ahmetkaragunlu.guidemate.tour.domain.model.operation

data class CancelTourSessionRequest(
    val sessionId: String,
    val reason: String,
)
