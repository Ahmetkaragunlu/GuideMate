package com.ahmetkaragunlu.guidemate.screens.guide.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import compose.icons.TablerIcons
import compose.icons.tablericons.Flag

@Composable
fun ProfileStatsRow(
    guideLevel: String,
    rating: Double,
    tourCount: Int,
    onGuideLevelInfoClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        StatCard(
            icon = Icons.Rounded.EmojiEvents,
            title = guideLevel,
            showInfoIcon = true,
            onInfoClick = onGuideLevelInfoClick,
        )

        StatCard(
            icon = Icons.Rounded.Star,
            title = stringResource(R.string.rating_format, rating.toString()),
        )

        StatCard(
            icon = TablerIcons.Flag,
            iconTint = colorResource(R.color.brand_color),
            title = stringResource(R.string.tour_count_format, tourCount.toString()),
        )
    }
}

@Composable
private fun RowScope.StatCard(
    icon: ImageVector,
    title: String,
    iconTint: Color = Color(0xFFFFD700),
    showInfoIcon: Boolean = false,
    onInfoClick: (() -> Unit)? = null,
) {
    Card(
        modifier =
            Modifier
                .weight(1f)
                .height(120.dp),
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_large)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(contentAlignment = Alignment.BottomEnd) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(36.dp),
                )
                if (showInfoIcon) {
                    Icon(
                        imageVector = Icons.Rounded.Info,
                        contentDescription = stringResource(R.string.info),
                        tint = Color.LightGray,
                        modifier =
                            Modifier
                                .size(20.dp)
                                .offset(x = 6.dp, y = 4.dp)
                                .background(Color.White, CircleShape)
                                .clip(CircleShape)
                                .clickable { onInfoClick?.invoke() },
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.text_color),
            )
        }
    }
}
