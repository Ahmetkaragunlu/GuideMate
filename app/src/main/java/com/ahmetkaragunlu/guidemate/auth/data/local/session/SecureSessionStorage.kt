package com.ahmetkaragunlu.guidemate.auth.data.local.session

interface SecureSessionStorage {
    fun get(key: String): String?

    fun putAll(values: Map<String, String>)

    fun remove(vararg keys: String)
}
