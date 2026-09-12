package com.ahmetkaragunlu.guidemate.auth.data.local.session

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CancellationException

@Singleton
class CredentialSessionManager @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val credentialManager = CredentialManager.create(context)

    suspend fun clear() {
        try {
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (_: Exception) {
            // Local credential cleanup must not prevent backend session cleanup.
        }
    }
}
