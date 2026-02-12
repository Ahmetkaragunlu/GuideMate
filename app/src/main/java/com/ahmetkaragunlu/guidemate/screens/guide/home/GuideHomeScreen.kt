package com.ahmetkaragunlu.guidemate.screens.guide.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.guide.home.model.GuideStatistic
import com.ahmetkaragunlu.guidemate.screens.guide.home.model.RecentActivity
import com.ahmetkaragunlu.guidemate.screens.guide.home.model.dashboardStats
import com.ahmetkaragunlu.guidemate.screens.guide.home.model.recentActivities
import compose.icons.TablerIcons
import compose.icons.tablericons.ArrowRight
import compose.icons.tablericons.Flag


@Composable
fun GuideHomeScreen(modifier: Modifier = Modifier) {


    //Mock Data
    val monthlyEarnings = "12.500$"
    val pendingCount = "1"

    val activeCount = "3"

    Column(
        modifier = modifier
            .fillMaxSize().verticalScroll(rememberScrollState())
            .padding(dimensionResource(R.dimen.spacing_medium)),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(R.dimen.spacing_medium)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
        ) {
            dashboardStats.forEach { stat ->
                GuideStatCard(
                    stat = stat, modifier = Modifier
                        .weight(1f)
                        .height(152.dp)
                )
            }
        }
        MonthlyEarningsCard(earnings = monthlyEarnings)
        Text(
            text = stringResource(R.string.tour_status),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.text_color),
            modifier = Modifier.padding(start = dimensionResource(R.dimen.spacing_small))
        )
        TourStatusCard(
            pendingCount = pendingCount,
            activeCount = activeCount
        )
        Text(
            text = stringResource(R.string.recent_activities),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.text_color),
            modifier = Modifier.padding(start = dimensionResource(R.dimen.spacing_small))
        )
        RecentActivities(activities = recentActivities, modifier = Modifier.heightIn(max = 200.dp))

    }
}


@Composable
private fun GuideStatCard(
    stat: GuideStatistic,
    modifier: Modifier = Modifier
) {
    val isStarIcon = stat.description == R.string.average_rating

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_large)),
        border = BorderStroke(width = 1.dp, color = Color(0xFFeeedf1)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = dimensionResource(R.dimen.spacing_medium)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Icon(
                imageVector = stat.icon,
                contentDescription = null,
                tint = if (isStarIcon) Color(0xFFFFC107) else colorResource(R.color.brand_color)
            )
            Text(
                text = stat.value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = stringResource(id = stat.description),
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = colorResource(R.color.text_color)
            )

        }
    }
}


@Composable
private fun MonthlyEarningsCard(
    earnings: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFf2f2fd)),
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.spacing_medium)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.monthly_earnings),
                color = colorResource(R.color.text_color),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = earnings,
                color = Color(0xFF888ded),
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_tiny)))
            Icon(
                imageVector = TablerIcons.ArrowRight,
                contentDescription = null,
                tint = Color(0xFF888ded)
            )
        }
    }
}


@Composable
private fun TourStatusCard(
    modifier: Modifier = Modifier,
    pendingCount: String,
    activeCount: String
) {
    Row(
        modifier = modifier.fillMaxWidth().height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
    ) {
        //Active Tours
        StatusItemCard(
            count = activeCount,
            label = stringResource(R.string.active_tours),
            indicatorColor = Color.Green,
            modifier = Modifier.weight(1f).fillMaxHeight()
        )
        //Pending Tours
        StatusItemCard(
            count = pendingCount,
            label = stringResource(R.string.pending_tours),
            indicatorColor = Color(0xFFFF9800),
            modifier = Modifier.weight(1f).fillMaxHeight()
        )
    }
}

@Composable
private fun StatusItemCard(
    count: String,
    label: String,
    indicatorColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_large)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
        border = BorderStroke(width = 1.dp, color = Color(0xFFeeedf1)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.spacing_medium)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(color = indicatorColor, shape = CircleShape)
                )
                Text(
                    text = count,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = colorResource(R.color.text_color),
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
private fun RecentActivities(
    activities: List<RecentActivity>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_large)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
        border = BorderStroke(width = 1.dp, color = Color(0xFFeeedf1)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth().padding(dimensionResource(R.dimen.spacing_medium))
        ) {
            itemsIndexed(activities) { index, item ->
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dimensionResource(R.dimen.spacing_medium)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = item.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = colorResource(R.color.text_color),
                            modifier = Modifier.weight(1f),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = item.time,
                            style = MaterialTheme.typography.bodySmall,
                            color = colorResource(R.color.text_color),
                        )
                    }

                    if (index < activities.lastIndex) {
                        androidx.compose.material3.HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            thickness = 0.5.dp,
                            color = Color(0xFFeeedf1)
                        )
                    }
                }
            }
        }
    }
}