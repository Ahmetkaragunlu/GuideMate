package com.ahmetkaragunlu.guidemate.data.local

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CredentialSessionManager @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val credentialManager = CredentialManager.create(context)

    suspend fun clear() {
        runCatching {
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
        }
    }
}
