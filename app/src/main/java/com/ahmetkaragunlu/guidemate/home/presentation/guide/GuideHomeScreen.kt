package com.ahmetkaragunlu.guidemate.home.presentation.guide

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.components.GuideMateContentState
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.home.presentation.guide.model.GuideHomeUiState
import com.ahmetkaragunlu.guidemate.notification.presentation.model.NotificationUiModel

@Composable
fun GuideHomeScreen(
    uiState: GuideHomeUiState,
    recentNotifications: List<NotificationUiModel>,
    onNavigateToEarnings: () -> Unit,
    onRetryPerformance: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(dimensionResource(R.dimen.spacing_medium)),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        GuideMateContentState(
            state = uiState.dashboardLoadState,
            onRetry = onRetryPerformance,
            modifier = Modifier.fillMaxWidth().height(168.dp),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = dimensionResource(R.dimen.spacing_medium)),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
            ) {
                uiState.dashboardStats.forEach { stat ->
                    GuideStatCard(
                        stat = stat,
                        modifier = Modifier.weight(1f).height(152.dp),
                    )
                }
            }
        }
        if (uiState.dashboardLoadState == ContentLoadState.CONTENT) {
            MonthlyEarningsCard(
                amountMinor = uiState.currentMonthEarningsMinor,
                onClick = onNavigateToEarnings,
            )
            GuideHomeSectionTitle(text = stringResource(R.string.tour_status))
            TourStatusCard(
                pendingCount = uiState.pendingCount,
                activeCount = uiState.activeCount,
            )
        }
        GuideHomeSectionTitle(text = stringResource(R.string.recent_activities))
        RecentActivities(
            notifications = recentNotifications,
            modifier = Modifier.heightIn(max = 200.dp),
        )
    }
}

@Composable
private fun GuideHomeSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = colorResource(R.color.text_color),
        modifier = Modifier.padding(start = dimensionResource(R.dimen.spacing_small)),
    )
}
