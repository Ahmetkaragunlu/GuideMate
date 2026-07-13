package com.ahmetkaragunlu.guidemate.screens.common.tours.detail.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.ahmetkaragunlu.guidemate.R
import compose.icons.TablerIcons
import compose.icons.tablericons.Rocket

enum class TourDetailMode(
    @param:StringRes val primaryActionTextResId: Int?,
    val primaryActionIcon: ImageVector? = null,
    val showPreviewBanner: Boolean = false,
    val showGuideInfo: Boolean = false,
) {
    PREVIEW(
        primaryActionTextResId = R.string.send_for_review,
        primaryActionIcon = TablerIcons.Rocket,
        showPreviewBanner = true,
        showGuideInfo = true,
    ),
    GUIDE_ACTIVE(primaryActionTextResId = R.string.cancel_tour),
    GUIDE_PAST(primaryActionTextResId = R.string.republish_tour),
    GUIDE_REVIEW(primaryActionTextResId = null),
    TOURIST(
        primaryActionTextResId = R.string.book_tour,
        showGuideInfo = true,
    ),
}
