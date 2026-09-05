package com.ahmetkaragunlu.guidemate.notification.presentation.tourist.settings

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.components.GuideMateContentState
import com.ahmetkaragunlu.guidemate.notification.presentation.settings.NotificationPreferencesUiState
import com.ahmetkaragunlu.guidemate.notification.presentation.settings.NotificationPreferencesViewModel
import com.ahmetkaragunlu.guidemate.notification.presentation.settings.NotificationSettingsSectionTitle
import com.ahmetkaragunlu.guidemate.notification.presentation.settings.NotificationSettingsSwitchRow

@Composable
fun TouristNotificationSettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: NotificationPreferencesViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(uiState.userMessage) {
        uiState.userMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            viewModel.onMessageShown()
        }
    }

    GuideMateContentState(
        state = uiState.loadState,
        onRetry = viewModel::refresh,
        errorMessage = uiState.userMessage,
        modifier = modifier,
    ) {
        TouristNotificationSettingsContent(
            uiState = uiState,
            onUpcomingReminderChanged = viewModel::updateUpcomingTourReminders,
            onGuideMessagesChanged = viewModel::updateChatMessages,
            onReservationUpdatesChanged = viewModel::updateReservationUpdates,
            onReviewRequestsChanged = viewModel::updateReviewRequests,
            onPaymentsAndRefundsChanged = viewModel::updatePaymentsAndEarnings,
        )
    }
}

@Composable
private fun TouristNotificationSettingsContent(
    modifier: Modifier = Modifier,
    uiState: NotificationPreferencesUiState,
    onUpcomingReminderChanged: (Boolean) -> Unit,
    onGuideMessagesChanged: (Boolean) -> Unit,
    onReservationUpdatesChanged: (Boolean) -> Unit,
    onReviewRequestsChanged: (Boolean) -> Unit,
    onPaymentsAndRefundsChanged: (Boolean) -> Unit,
) {
    val preferences = checkNotNull(uiState.preferences)
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
    ) {
        NotificationSettingsSectionTitle(
            title = stringResource(R.string.notification_tours_and_reservations),
        )

        NotificationSettingsSwitchRow(
            title = stringResource(id = R.string.upcoming_tour_reminders),
            subtitle = stringResource(id = R.string.upcoming_tour_reminders_desc),
            isChecked = preferences.upcomingTourRemindersEnabled,
            onCheckedChange = onUpcomingReminderChanged,
            enabled = !uiState.isUpdating,
        )
        NotificationSettingsSwitchRow(
            title = stringResource(id = R.string.guide_messages),
            subtitle = stringResource(id = R.string.guide_messages_desc),
            isChecked = preferences.chatMessagesEnabled,
            onCheckedChange = onGuideMessagesChanged,
            enabled = !uiState.isUpdating,
        )
        NotificationSettingsSwitchRow(
            title = stringResource(id = R.string.reservation_updates),
            subtitle = stringResource(id = R.string.reservation_updates_desc),
            isChecked = preferences.reservationUpdatesEnabled,
            onCheckedChange = onReservationUpdatesChanged,
            enabled = !uiState.isUpdating,
        )

        HorizontalDivider(
            thickness = 1.dp,
            color = Color(0xFFEEEEEE),
            modifier = Modifier.padding(vertical = 12.dp),
        )

        NotificationSettingsSectionTitle(
            title = stringResource(R.string.account_and_interaction),
        )

        NotificationSettingsSwitchRow(
            title = stringResource(id = R.string.review_requests),
            subtitle = stringResource(id = R.string.review_requests_desc),
            isChecked = preferences.reviewRequestsEnabled,
            onCheckedChange = onReviewRequestsChanged,
            enabled = !uiState.isUpdating,
        )
        NotificationSettingsSwitchRow(
            title = stringResource(id = R.string.payments_and_refunds),
            subtitle = stringResource(id = R.string.payments_and_refunds_desc),
            isChecked = preferences.paymentsAndEarningsEnabled,
            onCheckedChange = onPaymentsAndRefundsChanged,
            enabled = !uiState.isUpdating,
        )
        NotificationSettingsSwitchRow(
            title = stringResource(id = R.string.security_alerts),
            subtitle = stringResource(id = R.string.security_alerts_desc),
            isChecked = preferences.securityAlertsEnabled,
            onCheckedChange = {},
            enabled = false,
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))
    }
}
