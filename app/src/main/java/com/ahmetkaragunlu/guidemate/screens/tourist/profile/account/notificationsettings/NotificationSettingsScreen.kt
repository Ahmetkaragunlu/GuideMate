package com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.notificationsettings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.notificationsettings.model.NotificationSettingsUiState
import com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.notificationsettings.viewmodel.NotificationSettingsViewModel

@Composable
fun NotificationSettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: NotificationSettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NotificationSettingsContent(
        modifier = modifier,
        uiState = uiState,
        onUpcomingReminderChanged = viewModel::onUpcomingReminderChanged,
        onGuideMessagesChanged = viewModel::onGuideMessagesChanged,
        onReservationUpdatesChanged = viewModel::onReservationUpdatesChanged,
        onReviewRequestsChanged = viewModel::onReviewRequestsChanged,
    )
}

@Composable
private fun NotificationSettingsContent(
    modifier: Modifier = Modifier,
    uiState: NotificationSettingsUiState,
    onUpcomingReminderChanged: (Boolean) -> Unit,
    onGuideMessagesChanged: (Boolean) -> Unit,
    onReservationUpdatesChanged: (Boolean) -> Unit,
    onReviewRequestsChanged: (Boolean) -> Unit,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
    ) {
        SectionTitle(title = stringResource(id = R.string.notification_tours_and_reservations))

        SettingsSwitchRow(
            title = stringResource(id = R.string.upcoming_tour_reminders),
            subtitle = stringResource(id = R.string.upcoming_tour_reminders_desc),
            isChecked = uiState.upcomingReminder,
            onCheckedChange = onUpcomingReminderChanged,
        )
        SettingsSwitchRow(
            title = stringResource(id = R.string.guide_messages),
            subtitle = stringResource(id = R.string.guide_messages_desc),
            isChecked = uiState.guideMessages,
            onCheckedChange = onGuideMessagesChanged,
        )
        SettingsSwitchRow(
            title = stringResource(id = R.string.reservation_updates),
            subtitle = stringResource(id = R.string.reservation_updates_desc),
            isChecked = uiState.reservationUpdates,
            onCheckedChange = onReservationUpdatesChanged,
        )

        HorizontalDivider(
            thickness = 1.dp,
            color = Color(0xFFEEEEEE),
            modifier = Modifier.padding(vertical = 12.dp),
        )

        SectionTitle(title = stringResource(id = R.string.account_and_interaction))

        SettingsSwitchRow(
            title = stringResource(id = R.string.review_requests),
            subtitle = stringResource(id = R.string.review_requests_desc),
            isChecked = uiState.reviewRequests,
            onCheckedChange = onReviewRequestsChanged,
        )
        SettingsSwitchRow(
            title = stringResource(id = R.string.security_alerts),
            subtitle = stringResource(id = R.string.security_alerts_desc),
            isChecked = uiState.securityAlerts,
            onCheckedChange = {},
            enabled = false,
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))
    }
}

@Composable
private fun SectionTitle(
    title: String,
) {
    Text(
        text = title,
        color = colorResource(id = R.color.brand_color),
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier.padding(start = 20.dp, top = 20.dp),
    )
}

@Composable
private fun SettingsSwitchRow(
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .padding(end = 16.dp),
        ) {
            Text(
                text = title,
                color = if (enabled) Color.Unspecified else colorResource(id = R.color.text_color),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = subtitle,
                color = colorResource(id = R.color.text_color),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 2.dp),
            )
        }

        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors =
                SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                    checkedTrackColor = colorResource(id = R.color.brand_color),
                    uncheckedThumbColor = MaterialTheme.colorScheme.onPrimary,
                    uncheckedTrackColor = Color(0xFFE0E0E0),
                    disabledCheckedTrackColor = colorResource(id = R.color.brand_color).copy(alpha = 0.5f),
                    disabledCheckedThumbColor = Color(0xFFF5F5F5),
                ),
        )
    }
}
