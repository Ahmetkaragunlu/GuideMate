package com.ahmetkaragunlu.guidemate.screens.guide.finance.iban

import java.util.Locale
import javax.inject.Inject

class TurkishIbanValidator
    @Inject
    constructor() {
        fun sanitizeBody(value: String): String =
            normalize(value)
                .removePrefix(COUNTRY_CODE)
                .take(IBAN_BODY_LENGTH)

        fun toNormalizedIban(ibanBody: String): String =
            COUNTRY_CODE + sanitizeBody(ibanBody)

        fun bankCode(iban: String): String? {
            val normalizedIban = normalize(iban)
            if (
                normalizedIban.length < BANK_CODE_END_INDEX ||
                    !normalizedIban.startsWith(COUNTRY_CODE)
            ) {
                return null
            }

            return normalizedIban
                .substring(BANK_CODE_START_INDEX, BANK_CODE_END_INDEX)
                .takeIf { code -> code.all(::isAsciiDigit) }
        }

        fun isValid(iban: String): Boolean {
            val normalizedIban = normalize(iban)
            if (
                normalizedIban.length != TURKISH_IBAN_LENGTH ||
                    !normalizedIban.startsWith(COUNTRY_CODE) ||
                    !normalizedIban.substring(CHECK_DIGITS_START_INDEX, CHECK_DIGITS_END_INDEX)
                        .all(::isAsciiDigit) ||
                    bankCode(normalizedIban) == null ||
                    normalizedIban[RESERVED_FIELD_INDEX] != RESERVED_FIELD_VALUE ||
                    !normalizedIban.drop(ACCOUNT_NUMBER_START_INDEX).all(::isAsciiAlphaNumeric)
            ) {
                return false
            }

            val rearrangedIban = normalizedIban.drop(4) + normalizedIban.take(4)
            return calculateMod97(rearrangedIban) == VALID_IBAN_REMAINDER
        }

        private fun normalize(value: String): String =
            value
                .uppercase(Locale.ROOT)
                .filter(::isAsciiAlphaNumeric)

        private fun isAsciiDigit(character: Char): Boolean = character in '0'..'9'

        private fun isAsciiAlphaNumeric(character: Char): Boolean =
            isAsciiDigit(character) || character in 'A'..'Z'

        private fun calculateMod97(value: String): Int {
            var remainder = 0
            value.forEach { character ->
                val numericValue =
                    if (character.isDigit()) {
                        character.toString()
                    } else {
                        (character - 'A' + LETTER_NUMERIC_OFFSET).toString()
                    }
                numericValue.forEach { digit ->
                    remainder = (remainder * 10 + digit.digitToInt()) % MODULUS
                }
            }
            return remainder
        }

        companion object {
            const val COUNTRY_CODE = "TR"
            const val IBAN_BODY_LENGTH = 24

            private const val TURKISH_IBAN_LENGTH = 26
            private const val CHECK_DIGITS_START_INDEX = 2
            private const val CHECK_DIGITS_END_INDEX = 4
            private const val BANK_CODE_START_INDEX = 4
            private const val BANK_CODE_END_INDEX = 9
            private const val RESERVED_FIELD_INDEX = 9
            private const val RESERVED_FIELD_VALUE = '0'
            private const val ACCOUNT_NUMBER_START_INDEX = 10
            private const val LETTER_NUMERIC_OFFSET = 10
            private const val MODULUS = 97
            private const val VALID_IBAN_REMAINDER = 1
        }
    }
