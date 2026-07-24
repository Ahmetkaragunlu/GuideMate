package com.ahmetkaragunlu.guidemate

import android.app.Application
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GuideMateApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.PLACES_API_KEY.isValidPlacesApiKey() && !Places.isInitialized()) {
            Places.initializeWithNewPlacesApiEnabled(this, BuildConfig.PLACES_API_KEY)
        }
    }

    private fun String.isValidPlacesApiKey(): Boolean =
        isNotBlank() && this != DEFAULT_PLACES_API_KEY

    private companion object {
        const val DEFAULT_PLACES_API_KEY = "DEFAULT_API_KEY"
    }
}
