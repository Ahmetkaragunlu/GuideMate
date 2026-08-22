package com.ahmetkaragunlu.guidemate.tour.domain.model.guide

import com.ahmetkaragunlu.guidemate.tour.domain.model.Tour
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.TourSession

data class GuideTourDetails(
    val tour: Tour,
    val sessions: List<TourSession>,
) {
    fun session(sessionId: String): TourSession? = sessions.firstOrNull { it.id == sessionId }
}
