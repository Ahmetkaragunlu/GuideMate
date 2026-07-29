package com.ahmetkaragunlu.guidemate.domain.repository

interface OnboardingRepository {
    suspend fun isCompleted(): Boolean

    suspend fun complete()
}
