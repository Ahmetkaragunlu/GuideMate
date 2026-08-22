package com.ahmetkaragunlu.guidemate.wallet.presentation.tourist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.formatting.isValidCurrencyInput
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.payment.data.mock.TouristPaymentStore
import com.ahmetkaragunlu.guidemate.payment.domain.repository.SavedPaymentMethodRepository
import com.ahmetkaragunlu.guidemate.payment.presentation.mapper.toUiModel
import com.ahmetkaragunlu.guidemate.payment.presentation.status.model.PaymentMethod
import com.ahmetkaragunlu.guidemate.payment.presentation.status.model.PaymentPurpose
import com.ahmetkaragunlu.guidemate.wallet.domain.repository.WalletRepository
import com.ahmetkaragunlu.guidemate.wallet.presentation.mapper.toTouristUiModel
import com.ahmetkaragunlu.guidemate.wallet.presentation.tourist.model.TouristWalletUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val WALLET_PREVIEW_SIZE = 3

@HiltViewModel
class TouristWalletViewModel
    @Inject
    constructor(
        private val walletRepository: WalletRepository,
        private val savedPaymentMethodRepository: SavedPaymentMethodRepository,
        private val paymentStore: TouristPaymentStore,
    ) : ViewModel() {
        private val mutableUiState = MutableStateFlow(TouristWalletUiState())
        val uiState: StateFlow<TouristWalletUiState> = mutableUiState.asStateFlow()

        init {
            refresh()
            viewModelScope.launch {
                savedPaymentMethodRepository.paymentMethodChanges.collect { refreshCards() }
            }
        }

        fun refresh() {
            viewModelScope.launch {
                mutableUiState.update { it.copy(loadState = ContentLoadState.LOADING) }
                val wallet = walletRepository.getWallet()
                if (wallet is DataResult.Error) {
                    showLoadError()
                    return@launch
                }
                val transactions =
                    walletRepository.getTransactions(page = 0, size = WALLET_PREVIEW_SIZE)
                if (transactions is DataResult.Error) {
                    showLoadError()
                    return@launch
                }

                val walletData = (wallet as DataResult.Success).data
                val transactionData = (transactions as DataResult.Success).data
                mutableUiState.update { current ->
                    current.copy(
                        loadState = ContentLoadState.CONTENT,
                        balanceMinor = walletData.balanceMinor,
                        currencyCode = walletData.currencyCode,
                        transactions = transactionData.items.mapNotNull { it.toTouristUiModel() },
                    )
                }
                refreshCards()
            }
        }

        fun onTopUpAmountChange(value: String) {
            if (value.isValidCurrencyInput()) {
                mutableUiState.update { it.copy(topUpAmount = value) }
            }
        }

        fun onTopUpPresetSelected(amount: Int) {
            mutableUiState.update { it.copy(topUpAmount = amount.toString()) }
        }

        fun resetSelectedCardToDefault() {
            mutableUiState.update { current ->
                current.copy(selectedCardId = current.defaultCard?.cardId)
            }
        }

        fun selectNextCard() {
            val cards = uiState.value.savedCards
            if (cards.isEmpty()) return
            val currentIndex =
                cards.indexOfFirst { it.cardId == uiState.value.selectedCardId }.coerceAtLeast(0)
            mutableUiState.update {
                it.copy(selectedCardId = cards[(currentIndex + 1) % cards.size].cardId)
            }
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
            mutableUiState.update { it.copy(topUpAmount = "") }
            return attemptId
        }

        private suspend fun refreshCards() {
            when (val result = savedPaymentMethodRepository.getSavedPaymentMethods()) {
                is DataResult.Success -> {
                    val cards = result.data.map { it.toUiModel() }.sortedByDescending { it.isDefault }
                    mutableUiState.update { current ->
                        current.copy(
                            savedCards = cards,
                            selectedCardId =
                                current.selectedCardId?.takeIf { selectedId ->
                                    cards.any { it.cardId == selectedId }
                                } ?: cards.firstOrNull { it.isDefault }?.cardId
                                    ?: cards.firstOrNull()?.cardId,
                        )
                    }
                }
                is DataResult.Error ->
                    mutableUiState.update {
                        it.copy(loadState = ContentLoadState.ERROR)
                    }
            }
        }

        private fun showLoadError() {
            mutableUiState.update { it.copy(loadState = ContentLoadState.ERROR) }
        }
    }
