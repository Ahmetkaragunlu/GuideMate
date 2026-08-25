package com.ahmetkaragunlu.guidemate.notification.presentation.mapper

import androidx.compose.ui.graphics.vector.ImageVector
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationCategory
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationType
import com.ahmetkaragunlu.guidemate.notification.domain.model.category
import compose.icons.TablerIcons
import compose.icons.tablericons.CreditCard
import compose.icons.tablericons.Lock
import compose.icons.tablericons.Message
import compose.icons.tablericons.MessageCircle2
import compose.icons.tablericons.Star
import compose.icons.tablericons.Ticket

fun NotificationType.notificationIcon(): ImageVector =
    when (category) {
        NotificationCategory.TOUR -> TablerIcons.Ticket
        NotificationCategory.CHAT -> TablerIcons.MessageCircle2
        NotificationCategory.COMMENT -> TablerIcons.Message
        NotificationCategory.RATING -> TablerIcons.Star
        NotificationCategory.PAYMENT -> TablerIcons.CreditCard
        NotificationCategory.SECURITY -> TablerIcons.Lock
        NotificationCategory.GENERAL -> TablerIcons.Ticket
    }
