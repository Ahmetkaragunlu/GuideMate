package com.ahmetkaragunlu.guidemate.payment.presentation.savedpaymentmethod.sandbox

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.payment.presentation.savedpaymentmethod.model.AddSavedCardFormErrors
import com.ahmetkaragunlu.guidemate.payment.presentation.savedpaymentmethod.model.AddSavedCardFormState
import java.time.YearMonth

fun formatCardNumber(value: String): String =
    value
        .filter(Char::isDigit)
        .take(CARD_NUMBER_LENGTH)
        .chunked(CARD_NUMBER_GROUP_LENGTH)
        .joinToString(separator = " ")

fun sanitizeCardHolderName(value: String): String =
    value
        .replace(Regex("\\s+"), " ")
        .trimStart()

fun sanitizeExpiryPart(value: String): String =
    value
        .filter(Char::isDigit)
        .take(EXPIRY_PART_LENGTH)

fun sanitizeCvv(value: String): String =
    value
        .filter(Char::isDigit)
        .take(CVV_LENGTH)

fun validateSandboxCardForm(
    formState: AddSavedCardFormState,
    currentMonth: YearMonth = YearMonth.now(),
): AddSavedCardFormErrors {
    val cardDigits = formState.cardNumber.filter(Char::isDigit)
    val expiryMonth = formState.expiryMonth.toIntOrNull()
    val expiryYear = formState.expiryYear.toIntOrNull()?.let { TWO_DIGIT_YEAR_BASE + it }

    val cardNumberError =
        when {
            cardDigits.length != CARD_NUMBER_LENGTH -> R.string.card_number_error_message
            !cardDigits.isLuhnValid() -> R.string.card_number_invalid
            SandboxCardCatalog.findByCardNumber(cardDigits) == null -> R.string.sandbox_card_not_supported
            else -> null
        }

    val expiryMonthError =
        if (expiryMonth == null || expiryMonth !in VALID_MONTHS) {
            R.string.expiry_month_error_message
        } else {
            null
        }

    val expiryYearError =
        when {
            expiryYear == null -> R.string.expiry_year_error_message
            expiryMonth != null &&
                expiryMonth in VALID_MONTHS &&
                YearMonth.of(expiryYear, expiryMonth).isBefore(currentMonth) ->
                R.string.card_expired_error
            else -> null
        }

    return AddSavedCardFormErrors(
        cardNumberErrorResId = cardNumberError,
        cardHolderErrorResId =
            if (formState.cardHolderName.trim().length < MIN_CARD_HOLDER_LENGTH) {
                R.string.card_holder_name_error_message
            } else {
                null
            },
        expiryMonthErrorResId = expiryMonthError,
        expiryYearErrorResId = expiryYearError,
        cvvErrorResId =
            if (formState.cvv.length != CVV_LENGTH) {
                R.string.cvv_error_message
            } else {
                null
            },
    )
}

private fun String.isLuhnValid(): Boolean {
    var sum = 0
    val parity = length % 2

    forEachIndexed { index, character ->
        var digit = character.digitToInt()
        if (index % 2 == parity) {
            digit *= 2
            if (digit > 9) digit -= 9
        }
        sum += digit
    }

    return sum % 10 == 0
}

private const val CARD_NUMBER_LENGTH = 16
private const val CARD_NUMBER_GROUP_LENGTH = 4
private const val EXPIRY_PART_LENGTH = 2
private const val CVV_LENGTH = 3
private const val TWO_DIGIT_YEAR_BASE = 2000
private const val MIN_CARD_HOLDER_LENGTH = 3
private val VALID_MONTHS = 1..12
