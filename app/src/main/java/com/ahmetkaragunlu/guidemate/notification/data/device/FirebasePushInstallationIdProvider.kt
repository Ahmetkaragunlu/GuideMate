package com.ahmetkaragunlu.guidemate.notification.data.device

import com.ahmetkaragunlu.guidemate.notification.domain.device.PushInstallationIdProvider
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine

@Singleton
class FirebasePushInstallationIdProvider @Inject constructor() : PushInstallationIdProvider {
    override suspend fun registerAndGetId(): String {
        registerWithFirebaseMessaging()
        return getFirebaseInstallationId()
    }

    private suspend fun registerWithFirebaseMessaging(): Unit =
        suspendCancellableCoroutine { continuation ->
            FirebaseMessaging.getInstance().register()
                .addOnSuccessListener {
                    if (continuation.isActive) continuation.resume(Unit)
                }
                .addOnFailureListener { exception ->
                    if (continuation.isActive) continuation.resumeWithException(exception)
                }
                .addOnCanceledListener { continuation.cancel() }
        }

    private suspend fun getFirebaseInstallationId(): String =
        suspendCancellableCoroutine { continuation ->
            FirebaseInstallations.getInstance().id
                .addOnSuccessListener { id ->
                    if (continuation.isActive) continuation.resume(id)
                }
                .addOnFailureListener { exception ->
                    if (continuation.isActive) continuation.resumeWithException(exception)
                }
                .addOnCanceledListener { continuation.cancel() }
        }
}
