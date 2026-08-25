package com.ahmetkaragunlu.guidemate.home.presentation.guide

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.ahmetkaragunlu.guidemate.common.ui.formatting.toPlatformCurrencyFromMinorUnit
import com.ahmetkaragunlu.guidemate.home.presentation.guide.model.GuideStatistic
import compose.icons.TablerIcons
import compose.icons.tablericons.ArrowRight

@Composable
internal fun GuideStatCard(
    stat: GuideStatistic,
    modifier: Modifier = Modifier,
) {
    val isStarIcon = stat.description == R.string.average_rating
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_large)),
        border = BorderStroke(width = 1.dp, color = colorResource(R.color.border_color)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(dimensionResource(R.dimen.spacing_medium)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
        ) {
            Icon(
                imageVector = stat.icon,
                contentDescription = null,
                tint = if (isStarIcon) Color(0xFFFFC107) else colorResource(R.color.brand_color),
            )
            Text(text = stat.value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(
                text = stringResource(id = stat.description),
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = colorResource(R.color.text_color),
            )
        }
    }
}

@Composable
internal fun MonthlyEarningsCard(
    amountMinor: Long,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFf2f2fd)),
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(dimensionResource(R.dimen.spacing_medium)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.this_month_earnings),
                color = colorResource(R.color.text_color),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelLarge,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = amountMinor.toPlatformCurrencyFromMinorUnit(),
                color = Color(0xFF888ded),
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_tiny)))
            Icon(imageVector = TablerIcons.ArrowRight, contentDescription = null, tint = Color(0xFF888ded))
        }
    }
}

@Composable
internal fun TourStatusCard(
    modifier: Modifier = Modifier,
    pendingCount: Long,
    activeCount: Long,
) {
    Row(
        modifier = modifier.fillMaxWidth().height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
    ) {
        StatusItemCard(
            count = activeCount,
            label = stringResource(R.string.active_tours),
            indicatorColor = Color.Green,
            modifier = Modifier.weight(1f).fillMaxHeight(),
        )
        StatusItemCard(
            count = pendingCount,
            label = stringResource(R.string.pending_tours),
            indicatorColor = Color(0xFFFF9800),
            modifier = Modifier.weight(1f).fillMaxHeight(),
        )
    }
}

@Composable
private fun StatusItemCard(
    count: Long,
    label: String,
    indicatorColor: Color,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_large)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
        border = BorderStroke(width = 1.dp, color = colorResource(R.color.border_color)),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(dimensionResource(R.dimen.spacing_medium)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(modifier = Modifier.size(12.dp).background(color = indicatorColor, shape = CircleShape))
                Text(text = count.toString(), style = MaterialTheme.typography.bodyLarge)
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = colorResource(R.color.text_color),
                textAlign = TextAlign.Center,
            )
        }
    }
}
