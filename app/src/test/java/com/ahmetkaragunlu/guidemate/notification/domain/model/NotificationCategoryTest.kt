package com.ahmetkaragunlu.guidemate.notification.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class NotificationCategoryTest {
    @Test
    fun `every notification type has the expected visual category`() {
        val expectedCategories =
            mapOf(
                NotificationType.CHAT_MESSAGE to NotificationCategory.CHAT,
                NotificationType.COMMENT_RECEIVED to NotificationCategory.COMMENT,
                NotificationType.RATING_RECEIVED to NotificationCategory.RATING,
                NotificationType.REVIEW_REQUEST to NotificationCategory.RATING,
                NotificationType.PAYMENT_SUCCEEDED to NotificationCategory.PAYMENT,
                NotificationType.SECURITY_ALERT to NotificationCategory.SECURITY,
                NotificationType.TOUR_APPROVED to NotificationCategory.TOUR,
                NotificationType.UNKNOWN to NotificationCategory.GENERAL,
            )

        expectedCategories.forEach { (type, category) ->
            assertEquals(category, type.category)
        }
    }
}
