package com.ahmetkaragunlu.guidemate.screens.guide.notifications.components

import android.text.format.DateUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.components.toLocalCurrency
import com.ahmetkaragunlu.guidemate.screens.guide.notifications.model.GuideNotificationType
import com.ahmetkaragunlu.guidemate.screens.guide.notifications.model.GuideNotificationUiModel
import compose.icons.TablerIcons
import compose.icons.tablericons.CreditCard
import compose.icons.tablericons.Message
import compose.icons.tablericons.Star
import compose.icons.tablericons.Ticket

@Composable
fun GuideNotificationItem(
    notification: GuideNotificationUiModel,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(R.dimen.spacing_small)),
        verticalAlignment = Alignment.Top,
    ) {
        NotificationIcon(type = notification.type)
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_small)))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_tiny)),
        ) {
            Text(
                text = notificationMessage(notification),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (notification.isRead) FontWeight.Normal else FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = colorResource(R.color.text_color),
            )
            notification.commentPreview?.takeIf(String::isNotBlank)?.let { preview ->
                Text(
                    text = stringResource(R.string.guide_notification_comment_preview, preview),
                    style = MaterialTheme.typography.bodySmall,
                    color = colorResource(R.color.text_color),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            notification.rejectionReason?.takeIf(String::isNotBlank)?.let { reason ->
                Text(
                    text = stringResource(R.string.guide_notification_rejection_reason, reason),
                    style = MaterialTheme.typography.bodySmall,
                    color = colorResource(R.color.text_color),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Text(
                text = notificationRelativeTime(notification.occurredAtMillis),
                style = MaterialTheme.typography.labelSmall,
                color = colorResource(R.color.text_color),
            )
        }
        if (!notification.isRead) {
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_small)))
            Box(
                modifier =
                    Modifier
                        .padding(top = dimensionResource(R.dimen.spacing_tiny))
                        .size(dimensionResource(R.dimen.spacing_small))
                        .background(colorResource(R.color.brand_color), CircleShape),
            )
        }
    }
}

@Composable
private fun NotificationIcon(type: GuideNotificationType) {
    val icon = type.notificationIcon()
    Box(
        modifier =
            Modifier
                .size(40.dp)
                .background(
                    color = colorResource(R.color.brand_color).copy(alpha = 0.1f),
                    shape = CircleShape,
                ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = colorResource(R.color.brand_color),
            modifier = Modifier.size(20.dp),
        )
    }
}

@Composable
internal fun notificationMessage(notification: GuideNotificationUiModel): String =
    when (notification.type) {
        GuideNotificationType.TOUR_PURCHASED ->
            stringResource(
                R.string.guide_notification_tour_purchased,
                notification.actorName,
                notification.tourTitle,
            )

        GuideNotificationType.RATING_RECEIVED ->
            pluralStringResource(
                R.plurals.guide_notification_rating_received,
                notification.rating ?: 0,
                notification.actorName,
                notification.tourTitle,
                notification.rating ?: 0,
            )

        GuideNotificationType.WITHDRAWAL_COMPLETED ->
            stringResource(
                R.string.guide_notification_withdrawal_completed,
                (notification.amount ?: 0.0).toLocalCurrency(),
            )

        GuideNotificationType.COMMENT_RECEIVED ->
            stringResource(
                R.string.guide_notification_comment_received,
                notification.actorName,
                notification.tourTitle,
            )

        GuideNotificationType.TOUR_APPROVED ->
            stringResource(R.string.guide_notification_tour_approved, notification.tourTitle)

        GuideNotificationType.TOUR_REJECTED ->
            stringResource(R.string.guide_notification_tour_rejected, notification.tourTitle)

        GuideNotificationType.TOUR_COMPLETED ->
            stringResource(R.string.guide_notification_tour_completed, notification.tourTitle)
    }

@Composable
internal fun notificationRelativeTime(occurredAtMillis: Long): String =
    DateUtils.getRelativeTimeSpanString(
            occurredAtMillis,
            System.currentTimeMillis(),
            DateUtils.MINUTE_IN_MILLIS,
            DateUtils.FORMAT_ABBREV_RELATIVE,
        )
        .toString()

internal fun GuideNotificationType.notificationIcon(): ImageVector =
    when (this) {
        GuideNotificationType.TOUR_PURCHASED -> TablerIcons.Ticket
        GuideNotificationType.RATING_RECEIVED -> TablerIcons.Star
        GuideNotificationType.WITHDRAWAL_COMPLETED -> TablerIcons.CreditCard
        GuideNotificationType.COMMENT_RECEIVED -> TablerIcons.Message
        GuideNotificationType.TOUR_APPROVED,
        GuideNotificationType.TOUR_REJECTED,
        GuideNotificationType.TOUR_COMPLETED,
        -> TablerIcons.Ticket
    }
