package com.ahmetkaragunlu.guidemate.notification.domain.device

interface PushInstallationIdProvider {
    suspend fun registerAndGetId(): String
}
