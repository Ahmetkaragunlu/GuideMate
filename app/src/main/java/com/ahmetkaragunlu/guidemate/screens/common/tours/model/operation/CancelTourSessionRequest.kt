package com.ahmetkaragunlu.guidemate.screens.common.tours.model.operation

data class CancelTourSessionRequest(
    val sessionId: String,
    val reason: String,
)
