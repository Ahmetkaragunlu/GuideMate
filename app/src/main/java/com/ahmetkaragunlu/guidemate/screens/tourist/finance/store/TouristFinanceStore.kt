package com.ahmetkaragunlu.guidemate.screens.tourist.finance.store

import com.ahmetkaragunlu.guidemate.screens.tourist.finance.model.PaymentCardAssociation
import com.ahmetkaragunlu.guidemate.screens.tourist.finance.model.PaymentCardType
import com.ahmetkaragunlu.guidemate.screens.tourist.finance.model.SavedPaymentCardUiModel
import com.ahmetkaragunlu.guidemate.screens.tourist.finance.model.TouristFinanceState
import com.ahmetkaragunlu.guidemate.screens.tourist.finance.model.TouristWalletTransactionStatus
import com.ahmetkaragunlu.guidemate.screens.tourist.finance.model.TouristWalletTransactionType
import com.ahmetkaragunlu.guidemate.screens.tourist.finance.model.TouristWalletTransactionUiModel
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Singleton
class TouristFinanceStore
    @Inject
    constructor() {
        private val _state = MutableStateFlow(createMockState())
        val state: StateFlow<TouristFinanceState> = _state.asStateFlow()

        fun deleteCard(cardId: String) {
            _state.update { current ->
                val remainingCards = current.savedCards.filterNot { it.cardId == cardId }
                val normalizedCards =
                    if (remainingCards.isNotEmpty() && remainingCards.none { it.isDefault }) {
                        remainingCards.mapIndexed { index, card ->
                            card.copy(isDefault = index == 0)
                        }
                    } else {
                        remainingCards
                    }
                current.copy(savedCards = normalizedCards)
            }
        }

        fun makeDefaultCard(cardId: String) {
            _state.update { current ->
                if (current.savedCards.none { it.cardId == cardId }) return@update current
                current.copy(
                    savedCards =
                        current.savedCards
                            .map { card -> card.copy(isDefault = card.cardId == cardId) }
                            .sortedByDescending { it.isDefault },
                )
            }
        }

        fun addCard(card: SavedPaymentCardUiModel) {
            _state.update { current ->
                val newCard = card.copy(isDefault = current.savedCards.isEmpty())
                current.copy(
                    savedCards =
                        (listOf(newCard) + current.savedCards)
                            .sortedByDescending { it.isDefault },
                )
            }
        }

        fun nextCard(currentCardId: String?): SavedPaymentCardUiModel? {
            val cards = state.value.savedCards
            if (cards.isEmpty()) return null
            val currentIndex = cards.indexOfFirst { it.cardId == currentCardId }.coerceAtLeast(0)
            return cards[(currentIndex + 1) % cards.size]
        }

        private companion object {
            fun createMockState(): TouristFinanceState =
                TouristFinanceState(
                    balanceMinor = 150_000,
                    savedCards =
                        listOf(
                            SavedPaymentCardUiModel(
                                cardId = "tourist-card-1",
                                bankName = "Garanti BBVA",
                                cardFamily = "Bonus",
                                cardAssociation = PaymentCardAssociation.MASTER_CARD,
                                cardType = PaymentCardType.CREDIT_CARD,
                                lastFourDigits = "4567",
                                cardHolderName = "Ahmet Karagünlü",
                                expiryMonth = "12",
                                expiryYear = "2028",
                                isDefault = true,
                            ),
                            SavedPaymentCardUiModel(
                                cardId = "tourist-card-2",
                                bankName = "Ziraat Bankası",
                                cardFamily = "Bankkart",
                                cardAssociation = PaymentCardAssociation.VISA,
                                cardType = PaymentCardType.DEBIT_CARD,
                                lastFourDigits = "9821",
                                cardHolderName = "Ahmet Karagünlü",
                                expiryMonth = "07",
                                expiryYear = "2027",
                                isDefault = false,
                            ),
                        ),
                    transactions =
                        listOf(
                            TouristWalletTransactionUiModel(
                                transactionId = "wallet-transaction-1",
                                title = "Bakiye yükleme",
                                amountMinor = 100_000,
                                type = TouristWalletTransactionType.TOP_UP,
                                status = TouristWalletTransactionStatus.COMPLETED,
                                createdAt = Instant.parse("2026-07-20T12:30:00Z"),
                            ),
                            TouristWalletTransactionUiModel(
                                transactionId = "wallet-transaction-2",
                                title = "Kapadokya Balon Turu",
                                amountMinor = -75_000,
                                type = TouristWalletTransactionType.TOUR_PURCHASE,
                                status = TouristWalletTransactionStatus.COMPLETED,
                                createdAt = Instant.parse("2026-07-18T09:15:00Z"),
                            ),
                            TouristWalletTransactionUiModel(
                                transactionId = "wallet-transaction-3",
                                title = "İptal edilen tur iadesi",
                                amountMinor = 50_000,
                                type = TouristWalletTransactionType.REFUND,
                                status = TouristWalletTransactionStatus.REFUNDED,
                                createdAt = Instant.parse("2026-07-12T16:45:00Z"),
                            ),
                        ),
                )
        }
    }
