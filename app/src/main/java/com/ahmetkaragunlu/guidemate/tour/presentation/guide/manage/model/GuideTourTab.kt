package com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.model

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.components.GuideMateTab

enum class GuideTourTab(
    override val titleResId: Int,
) : GuideMateTab {
    ACTIVE(R.string.tab_active),
    REVIEW(R.string.tab_review),
    PAST(R.string.tab_past),
}
