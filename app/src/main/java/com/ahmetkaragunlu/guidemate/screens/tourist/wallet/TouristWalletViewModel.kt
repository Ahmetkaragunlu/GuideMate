package com.ahmetkaragunlu.guidemate.screens.tourist.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.screens.common.formatting.isValidCurrencyInput
import com.ahmetkaragunlu.guidemate.screens.tourist.finance.store.TouristFinanceStore
import com.ahmetkaragunlu.guidemate.screens.tourist.payment.model.PaymentMethod
import com.ahmetkaragunlu.guidemate.screens.tourist.payment.model.PaymentPurpose
import com.ahmetkaragunlu.guidemate.screens.tourist.payment.store.TouristPaymentStore
import com.ahmetkaragunlu.guidemate.screens.tourist.wallet.model.TouristWalletUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel
class TouristWalletViewModel
    @Inject
    constructor(
        private val financeStore: TouristFinanceStore,
        private val paymentStore: TouristPaymentStore,
    ) : ViewModel() {
        private val actionState = MutableStateFlow(TouristWalletUiState())

        val uiState: StateFlow<TouristWalletUiState> =
            combine(financeStore.state, actionState) { finance, action ->
                action.copy(
                    balanceMinor = finance.balanceMinor,
                    savedCards = finance.savedCards,
                    transactions = finance.transactions.sortedByDescending { it.createdAt },
                    selectedCardId =
                        action.selectedCardId
                            ?.takeIf { selectedId ->
                                finance.savedCards.any { it.cardId == selectedId }
                            }
                            ?: finance.defaultCard?.cardId,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue =
                    TouristWalletUiState(
                        balanceMinor = financeStore.state.value.balanceMinor,
                        savedCards = financeStore.state.value.savedCards,
                        transactions =
                            financeStore.state.value.transactions
                                .sortedByDescending { it.createdAt },
                        selectedCardId = financeStore.state.value.defaultCard?.cardId,
                    ),
            )

        fun onTopUpAmountChange(value: String) {
            if (value.isValidCurrencyInput()) {
                actionState.update { it.copy(topUpAmount = value) }
            }
        }

        fun onTopUpPresetSelected(amount: Int) {
            actionState.update { it.copy(topUpAmount = amount.toString()) }
        }

        fun resetSelectedCardToDefault() {
            actionState.update {
                it.copy(selectedCardId = null)
            }
        }

        fun selectNextCard() {
            val nextCard = financeStore.nextCard(uiState.value.selectedCardId) ?: return
            actionState.update { it.copy(selectedCardId = nextCard.cardId) }
        }

        fun createTopUpAttempt(amountMinor: Long): String? {
            val selectedCardId = uiState.value.selectedCardId ?: return null
            if (amountMinor <= 0) return null

            val attemptId =
                paymentStore.createAttempt(
                    purpose = PaymentPurpose.WALLET_TOP_UP,
                    amountMinor = amountMinor,
                    method = PaymentMethod.SAVED_CARD,
                    savedCardId = selectedCardId,
                )
            actionState.update { it.copy(topUpAmount = "") }
            return attemptId
        }
    }
