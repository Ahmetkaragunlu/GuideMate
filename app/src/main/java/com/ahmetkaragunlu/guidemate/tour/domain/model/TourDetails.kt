package com.ahmetkaragunlu.guidemate.tour.domain.model

import com.ahmetkaragunlu.guidemate.tour.domain.model.session.TourSession

data class TourDetails(
    val tour: Tour,
    val sessions: List<TourSession>,
) {
    fun session(sessionId: String): TourSession? = sessions.firstOrNull { it.id == sessionId }
}
