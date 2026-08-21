package com.ahmetkaragunlu.guidemate.auth.domain.validation

import javax.inject.Inject

class NumericPasswordPolicy @Inject constructor() {
    fun sanitize(value: String): String = value.filter(Char::isDigit)

    fun isValid(value: String): Boolean =
        value.length >= MIN_LENGTH && value.all(Char::isDigit)

    companion object {
        const val MIN_LENGTH = 8
    }
}
