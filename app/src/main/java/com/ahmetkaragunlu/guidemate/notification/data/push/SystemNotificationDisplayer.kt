package com.ahmetkaragunlu.guidemate.notification.data.push

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.ahmetkaragunlu.guidemate.MainActivity
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationNavigationTarget
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationCategory
import com.ahmetkaragunlu.guidemate.notification.domain.model.category
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
) {
    fun createChannel() {
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

    fun show(target: NotificationNavigationTarget) {
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val contentIntent =
            targetParser.putExtras(
                Intent(context, MainActivity::class.java).apply {
                    action = "$NOTIFICATION_ACTION_PREFIX${target.notificationId.orEmpty()}"
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                },
                target,
            )
        val pendingIntent =
            PendingIntent.getActivity(
                context,
                target.notificationId?.hashCode() ?: target.hashCode(),
                contentIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
        val title = textResolver.title(target.type)
        val body = textResolver.body(target.type)
        val notification =
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_app)
                .setColor(ContextCompat.getColor(context, R.color.notification_icon_color))
                .setLargeIcon(categoryBitmap(target.type.category))
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(NotificationCompat.BigTextStyle().bigText(body))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()

        NotificationManagerCompat.from(context).notify(
            target.notificationId?.hashCode() ?: target.hashCode(),
            notification,
        )
    }

    private fun categoryBitmap(category: NotificationCategory): Bitmap? {
        val drawable = ContextCompat.getDrawable(context, category.drawableResource()) ?: return null
        val wrapped = DrawableCompat.wrap(drawable.mutate())
        DrawableCompat.setTint(
            wrapped,
            ContextCompat.getColor(context, R.color.notification_icon_color),
        )
        val size = (40 * context.resources.displayMetrics.density).toInt()
        return Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888).also { bitmap ->
            wrapped.setBounds(0, 0, size, size)
            wrapped.draw(Canvas(bitmap))
        }
    }

    companion object {
        const val CHANNEL_ID = "guidemate_updates"
        private const val NOTIFICATION_ACTION_PREFIX = "guidemate.notification."
    }
}

private fun NotificationCategory.drawableResource(): Int =
    when (this) {
        NotificationCategory.TOUR -> R.drawable.ic_notification_tour
        NotificationCategory.CHAT -> R.drawable.ic_notification_chat
        NotificationCategory.COMMENT -> R.drawable.ic_notification_comment
        NotificationCategory.RATING -> R.drawable.ic_notification_rating
        NotificationCategory.PAYMENT -> R.drawable.ic_notification_payment
        NotificationCategory.SECURITY -> R.drawable.ic_notification_security
        NotificationCategory.GENERAL -> R.drawable.ic_notification_general
    }
