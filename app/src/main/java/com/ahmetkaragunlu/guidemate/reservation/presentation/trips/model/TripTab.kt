package com.ahmetkaragunlu.guidemate.reservation.presentation.trips.model

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.components.GuideMateTab

enum class TripTab(
    override val titleResId: Int,
) : GuideMateTab {
    UPCOMING(R.string.tab_upcoming),
    PAST(R.string.tab_past),
}
