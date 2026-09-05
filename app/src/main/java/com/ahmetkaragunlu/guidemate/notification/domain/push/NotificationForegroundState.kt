package com.ahmetkaragunlu.guidemate.notification.domain.push

import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationNavigationTarget
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationVisibleTarget
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationForegroundState @Inject constructor() {
    @Volatile private var isAppResumed = false
    @Volatile private var visibleTarget: NotificationVisibleTarget? = null

    fun setAppResumed(isResumed: Boolean) {
        isAppResumed = isResumed
    }

    fun setVisibleTarget(target: NotificationVisibleTarget?) {
        visibleTarget = target
    }

    fun shouldDisplay(target: NotificationNavigationTarget): Boolean =
        !isAppResumed || visibleTarget?.matches(target) != true
}
