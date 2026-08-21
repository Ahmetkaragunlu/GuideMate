package com.ahmetkaragunlu.guidemate.auth.presentation.signin

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException

internal class GoogleCredentialSignInClient(
    private val credentialManager: CredentialManager,
) {
    suspend fun signIn(
        context: Context,
        serverClientId: String,
    ): GoogleSignInResult {
        val signInOption =
            GetSignInWithGoogleOption.Builder(serverClientId = serverClientId).build()
        val request =
            GetCredentialRequest.Builder().addCredentialOption(signInOption).build()

        return try {
            val credential = credentialManager.getCredential(context, request).credential
            if (
                credential is CustomCredential &&
                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                val googleCredential = GoogleIdTokenCredential.createFrom(credential.data)
                GoogleSignInResult.Success(googleCredential.idToken)
            } else {
                GoogleSignInResult.Failure
            }
        } catch (_: GetCredentialCancellationException) {
            GoogleSignInResult.Cancelled
        } catch (_: NoCredentialException) {
            GoogleSignInResult.Failure
        } catch (_: GetCredentialException) {
            GoogleSignInResult.Failure
        } catch (_: GoogleIdTokenParsingException) {
            GoogleSignInResult.Failure
        }
    }

    companion object {
        fun create(context: Context): GoogleCredentialSignInClient =
            GoogleCredentialSignInClient(CredentialManager.create(context))
    }
}

internal sealed interface GoogleSignInResult {
    data class Success(val idToken: String) : GoogleSignInResult

    data object Cancelled : GoogleSignInResult

    data object Failure : GoogleSignInResult
}
