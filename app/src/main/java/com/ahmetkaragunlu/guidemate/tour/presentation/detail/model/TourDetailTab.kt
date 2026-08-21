package com.ahmetkaragunlu.guidemate.tour.presentation.detail.model

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.components.GuideMateTab

enum class TourDetailTab(
    override val titleResId: Int,
) : GuideMateTab {
    DETAILS(R.string.guide_tour_publish_step4_tab_details),
    MEETING(R.string.guide_tour_publish_step4_tab_meeting),
    REVIEWS(R.string.guide_tour_publish_step4_tab_reviews),
}
