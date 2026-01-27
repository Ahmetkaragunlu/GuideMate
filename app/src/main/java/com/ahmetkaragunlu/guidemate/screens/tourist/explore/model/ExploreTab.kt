package com.ahmetkaragunlu.guidemate.screens.tourist.explore.model

import androidx.annotation.StringRes
import com.ahmetkaragunlu.guidemate.R

enum class ExploreTab(@StringRes val titleResId: Int) {
    TOURS(R.string.tab_tours),
    GUIDES(R.string.tab_guides)
}