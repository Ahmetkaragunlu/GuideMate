package com.ahmetkaragunlu.guidemate.wallet.presentation.guide

import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationType
import com.ahmetkaragunlu.guidemate.notification.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

internal fun NotificationRepository.earningAvailabilityChanges(): Flow<Unit> =
    notifications
        .map { notifications ->
            notifications
                .asSequence()
                .filter { it.type == NotificationType.EARNING_AVAILABLE }
                .map { it.notificationId }
                .toSet()
        }.distinctUntilChanged()
        .drop(1)
        .filter { it.isNotEmpty() }
        .map { Unit }
