package com.ahmetkaragunlu.guidemate.screens.guide.home.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.ahmetkaragunlu.guidemate.R
import compose.icons.TablerIcons
import compose.icons.tablericons.Flag
import compose.icons.tablericons.Friends
import compose.icons.tablericons.Star


data class GuideStatistic(
    val icon: ImageVector,
    val value: String,
    val description: Int
)




val dashboardStats = listOf(
    GuideStatistic(
        icon = TablerIcons.Flag,
        value = "45",
        description = R.string.completed_tours
    ),
    GuideStatistic(
        icon = TablerIcons.Friends,
        value = "320",
        description = R.string.total_participants
    ),
    GuideStatistic(
        icon = TablerIcons.Star,
        value = "4.9",
        description = R.string.average_rating
    )
)