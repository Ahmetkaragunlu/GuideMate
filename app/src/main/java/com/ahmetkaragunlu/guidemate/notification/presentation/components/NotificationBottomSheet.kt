package com.ahmetkaragunlu.guidemate.notification.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.components.GuideMateContentState
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationNavigationTarget
import com.ahmetkaragunlu.guidemate.notification.presentation.model.NotificationUiState
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationBottomSheet(
    isVisible: Boolean,
    uiState: NotificationUiState,
    onDismiss: () -> Unit,
    onNotificationClick: (NotificationNavigationTarget) -> Unit,
    onMarkAllRead: () -> Unit,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (!isVisible) return

    LaunchedEffect(Unit) { onRefresh() }

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
            NotificationSheetHeader(
                showMarkAllRead = uiState.unreadCount > 0,
                isMarkingAllRead = uiState.isMarkingAllRead,
                onMarkAllRead = onMarkAllRead,
            )
            Text(
                text = stringResource(R.string.notifications_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = colorResource(R.color.text_color),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = dimensionResource(R.dimen.spacing_tiny)),
            )
            HorizontalDivider(
                modifier = Modifier.padding(top = dimensionResource(R.dimen.spacing_medium)),
                color = colorResource(R.color.border_color),
            )

            GuideMateContentState(
                state = uiState.loadState,
                onRetry = onRefresh,
                errorMessage = uiState.errorMessage,
                modifier = Modifier.fillMaxWidth().height(240.dp),
            ) {
                NotificationSheetContent(
                    uiState = uiState,
                    onNotificationClick = onNotificationClick,
                    onLoadMore = onLoadMore,
                )
            }
        }
    }
}

@Composable
private fun NotificationSheetHeader(
    showMarkAllRead: Boolean,
    isMarkingAllRead: Boolean,
    onMarkAllRead: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.notifications),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center),
        )
        if (showMarkAllRead) {
            TextButton(
                onClick = onMarkAllRead,
                enabled = !isMarkingAllRead,
                modifier = Modifier.align(Alignment.CenterEnd),
            ) {
                Text(
                    text = stringResource(R.string.notification_mark_all_read),
                    color = colorResource(R.color.brand_color),
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
    }
}

@Composable
private fun NotificationSheetContent(
    uiState: NotificationUiState,
    onNotificationClick: (NotificationNavigationTarget) -> Unit,
    onLoadMore: () -> Unit,
) {
    if (uiState.notifications.isEmpty()) {
        Text(
            text = stringResource(R.string.notifications_empty),
            style = MaterialTheme.typography.bodyMedium,
            color = colorResource(R.color.text_color),
            modifier = Modifier.padding(vertical = dimensionResource(R.dimen.spacing_large)),
        )
        return
    }

    val listState = rememberLazyListState()
    LaunchedEffect(listState, uiState.hasMore, uiState.isLoadingMore) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .distinctUntilChanged()
            .collect { lastVisibleIndex ->
                if (
                    lastVisibleIndex != null &&
                        lastVisibleIndex >= uiState.notifications.lastIndex - 2 &&
                        uiState.hasMore &&
                        !uiState.isLoadingMore
                ) {
                    onLoadMore()
                }
            }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxWidth().heightIn(max = 480.dp),
    ) {
        itemsIndexed(
            items = uiState.notifications,
            key = { _, notification -> notification.id },
        ) { index, notification ->
            NotificationItem(
                notification = notification,
                onClick = { onNotificationClick(notification.navigationTarget) },
            )
            if (index < uiState.notifications.lastIndex) {
                HorizontalDivider(color = colorResource(R.color.border_color))
            }
        }
        if (uiState.isLoadingMore) {
            item(key = "notification-loading-more") {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(dimensionResource(R.dimen.spacing_medium)),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = colorResource(R.color.brand_color),
                    )
                }
            }
        }
    }
}
