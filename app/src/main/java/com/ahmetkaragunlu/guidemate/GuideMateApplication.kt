package com.ahmetkaragunlu.guidemate

import android.app.Application
import android.content.Context
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import com.google.android.libraries.places.api.Places
import com.ahmetkaragunlu.guidemate.notification.data.push.SystemNotificationDisplayer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import okhttp3.OkHttpClient

@HiltAndroidApp
class GuideMateApplication : Application(), SingletonImageLoader.Factory {
    @Inject lateinit var okHttpClient: OkHttpClient
    @Inject lateinit var notificationDisplayer: SystemNotificationDisplayer

    override fun onCreate() {
        super.onCreate()
        notificationDisplayer.createChannel()
        if (BuildConfig.PLACES_API_KEY.isValidPlacesApiKey() && !Places.isInitialized()) {
            Places.initializeWithNewPlacesApiEnabled(this, BuildConfig.PLACES_API_KEY)
        }
    }

    private fun String.isValidPlacesApiKey(): Boolean =
        isNotBlank() && this != DEFAULT_PLACES_API_KEY

    override fun newImageLoader(context: Context): ImageLoader =
        ImageLoader
            .Builder(context)
            .components {
                add(
                    OkHttpNetworkFetcherFactory(
                        callFactory = { okHttpClient },
                    ),
                )
            }.build()

    private companion object {
        const val DEFAULT_PLACES_API_KEY = "DEFAULT_API_KEY"
    }
}
