package com.ahmetkaragunlu.guidemate.auth.data.local.session

fun interface CredentialSessionCleaner {
    suspend fun clear()
}
