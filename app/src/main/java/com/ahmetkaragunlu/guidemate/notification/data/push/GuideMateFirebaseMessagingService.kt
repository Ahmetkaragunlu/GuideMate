package com.ahmetkaragunlu.guidemate.notification.data.push

import com.ahmetkaragunlu.guidemate.notification.domain.repository.NotificationRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GuideMateFirebaseMessagingService : FirebaseMessagingService() {
    @Inject lateinit var notificationRepository: NotificationRepository
    @Inject lateinit var targetParser: NotificationTargetParser
    @Inject lateinit var notificationDisplayer: SystemNotificationDisplayer

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val target = targetParser.fromData(message.data) ?: return
        notificationRepository.onPushReceived(target)
        notificationDisplayer.show(target)
    }

}
