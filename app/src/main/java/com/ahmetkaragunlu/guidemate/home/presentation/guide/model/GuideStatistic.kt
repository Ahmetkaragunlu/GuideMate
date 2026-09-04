package com.ahmetkaragunlu.guidemate.home.presentation.guide.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.formatting.toRatingText
import com.ahmetkaragunlu.guidemate.tour.domain.model.guide.GuideDashboard
import compose.icons.TablerIcons
import compose.icons.tablericons.Flag
import compose.icons.tablericons.Friends
import compose.icons.tablericons.Star

data class GuideStatistic(
    val icon: ImageVector,
    val value: String,
    @param:StringRes val titleLineOneResId: Int,
    @param:StringRes val titleLineTwoResId: Int? = null,
    val usesRatingTint: Boolean = false,
)

fun GuideDashboard.toDashboardStatistics(): List<GuideStatistic> =
    listOf(
        GuideStatistic(
            icon = TablerIcons.Flag,
            value = completedSessionCount.toString(),
            titleLineOneResId = R.string.stat_completed,
            titleLineTwoResId = R.string.stat_tour,
        ),
        GuideStatistic(
            icon = TablerIcons.Friends,
            value = totalParticipantCount.toString(),
            titleLineOneResId = R.string.total_participants,
        ),
        GuideStatistic(
            icon = TablerIcons.Star,
            value = averageRating.toRatingText(),
            titleLineOneResId = R.string.stat_average,
            titleLineTwoResId = R.string.stat_rating,
            usesRatingTint = true,
        ),
    )
