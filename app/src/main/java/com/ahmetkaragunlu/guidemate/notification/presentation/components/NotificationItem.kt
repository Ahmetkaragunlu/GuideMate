package com.ahmetkaragunlu.guidemate.notification.presentation.components

import android.text.format.DateUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.formatting.toPlatformCurrencyFromMinorUnit
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationType
import com.ahmetkaragunlu.guidemate.notification.presentation.mapper.notificationIcon
import com.ahmetkaragunlu.guidemate.notification.presentation.model.NotificationUiModel

@Composable
fun NotificationItem(
    notification: NotificationUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
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
            notification.messagePreview?.takeIf(String::isNotBlank)?.let { preview ->
                NotificationPreview(preview)
            }
            notification.commentPreview?.takeIf(String::isNotBlank)?.let { preview ->
                NotificationPreview(preview)
            }
            notification.rejectionReason?.takeIf(String::isNotBlank)?.let { reason ->
                Text(
                    text = stringResource(R.string.notification_rejection_reason, reason),
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
private fun NotificationPreview(preview: String) {
    Text(
        text = stringResource(R.string.notification_preview, preview),
        style = MaterialTheme.typography.bodySmall,
        color = colorResource(R.color.text_color),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun NotificationIcon(type: NotificationType) {
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
            imageVector = type.notificationIcon(),
            contentDescription = null,
            tint = colorResource(R.color.brand_color),
            modifier = Modifier.size(20.dp),
        )
    }
}

@Composable
fun notificationMessage(notification: NotificationUiModel): String =
    when (notification.type) {
        NotificationType.TOUR_PURCHASED ->
            notification.actorAndTourMessage(
                specific = R.string.notification_tour_purchased,
                fallback = R.string.notification_tour_purchased_generic,
            )
        NotificationType.RATING_RECEIVED ->
            if (notification.actorName.isNotBlank() && notification.tourTitle.isNotBlank()) {
                pluralStringResource(
                    R.plurals.notification_rating_received,
                    notification.rating ?: 0,
                    notification.actorName,
                    notification.tourTitle,
                    notification.rating ?: 0,
                )
            } else {
                stringResource(R.string.notification_rating_received_generic)
            }
        NotificationType.WITHDRAWAL_COMPLETED ->
            notification.amountMinor?.let { amount ->
                stringResource(
                    R.string.notification_withdrawal_completed,
                    amount.toPlatformCurrencyFromMinorUnit(),
                )
            } ?: stringResource(R.string.notification_withdrawal_completed_generic)
        NotificationType.COMMENT_RECEIVED ->
            notification.actorAndTourMessage(
                specific = R.string.notification_comment_received,
                fallback = R.string.notification_comment_received_generic,
            )
        NotificationType.TOUR_APPROVED ->
            notification.tourMessage(
                specific = R.string.notification_tour_approved,
                fallback = R.string.notification_tour_approved_generic,
            )
        NotificationType.TOUR_REJECTED ->
            notification.tourMessage(
                specific = R.string.notification_tour_rejected,
                fallback = R.string.notification_tour_rejected_generic,
            )
        NotificationType.TOUR_CHANGE_APPROVED ->
            stringResource(R.string.notification_tour_change_approved)
        NotificationType.TOUR_CHANGE_REJECTED ->
            stringResource(R.string.notification_tour_change_rejected)
        NotificationType.RESERVATION_CONFIRMED ->
            stringResource(R.string.notification_reservation_confirmed)
        NotificationType.RESERVATION_CANCELLED ->
            stringResource(R.string.notification_reservation_cancelled)
        NotificationType.TOUR_CANCELLED -> stringResource(R.string.notification_tour_cancelled)
        NotificationType.TOUR_COMPLETED ->
            notification.tourMessage(
                specific = R.string.notification_tour_completed,
                fallback = R.string.notification_tour_completed_generic,
            )
        NotificationType.REVIEW_REQUEST -> stringResource(R.string.notification_review_request)
        NotificationType.PAYMENT_SUCCEEDED ->
            stringResource(R.string.notification_payment_succeeded)
        NotificationType.PAYMENT_FAILED -> stringResource(R.string.notification_payment_failed)
        NotificationType.REFUND_REQUESTED -> stringResource(R.string.notification_refund_requested)
        NotificationType.REFUND_COMPLETED -> stringResource(R.string.notification_refund_completed)
        NotificationType.REFUND_FAILED -> stringResource(R.string.notification_refund_failed)
        NotificationType.REFUND_MANUAL_REVIEW ->
            stringResource(R.string.notification_refund_manual_review)
        NotificationType.EARNING_AVAILABLE -> stringResource(R.string.notification_earning_available)
        NotificationType.CHAT_MESSAGE -> stringResource(R.string.notification_chat_message)
        NotificationType.UPCOMING_TOUR_REMINDER ->
            stringResource(R.string.notification_upcoming_tour_reminder)
        NotificationType.SECURITY_ALERT -> stringResource(R.string.notification_security_alert)
        NotificationType.UNKNOWN -> stringResource(R.string.notification_generic_update)
    }

@Composable
private fun NotificationUiModel.actorAndTourMessage(
    specific: Int,
    fallback: Int,
): String =
    if (actorName.isNotBlank() && tourTitle.isNotBlank()) {
        stringResource(specific, actorName, tourTitle)
    } else {
        stringResource(fallback)
    }

@Composable
private fun NotificationUiModel.tourMessage(
    specific: Int,
    fallback: Int,
): String =
    if (tourTitle.isNotBlank()) stringResource(specific, tourTitle) else stringResource(fallback)

@Composable
fun notificationRelativeTime(occurredAtMillis: Long): String =
    DateUtils.getRelativeTimeSpanString(
            occurredAtMillis,
            System.currentTimeMillis(),
            DateUtils.MINUTE_IN_MILLIS,
            DateUtils.FORMAT_ABBREV_RELATIVE,
        )
        .toString()
