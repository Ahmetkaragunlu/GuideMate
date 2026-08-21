package com.ahmetkaragunlu.guidemate.auth.domain.validation

import java.util.Locale
import javax.inject.Inject

class EmailPolicy @Inject constructor() {
    fun normalize(value: String): String = value.trim().lowercase(Locale.ROOT)

    fun isValid(value: String): Boolean =
        EMAIL_PATTERN.matches(normalize(value))

    private companion object {
        val EMAIL_PATTERN =
            Regex("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", RegexOption.IGNORE_CASE)
    }
}
