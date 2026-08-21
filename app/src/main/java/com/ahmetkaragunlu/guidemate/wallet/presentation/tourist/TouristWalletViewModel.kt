package com.ahmetkaragunlu.guidemate.wallet.presentation.tourist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.common.ui.formatting.isValidCurrencyInput
import com.ahmetkaragunlu.guidemate.wallet.data.mock.tourist.TouristWalletStore
import com.ahmetkaragunlu.guidemate.payment.presentation.status.model.PaymentMethod
import com.ahmetkaragunlu.guidemate.payment.presentation.status.model.PaymentPurpose
import com.ahmetkaragunlu.guidemate.payment.data.mock.TouristPaymentStore
import com.ahmetkaragunlu.guidemate.wallet.presentation.tourist.model.TouristWalletUiState
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
        private val walletStore: TouristWalletStore,
        private val paymentStore: TouristPaymentStore,
    ) : ViewModel() {
        private val actionState = MutableStateFlow(TouristWalletUiState())

        val uiState: StateFlow<TouristWalletUiState> =
            combine(walletStore.state, actionState) { wallet, action ->
                action.copy(
                    balanceMinor = wallet.balanceMinor,
                    savedCards = wallet.savedCards,
                    transactions = wallet.transactions.sortedByDescending { it.createdAt },
                    selectedCardId =
                        action.selectedCardId
                            ?.takeIf { selectedId ->
                                wallet.savedCards.any { it.cardId == selectedId }
                            }
                            ?: wallet.defaultCard?.cardId,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue =
                    TouristWalletUiState(
                        balanceMinor = walletStore.state.value.balanceMinor,
                        savedCards = walletStore.state.value.savedCards,
                        transactions =
                            walletStore.state.value.transactions
                                .sortedByDescending { it.createdAt },
                        selectedCardId = walletStore.state.value.defaultCard?.cardId,
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
            val nextCard = walletStore.nextCard(uiState.value.selectedCardId) ?: return
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
