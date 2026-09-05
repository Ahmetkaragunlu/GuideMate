package com.ahmetkaragunlu.guidemate.notification.di

import com.ahmetkaragunlu.guidemate.notification.data.device.FirebasePushInstallationIdProvider
import com.ahmetkaragunlu.guidemate.notification.data.realtime.DefaultNotificationRealtimeClient
import com.ahmetkaragunlu.guidemate.notification.data.realtime.NotificationRealtimeClient
import com.ahmetkaragunlu.guidemate.notification.data.remote.api.NotificationApi
import com.ahmetkaragunlu.guidemate.notification.data.repository.NotificationRepositoryImpl
import com.ahmetkaragunlu.guidemate.notification.data.push.SystemNotificationDisplayer
import com.ahmetkaragunlu.guidemate.notification.domain.device.PushInstallationIdProvider
import com.ahmetkaragunlu.guidemate.notification.domain.push.SystemNotificationController
import com.ahmetkaragunlu.guidemate.notification.domain.repository.NotificationRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationModule {
    @Binds
    @Singleton
    abstract fun bindNotificationRepository(
        implementation: NotificationRepositoryImpl,
    ): NotificationRepository

    @Binds
    @Singleton
    abstract fun bindPushInstallationIdProvider(
        implementation: FirebasePushInstallationIdProvider,
    ): PushInstallationIdProvider

    @Binds
    @Singleton
    abstract fun bindNotificationRealtimeClient(
        implementation: DefaultNotificationRealtimeClient,
    ): NotificationRealtimeClient

    @Binds
    @Singleton
    abstract fun bindSystemNotificationController(
        implementation: SystemNotificationDisplayer,
    ): SystemNotificationController

    companion object {
        @Provides
        @Singleton
        fun provideNotificationApi(retrofit: Retrofit): NotificationApi =
            retrofit.create(NotificationApi::class.java)
    }
}
