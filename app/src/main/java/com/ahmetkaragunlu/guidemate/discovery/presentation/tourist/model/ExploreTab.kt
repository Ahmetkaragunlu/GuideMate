package com.ahmetkaragunlu.guidemate.discovery.presentation.tourist.model

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.components.GuideMateTab

enum class ExploreTab(
    override val titleResId: Int,
) : GuideMateTab {
    TOURS(R.string.tab_tours),
    GUIDES(R.string.tab_guides),
}
