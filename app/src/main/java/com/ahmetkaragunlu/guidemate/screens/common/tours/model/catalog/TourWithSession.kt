package com.ahmetkaragunlu.guidemate.screens.common.tours.model.catalog

import com.ahmetkaragunlu.guidemate.screens.common.tours.model.Tour
import com.ahmetkaragunlu.guidemate.screens.common.tours.model.session.TourSession

data class TourWithSession(
    val tour: Tour,
    val session: TourSession,
)
