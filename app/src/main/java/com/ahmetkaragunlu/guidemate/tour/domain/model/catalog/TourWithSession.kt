package com.ahmetkaragunlu.guidemate.tour.domain.model.catalog

import com.ahmetkaragunlu.guidemate.tour.domain.model.Tour
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.TourSession

data class TourWithSession(
    val tour: Tour,
    val session: TourSession,
)
