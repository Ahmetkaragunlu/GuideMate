package com.ahmetkaragunlu.guidemate.auth.data.local.session

internal class InMemorySecureSessionStorage : SecureSessionStorage {
    private val values = mutableMapOf<String, String>()

    override fun get(key: String): String? = values[key]

    override fun putAll(values: Map<String, String>) {
        this.values.putAll(values)
    }

    override fun remove(vararg keys: String) {
        keys.forEach(values::remove)
    }
}
