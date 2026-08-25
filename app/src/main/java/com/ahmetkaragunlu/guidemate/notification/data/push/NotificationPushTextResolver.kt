package com.ahmetkaragunlu.guidemate.notification.data.push

import android.content.Context
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationType
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationPushTextResolver
@Inject
constructor(
    @param:ApplicationContext private val context: Context,
) {
    fun title(type: NotificationType): String = context.getString(type.titleResource())

    fun body(type: NotificationType): String =
        context.getString(type.bodyResource())
}

private fun NotificationType.titleResource(): Int =
    when (this) {
        NotificationType.TOUR_APPROVED -> R.string.notification_title_tour_approved
        NotificationType.TOUR_REJECTED -> R.string.notification_title_tour_rejected
        NotificationType.TOUR_CHANGE_APPROVED ->
            R.string.notification_title_tour_change_approved
        NotificationType.TOUR_CHANGE_REJECTED ->
            R.string.notification_title_tour_change_rejected
        NotificationType.TOUR_PURCHASED -> R.string.notification_title_tour_purchased
        NotificationType.RESERVATION_CONFIRMED ->
            R.string.notification_title_reservation_confirmed
        NotificationType.RESERVATION_CANCELLED ->
            R.string.notification_title_reservation_cancelled
        NotificationType.TOUR_CANCELLED -> R.string.notification_title_tour_cancelled
        NotificationType.TOUR_COMPLETED -> R.string.notification_title_tour_completed
        NotificationType.REVIEW_REQUEST -> R.string.notification_title_review_request
        NotificationType.RATING_RECEIVED -> R.string.notification_title_rating_received
        NotificationType.COMMENT_RECEIVED -> R.string.notification_title_comment_received
        NotificationType.PAYMENT_SUCCEEDED -> R.string.notification_title_payment_succeeded
        NotificationType.PAYMENT_FAILED -> R.string.notification_title_payment_failed
        NotificationType.REFUND_REQUESTED -> R.string.notification_title_refund_requested
        NotificationType.REFUND_COMPLETED -> R.string.notification_title_refund_completed
        NotificationType.REFUND_FAILED -> R.string.notification_title_refund_failed
        NotificationType.REFUND_MANUAL_REVIEW ->
            R.string.notification_title_refund_manual_review
        NotificationType.EARNING_AVAILABLE -> R.string.notification_title_earning_available
        NotificationType.WITHDRAWAL_COMPLETED ->
            R.string.notification_title_withdrawal_completed
        NotificationType.CHAT_MESSAGE -> R.string.notification_title_chat_message
        NotificationType.UPCOMING_TOUR_REMINDER ->
            R.string.notification_title_upcoming_tour_reminder
        NotificationType.SECURITY_ALERT -> R.string.notification_title_security_alert
        NotificationType.UNKNOWN -> R.string.notification_title_general
    }

private fun NotificationType.bodyResource(): Int =
    when (this) {
        NotificationType.TOUR_APPROVED -> R.string.notification_tour_approved_generic
        NotificationType.TOUR_REJECTED -> R.string.notification_tour_rejected_generic
        NotificationType.TOUR_CHANGE_APPROVED -> R.string.notification_tour_change_approved
        NotificationType.TOUR_CHANGE_REJECTED -> R.string.notification_tour_change_rejected
        NotificationType.TOUR_PURCHASED -> R.string.notification_tour_purchased_generic
        NotificationType.RESERVATION_CONFIRMED -> R.string.notification_reservation_confirmed
        NotificationType.RESERVATION_CANCELLED -> R.string.notification_reservation_cancelled
        NotificationType.TOUR_CANCELLED -> R.string.notification_tour_cancelled
        NotificationType.TOUR_COMPLETED -> R.string.notification_tour_completed_generic
        NotificationType.REVIEW_REQUEST -> R.string.notification_review_request
        NotificationType.RATING_RECEIVED -> R.string.notification_rating_received_generic
        NotificationType.COMMENT_RECEIVED -> R.string.notification_comment_received_generic
        NotificationType.PAYMENT_SUCCEEDED -> R.string.notification_payment_succeeded
        NotificationType.PAYMENT_FAILED -> R.string.notification_payment_failed
        NotificationType.REFUND_REQUESTED -> R.string.notification_refund_requested
        NotificationType.REFUND_COMPLETED -> R.string.notification_refund_completed
        NotificationType.REFUND_FAILED -> R.string.notification_refund_failed
        NotificationType.REFUND_MANUAL_REVIEW -> R.string.notification_refund_manual_review
        NotificationType.EARNING_AVAILABLE -> R.string.notification_earning_available
        NotificationType.WITHDRAWAL_COMPLETED ->
            R.string.notification_withdrawal_completed_generic
        NotificationType.CHAT_MESSAGE -> R.string.notification_chat_message
        NotificationType.UPCOMING_TOUR_REMINDER -> R.string.notification_upcoming_tour_reminder
        NotificationType.SECURITY_ALERT -> R.string.notification_security_alert
        NotificationType.UNKNOWN -> R.string.notification_generic_update
    }
