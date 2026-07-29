package com.ahmetkaragunlu.guidemate.data.repository

import com.ahmetkaragunlu.guidemate.data.local.preferences.AppPreferencesDataSource
import com.ahmetkaragunlu.guidemate.domain.repository.OnboardingRepository
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val preferences: AppPreferencesDataSource,
) : OnboardingRepository {
    override suspend fun isCompleted(): Boolean = preferences.isOnboardingCompleted()

    override suspend fun complete() {
        preferences.completeOnboarding()
    }
}
