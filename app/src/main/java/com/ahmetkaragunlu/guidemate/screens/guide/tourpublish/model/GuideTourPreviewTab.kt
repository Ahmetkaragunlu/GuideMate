package com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.model

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.common.tab.GuideMateTab

enum class GuideTourPreviewTab(
    override val titleResId: Int,
) : GuideMateTab {
    DETAILS(R.string.guide_tour_publish_step4_tab_details),
    MEETING(R.string.guide_tour_publish_step4_tab_meeting),
    REVIEWS(R.string.guide_tour_publish_step4_tab_reviews),
}

