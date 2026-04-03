package com.ahmetkaragunlu.guidemate.screens.tourist.trips

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.common.tab.GuideMateTab

enum class TripTab(
    override val titleResId: Int,
) : GuideMateTab {
    UPCOMING(R.string.tab_upcoming),
    PAST(R.string.tab_past),
}
