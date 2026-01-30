package com.ahmetkaragunlu.guidemate.screens.tourist.explore.model

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.tourist.shared.GuidemateTab

enum class ExploreTab(override val titleResId: Int) : GuidemateTab {
    TOURS(R.string.tab_tours),
    GUIDES(R.string.tab_guides)
}