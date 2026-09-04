package com.ahmetkaragunlu.guidemate.home.presentation.guide

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.notification.presentation.components.notificationMessage
import com.ahmetkaragunlu.guidemate.notification.presentation.components.notificationRelativeTime
import com.ahmetkaragunlu.guidemate.notification.presentation.mapper.notificationIcon
import com.ahmetkaragunlu.guidemate.notification.presentation.model.NotificationUiModel

@Composable
internal fun RecentActivities(
    notifications: List<NotificationUiModel>,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val newestNotificationId = notifications.firstOrNull()?.id

    LaunchedEffect(newestNotificationId) {
        if (newestNotificationId != null) {
            listState.scrollToItem(0)
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_large)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
        border = BorderStroke(width = 1.dp, color = colorResource(R.color.border_color)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxWidth().padding(dimensionResource(R.dimen.spacing_medium)),
        ) {
            if (notifications.isEmpty()) {
                item(key = "recent-activities-empty") {
                    Text(
                        text = stringResource(R.string.notifications_empty),
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorResource(R.color.text_color),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(dimensionResource(R.dimen.spacing_medium)),
                    )
                }
            }
            itemsIndexed(
                items = notifications,
                key = { _, notification -> notification.id },
            ) { index, notification ->
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(dimensionResource(R.dimen.spacing_medium)),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = notification.type.notificationIcon(),
                            contentDescription = null,
                            tint = colorResource(R.color.notification_icon_color),
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = notificationMessage(notification),
                            style = MaterialTheme.typography.bodySmall,
                            color = colorResource(R.color.text_color),
                            modifier = Modifier.weight(1f),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            text = notificationRelativeTime(notification.occurredAtMillis),
                            style = MaterialTheme.typography.labelSmall,
                            color = colorResource(R.color.text_color),
                            modifier = Modifier.align(Alignment.Bottom),
                        )
                    }
                    if (index < notifications.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.spacing_medium)),
                            thickness = 0.5.dp,
                            color = colorResource(R.color.border_color),
                        )
                    }
                }
            }
        }
    }
}
