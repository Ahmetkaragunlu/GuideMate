package com.ahmetkaragunlu.guidemate.screens.common.profile.account.savedcards

import com.ahmetkaragunlu.guidemate.screens.common.profile.account.savedcards.model.AddSavedCardFormState
import com.ahmetkaragunlu.guidemate.screens.common.profile.account.savedcards.model.SavedCardUi

fun findDefaultSavedCard(cards: List<SavedCardUi>): SavedCardUi? =
    cards.firstOrNull { it.isDefault } ?: cards.firstOrNull()

fun deleteSavedCard(
    cards: List<SavedCardUi>,
    cardId: String,
): List<SavedCardUi> {
    val filteredCards = cards.filterNot { it.cardId == cardId }
    return if (filteredCards.isNotEmpty() && filteredCards.none { it.isDefault }) {
        filteredCards.mapIndexed { index, card ->
            card.copy(isDefault = index == 0)
        }
    } else {
        filteredCards
    }
}

fun makeDefaultSavedCard(
    cards: List<SavedCardUi>,
    cardId: String,
): List<SavedCardUi> =
    cards
        .map { card -> card.copy(isDefault = card.cardId == cardId) }
        .sortedByDescending { it.isDefault }

fun findNextSavedCard(
    cards: List<SavedCardUi>,
    currentCardId: String?,
): SavedCardUi? {
    if (cards.isEmpty()) return null

    val currentIndex =
        cards.indexOfFirst { it.cardId == currentCardId }
            .takeIf { it >= 0 }
            ?: 0

    return cards[(currentIndex + 1) % cards.size]
}

fun sanitizeCardNumber(value: String): String =
    value.filter(Char::isDigit).take(16)

fun sanitizeCardHolderName(value: String): String =
    value.replace(Regex("\\s+"), " ").trimStart()

fun sanitizeExpiryMonth(value: String): String =
    value.filter(Char::isDigit).take(2)

fun sanitizeExpiryYear(value: String): String =
    value.filter(Char::isDigit).take(2)

fun appendSavedCard(
    cards: List<SavedCardUi>,
    form: AddSavedCardFormState,
): List<SavedCardUi> {
    val newCard =
        SavedCardUi(
            cardId = "card_${System.currentTimeMillis()}",
            bankName = DEFAULT_BANK_NAME,
            cardNumber = form.cardNumber.toMaskedCardNumber(),
            cardHolderName = form.cardHolderName.trim(),
            expiryDate = "${form.expiryMonth}/${form.expiryYear}",
            isDefault = cards.isEmpty(),
        )

    return cards + newCard
}

private fun String.toMaskedCardNumber(): String =
    "**** **** **** ${takeLast(4)}"

private const val DEFAULT_BANK_NAME = "Kredi Kartı"
