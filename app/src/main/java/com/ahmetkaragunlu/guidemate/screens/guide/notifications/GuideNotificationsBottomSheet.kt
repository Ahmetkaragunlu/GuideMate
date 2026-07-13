package com.ahmetkaragunlu.guidemate.screens.guide.notifications

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.guide.notifications.components.GuideNotificationItem
import com.ahmetkaragunlu.guidemate.screens.guide.notifications.model.GuideNotificationsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuideNotificationsBottomSheet(
    isVisible: Boolean,
    uiState: GuideNotificationsUiState,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (!isVisible) return

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier =
                modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.spacing_medium))
                    .padding(bottom = dimensionResource(R.dimen.spacing_large)),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.notifications),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = stringResource(R.string.guide_notifications_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = colorResource(R.color.text_color),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = dimensionResource(R.dimen.spacing_tiny)),
            )
            HorizontalDivider(
                modifier = Modifier.padding(top = dimensionResource(R.dimen.spacing_medium)),
                color = Color(0xFFEEEDF1),
            )

            if (uiState.notifications.isEmpty()) {
                Text(
                    text = stringResource(R.string.guide_notifications_empty),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorResource(R.color.text_color),
                    modifier = Modifier.padding(vertical = dimensionResource(R.dimen.spacing_large)),
                )
            } else {
                LazyColumn(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .heightIn(max = 480.dp),
                ) {
                    itemsIndexed(
                        items = uiState.notifications,
                        key = { _, notification -> notification.id },
                    ) { index, notification ->
                        GuideNotificationItem(notification = notification)
                        if (index < uiState.notifications.lastIndex) {
                            HorizontalDivider(color = Color(0xFFEEEDF1))
                        }
                    }
                }
            }
        }
    }
}
