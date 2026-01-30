package com.ahmetkaragunlu.guidemate.screens.tourist.trips

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.tourist.shared.GuidemateTab


enum class TripTab(override val titleResId: Int) : GuidemateTab {
    UPCOMING(R.string.tab_upcoming),
    PAST(R.string.tab_past)
}