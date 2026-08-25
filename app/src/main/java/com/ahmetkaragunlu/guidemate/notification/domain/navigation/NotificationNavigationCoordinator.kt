package com.ahmetkaragunlu.guidemate.notification.domain.navigation

import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationNavigationTarget
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Singleton
class NotificationNavigationCoordinator @Inject constructor() {
    private val mutablePendingTarget = MutableStateFlow<NotificationNavigationTarget?>(null)
    val pendingTarget: StateFlow<NotificationNavigationTarget?> = mutablePendingTarget.asStateFlow()

    fun offer(target: NotificationNavigationTarget) {
        mutablePendingTarget.value = target
    }

    fun consume(target: NotificationNavigationTarget) {
        mutablePendingTarget.compareAndSet(target, null)
    }
}
