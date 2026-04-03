package com.ahmetkaragunlu.guidemate.screens.tourist.explore.model

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.common.tab.GuideMateTab

enum class ExploreTab(
    override val titleResId: Int,
) : GuideMateTab {
    TOURS(R.string.tab_tours),
    GUIDES(R.string.tab_guides),
}
