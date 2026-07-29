package com.ahmetkaragunlu.guidemate.data.local

interface SecureStringStorage {
    fun get(key: String): String?

    fun putAll(values: Map<String, String>)

    fun remove(vararg keys: String)
}
