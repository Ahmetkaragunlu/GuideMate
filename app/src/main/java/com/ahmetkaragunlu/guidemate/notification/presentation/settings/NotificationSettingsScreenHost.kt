package com.ahmetkaragunlu.guidemate.notification.presentation.settings

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.common.ui.components.GuideMateContentState

@Composable
internal fun NotificationSettingsScreenHost(
    viewModel: NotificationPreferencesViewModel,
    modifier: Modifier = Modifier,
    content: @Composable (NotificationPreferencesUiState) -> Unit,
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
        content(uiState)
    }
}
