package com.ahmetkaragunlu.guidemate.notification.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class NotificationCategoryTest {
    @Test
    fun `every notification type has the expected visual category`() {
        val expectedCategories =
            mapOf(
                NotificationType.TOUR_APPROVED to NotificationCategory.TOUR,
                NotificationType.TOUR_REJECTED to NotificationCategory.TOUR,
                NotificationType.TOUR_CHANGE_APPROVED to NotificationCategory.TOUR,
                NotificationType.TOUR_CHANGE_REJECTED to NotificationCategory.TOUR,
                NotificationType.TOUR_PURCHASED to NotificationCategory.TOUR,
                NotificationType.RESERVATION_CONFIRMED to NotificationCategory.TOUR,
                NotificationType.RESERVATION_CANCELLED to NotificationCategory.TOUR,
                NotificationType.TOUR_CANCELLED to NotificationCategory.TOUR,
                NotificationType.TOUR_COMPLETED to NotificationCategory.TOUR,
                NotificationType.REVIEW_REQUEST to NotificationCategory.RATING,
                NotificationType.RATING_RECEIVED to NotificationCategory.RATING,
                NotificationType.COMMENT_RECEIVED to NotificationCategory.COMMENT,
                NotificationType.PAYMENT_SUCCEEDED to NotificationCategory.PAYMENT,
                NotificationType.PAYMENT_FAILED to NotificationCategory.PAYMENT,
                NotificationType.REFUND_REQUESTED to NotificationCategory.PAYMENT,
                NotificationType.REFUND_COMPLETED to NotificationCategory.PAYMENT,
                NotificationType.REFUND_FAILED to NotificationCategory.PAYMENT,
                NotificationType.REFUND_MANUAL_REVIEW to NotificationCategory.PAYMENT,
                NotificationType.EARNING_AVAILABLE to NotificationCategory.PAYMENT,
                NotificationType.WITHDRAWAL_COMPLETED to NotificationCategory.PAYMENT,
                NotificationType.CHAT_MESSAGE to NotificationCategory.CHAT,
                NotificationType.UPCOMING_TOUR_REMINDER to NotificationCategory.TOUR,
                NotificationType.SECURITY_ALERT to NotificationCategory.SECURITY,
                NotificationType.UNKNOWN to NotificationCategory.GENERAL,
            )

        assertEquals(NotificationType.entries.toSet(), expectedCategories.keys)
        expectedCategories.forEach { (type, category) ->
            assertEquals(category, type.category)
        }
    }
}
