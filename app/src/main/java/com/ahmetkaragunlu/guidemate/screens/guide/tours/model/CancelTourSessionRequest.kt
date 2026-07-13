package com.ahmetkaragunlu.guidemate.screens.guide.tours.model

data class CancelTourSessionRequest(
    val sessionId: String,
    val reason: String,
)
