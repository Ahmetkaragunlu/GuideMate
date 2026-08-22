package com.ahmetkaragunlu.guidemate.home.presentation.guide.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.tour.domain.model.guide.GuideDashboard
import compose.icons.TablerIcons
import compose.icons.tablericons.Flag
import compose.icons.tablericons.Friends
import compose.icons.tablericons.Star

data class GuideStatistic(
    val icon: ImageVector,
    val value: String,
    val description: Int,
)

fun GuideDashboard.toDashboardStatistics(): List<GuideStatistic> =
    listOf(
        GuideStatistic(
            icon = TablerIcons.Flag,
            value = completedSessionCount.toString(),
            description = R.string.completed_tours,
        ),
        GuideStatistic(
            icon = TablerIcons.Friends,
            value = totalParticipantCount.toString(),
            description = R.string.total_participants,
        ),
        GuideStatistic(
            icon = TablerIcons.Star,
            value = averageRating.toString(),
            description = R.string.average_rating,
        ),
    )
