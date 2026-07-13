package com.ahmetkaragunlu.guidemate.screens.guide.tours.model

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.common.tab.GuideMateTab

enum class GuideTourTab(
    override val titleResId: Int,
) : GuideMateTab {
    ACTIVE(R.string.tab_active),
    REVIEW(R.string.tab_review),
    PAST(R.string.tab_past),
}
