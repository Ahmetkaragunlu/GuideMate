package com.ahmetkaragunlu.guidemate.screens.common.tours.detail.model

import androidx.annotation.StringRes
import com.ahmetkaragunlu.guidemate.R

enum class TourDetailStatus(
    @param:StringRes val labelResId: Int,
) {
    COMPLETED(R.string.tour_status_completed),
    CANCELLED(R.string.tour_status_cancelled),
}
