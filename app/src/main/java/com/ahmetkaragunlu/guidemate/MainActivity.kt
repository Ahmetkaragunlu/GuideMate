package com.ahmetkaragunlu.guidemate

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ahmetkaragunlu.guidemate.navigation.GuideMateNavigation
import com.ahmetkaragunlu.guidemate.common.ui.theme.GuideMateTheme
import com.ahmetkaragunlu.guidemate.notification.data.push.NotificationTargetParser
import com.ahmetkaragunlu.guidemate.notification.domain.navigation.NotificationNavigationCoordinator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var notificationTargetParser: NotificationTargetParser
    @Inject lateinit var notificationNavigationCoordinator: NotificationNavigationCoordinator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleNotificationIntent(intent)
        enableEdgeToEdge()
        setContent {
            GuideMateTheme {
                GuideMateNavigation()
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleNotificationIntent(intent)
    }

    private fun handleNotificationIntent(intent: Intent?) {
        notificationTargetParser.consumeIntent(intent)?.let(notificationNavigationCoordinator::offer)
    }
}
