package com.ahmetkaragunlu.guidemate.notification.data.device

import com.ahmetkaragunlu.guidemate.notification.domain.device.PushInstallationIdProvider
import com.google.firebase.installations.FirebaseInstallations
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine

@Singleton
class FirebasePushInstallationIdProvider @Inject constructor() : PushInstallationIdProvider {
    override suspend fun getId(): String =
        suspendCancellableCoroutine { continuation ->
            FirebaseInstallations.getInstance().id
                .addOnSuccessListener { id ->
                    if (continuation.isActive) continuation.resume(id)
                }
                .addOnFailureListener { exception ->
                    if (continuation.isActive) continuation.resumeWithException(exception)
                }
        }
}
