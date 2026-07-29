package com.ahmetkaragunlu.guidemate.data.local

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidKeystoreSecureStringStorage @Inject constructor(
    @ApplicationContext context: Context,
) : SecureStringStorage {
    private val lock = Any()
    private val preferences =
        context.getSharedPreferences(SECURE_PREFERENCES, Context.MODE_PRIVATE)

    override fun get(key: String): String? =
        synchronized(lock) {
            preferences.getString(key, null)?.let(::decrypt)
        }

    override fun putAll(values: Map<String, String>) {
        synchronized(lock) {
            val encryptedValues = values.mapValues { (_, value) -> encrypt(value) }
            preferences.edit(commit = true) {
                encryptedValues.forEach { (key, value) ->
                    putString(key, value)
                }
            }
        }
    }

    override fun remove(vararg keys: String) {
        synchronized(lock) {
            preferences.edit(commit = true) {
                keys.forEach(::remove)
            }
        }
    }

    private fun encrypt(value: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreateKey())
        val encrypted = cipher.doFinal(value.toByteArray(Charsets.UTF_8))
        return "${cipher.iv.encode()}:${
            encrypted.encode()
        }"
    }

    private fun decrypt(value: String): String? =
        runCatching {
            val (encodedIv, encodedValue) = value.split(SEPARATOR, limit = 2)
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(
                Cipher.DECRYPT_MODE,
                getOrCreateKey(),
                GCMParameterSpec(GCM_TAG_LENGTH_BITS, encodedIv.decode()),
            )
            String(cipher.doFinal(encodedValue.decode()), Charsets.UTF_8)
        }.getOrNull()

    private fun getOrCreateKey(): SecretKey {
        val keyStore =
            KeyStore.getInstance(ANDROID_KEYSTORE).apply {
                load(null)
            }
        (keyStore.getKey(KEY_ALIAS, null) as? SecretKey)?.let { return it }

        return KeyGenerator
            .getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
            .apply {
                init(
                    KeyGenParameterSpec
                        .Builder(
                            KEY_ALIAS,
                            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
                        ).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        .build(),
                )
            }.generateKey()
    }

    private fun ByteArray.encode(): String =
        Base64.encodeToString(this, Base64.NO_WRAP)

    private fun String.decode(): ByteArray =
        Base64.decode(this, Base64.NO_WRAP)

    private companion object {
        const val ANDROID_KEYSTORE = "AndroidKeyStore"
        const val KEY_ALIAS = "guidemate_auth_session_key"
        const val SECURE_PREFERENCES = "guidemate_secure_session"
        const val TRANSFORMATION = "AES/GCM/NoPadding"
        const val GCM_TAG_LENGTH_BITS = 128
        const val SEPARATOR = ":"
    }
}
