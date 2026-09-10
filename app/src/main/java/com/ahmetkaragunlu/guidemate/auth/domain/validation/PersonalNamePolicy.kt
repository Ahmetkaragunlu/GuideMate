package com.ahmetkaragunlu.guidemate.auth.domain.validation

import javax.inject.Inject

class PersonalNamePolicy @Inject constructor() {
    fun normalize(value: String): String = value.trim()

    fun isValidFirstName(value: String): Boolean = isValid(value, MIN_FIRST_NAME_LETTERS)

    fun isValidLastName(value: String): Boolean = isValid(value, MIN_LAST_NAME_LETTERS)

    private fun isValid(
        value: String,
        minimumLetterCount: Int,
    ): Boolean {
        val normalized = normalize(value)
        return normalized.count(Char::isLetter) >= minimumLetterCount &&
            NAME_PATTERN.matches(normalized)
    }

    private companion object {
        const val MIN_FIRST_NAME_LETTERS = 3
        const val MIN_LAST_NAME_LETTERS = 2
        val NAME_PATTERN = Regex("^\\p{L}+(?:[ '\\u2019-]\\p{L}+)*$")
    }
}
