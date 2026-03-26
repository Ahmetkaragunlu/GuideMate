package com.ahmetkaragunlu.guidemate.screens.guide.tours.model

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.tourist.shared.GuidemateTab

enum class GuideTourTab(
    override val titleResId: Int,
) : GuidemateTab {
    ACTIVE(R.string.tab_active),
    PAST(R.string.tab_past),
}
