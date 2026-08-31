package com.ahmetkaragunlu.guidemate.notification.data.push

import android.annotation.SuppressLint
import com.ahmetkaragunlu.guidemate.common.coroutines.ApplicationScope
import com.ahmetkaragunlu.guidemate.notification.domain.repository.NotificationRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Firebase Messaging 25+ replaces the token callback with FID-based onRegistered.
@SuppressLint("MissingFirebaseInstanceTokenRefresh")
@AndroidEntryPoint
class GuideMateFirebaseMessagingService : FirebaseMessagingService() {
    @Inject lateinit var notificationRepository: NotificationRepository
    @Inject lateinit var targetParser: NotificationTargetParser
    @Inject lateinit var notificationDisplayer: SystemNotificationDisplayer
    @Inject @ApplicationScope lateinit var applicationScope: CoroutineScope

    override fun onRegistered(installationId: String) {
        super.onRegistered(installationId)
        applicationScope.launch {
            notificationRepository.registerDevice(installationId)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val target = targetParser.fromData(message.data) ?: return
        notificationRepository.onPushReceived(target)
        notificationDisplayer.show(target)
    }
}
