package com.ahmetkaragunlu.guidemate.notification.domain.device

interface PushInstallationIdProvider {
    suspend fun getId(): String
}
