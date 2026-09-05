package com.ahmetkaragunlu.guidemate.notification.data.push

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.ahmetkaragunlu.guidemate.MainActivity
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationNavigationTarget
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationTargetReference
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationTargetType
import com.ahmetkaragunlu.guidemate.notification.domain.push.NotificationForegroundState
import com.ahmetkaragunlu.guidemate.notification.domain.push.SystemNotificationController
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SystemNotificationDisplayer
@Inject
constructor(
    @param:ApplicationContext private val context: Context,
    private val targetParser: NotificationTargetParser,
    private val textResolver: NotificationPushTextResolver,
    private val foregroundState: NotificationForegroundState,
) : SystemNotificationController {
    override fun createChannel() {
        val channel =
            NotificationChannel(
                CHANNEL_ID,
                context.getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT,
            ).apply {
                description = context.getString(R.string.notification_channel_description)
            }
        context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    override fun show(target: NotificationNavigationTarget) {
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        if (!foregroundState.shouldDisplay(target)) return

        val identity = target.systemNotificationIdentity()

        val contentIntent =
            targetParser.putExtras(
                Intent(context, MainActivity::class.java).apply {
                    action = "$NOTIFICATION_ACTION_PREFIX${identity.actionSuffix}"
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                },
                target,
            )
        val pendingIntent =
            PendingIntent.getActivity(
                context,
                identity.requestCode,
                contentIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
        val title = textResolver.title(target)
        val body = textResolver.body(target)
        val notification =
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_app)
                .setColor(ContextCompat.getColor(context, R.color.notification_icon_color))
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(NotificationCompat.BigTextStyle().bigText(body))
                .addExtras(target.toSystemNotificationMetadata())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()

        NotificationManagerCompat.from(context).notify(identity.tag, identity.id, notification)
    }

    override fun dismiss(target: NotificationNavigationTarget) {
        val identity = target.systemNotificationIdentity()
        NotificationManagerCompat.from(context).cancel(identity.tag, identity.id)
    }

    override fun dismissRelated(target: NotificationTargetReference) {
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.activeNotifications
            .filter { notification ->
                notification.notification.extras.getString(target.type.metadataKey) ==
                    target.targetId
            }.forEach { notification -> manager.cancel(notification.tag, notification.id) }
    }

    override fun dismissAll() {
        NotificationManagerCompat.from(context).cancelAll()
    }

    companion object {
        const val CHANNEL_ID = "guidemate_updates"
        private const val NOTIFICATION_ACTION_PREFIX = "guidemate.notification."
    }
}

private fun NotificationNavigationTarget.toSystemNotificationMetadata(): Bundle =
    Bundle().apply {
        putString(NotificationTargetType.CHAT.metadataKey, chatId)
        putString(NotificationTargetType.TOUR.metadataKey, tourId ?: sessionId)
        putString(NotificationTargetType.RESERVATION.metadataKey, reservationId)
        putString(NotificationTargetType.PAYMENT.metadataKey, paymentId)
    }

private val NotificationTargetType.metadataKey: String
    get() = "guidemate.notification.target.$name"
