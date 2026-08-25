package com.ahmetkaragunlu.guidemate.notification.presentation

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.LifecycleResumeEffect

@Composable
fun NotificationSyncEffect(onRefresh: () -> Unit) {
    LifecycleResumeEffect(Unit) {
        onRefresh()
        onPauseOrDispose {}
    }
}
