package com.ahmetkaragunlu.guidemate.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {
    @Provides
    @Singleton
    fun provideAppPreferences(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            migrations =
                listOf(
                    SharedPreferencesMigration(context, USER_PREFERENCES),
                    SharedPreferencesMigration(context, INSTALLATION_PREFERENCES),
                ),
            produceFile = { context.dataStoreFile(APP_PREFERENCES_FILE) },
        )

    private const val APP_PREFERENCES_FILE = "guidemate.preferences_pb"
    private const val USER_PREFERENCES = "user_prefs"
    private const val INSTALLATION_PREFERENCES = "guidemate_installation_prefs"
}
