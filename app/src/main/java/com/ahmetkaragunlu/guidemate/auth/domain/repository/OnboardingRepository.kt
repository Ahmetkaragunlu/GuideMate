package com.ahmetkaragunlu.guidemate.auth.domain.repository

interface OnboardingRepository {
    suspend fun isCompleted(): Boolean

    suspend fun complete()
}
