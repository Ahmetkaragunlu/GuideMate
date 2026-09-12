package com.ahmetkaragunlu.guidemate.common.ui.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.LifecycleResumeEffect

@Composable
fun RefreshOnResumeAfterInitialLoad(onRefresh: () -> Unit) {
    val currentOnRefresh by rememberUpdatedState(onRefresh)
    var hasCompletedInitialResume by remember { mutableStateOf(false) }

    LifecycleResumeEffect(Unit) {
        if (hasCompletedInitialResume) {
            currentOnRefresh()
        } else {
            hasCompletedInitialResume = true
        }
        onPauseOrDispose { }
    }
}
