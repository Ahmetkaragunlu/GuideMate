package com.ahmetkaragunlu.guidemate.profile.presentation.level.model

import androidx.annotation.StringRes
import com.ahmetkaragunlu.guidemate.R

enum class GuideLevelViewerType(
    @param:StringRes val activeTagResId: Int,
) {
    OWNER(activeTagResId = R.string.guide_level_current_level_owner),
    VISITOR(activeTagResId = R.string.guide_level_current_level_visitor),
}
