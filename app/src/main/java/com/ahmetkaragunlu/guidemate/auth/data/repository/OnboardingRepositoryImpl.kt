package com.ahmetkaragunlu.guidemate.auth.data.repository

import com.ahmetkaragunlu.guidemate.auth.data.local.preferences.AuthPreferencesDataSource
import com.ahmetkaragunlu.guidemate.auth.domain.repository.OnboardingRepository
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val preferences: AuthPreferencesDataSource,
) : OnboardingRepository {
    override suspend fun isCompleted(): Boolean = preferences.isOnboardingCompleted()

    override suspend fun complete() {
        preferences.completeOnboarding()
    }
}
