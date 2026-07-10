package com.ahmetkaragunlu.guidemate.screens.guide.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.screens.common.profile.account.savedcards.findDefaultSavedCard
import com.ahmetkaragunlu.guidemate.screens.common.profile.account.savedcards.findNextSavedCard
import com.ahmetkaragunlu.guidemate.screens.common.profile.account.savedcards.model.SavedCardUi
import com.ahmetkaragunlu.guidemate.screens.common.profile.account.savedcards.model.toMoneyActionMethodUi
import com.ahmetkaragunlu.guidemate.screens.guide.profile.account.savedcards.viewmodel.GuideSavedCardsViewModel
import com.ahmetkaragunlu.guidemate.screens.guide.wallet.model.GuideWalletUiState
import com.ahmetkaragunlu.guidemate.screens.guide.wallet.model.Transaction
import com.ahmetkaragunlu.guidemate.screens.guide.wallet.model.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GuideMyWalletViewModel
    @Inject
    constructor() : ViewModel() {
        private val _uiState =
            MutableStateFlow(
                GuideWalletUiState(
                    totalBalance = 20000.0,
                    recentTransactions =
                        listOf(
                            Transaction("1", "Ayasofya Turu", 1707996600000, "15 Şub 2026, 14:30", 750.0, TransactionType.TOUR_INCOME),
                            Transaction("2", "Para Çekimi", 1707729300000, "12 Şub 2026, 09:15", 5000.0, TransactionType.WITHDRAWAL),
                            Transaction(
                                "3",
                                "Kapadokya Balon Turu",
                                1707544800000,
                                "10 Şub 2026, 06:00",
                                1500.0,
                                TransactionType.TOUR_INCOME,
                            ),
                            Transaction("4", "Para Çekimi", 1707147900000, "05 Şub 2026, 16:45", 3000.0, TransactionType.WITHDRAWAL),
                        ),
                ),
            )
        val uiState: StateFlow<GuideWalletUiState> = _uiState.asStateFlow()

        private var isWalletLoaded = false

        fun loadWalletData() {
            if (isWalletLoaded) return
            isWalletLoaded = true

            viewModelScope.launch {
                GuideSavedCardsViewModel.savedCards.collect { cards ->
                    updateSelectedMethodFromDefault(cards)
                }
            }
        }

        fun syncSelectedCardWithDefault() {
            updateSelectedMethodFromDefault(GuideSavedCardsViewModel.getSavedCards())
        }

        fun onChangeBankClick() {
            val nextCard =
                findNextSavedCard(
                    cards = GuideSavedCardsViewModel.getSavedCards(),
                    currentCardId = _uiState.value.selectedCardId,
                ) ?: return

            _uiState.update {
                it.copy(
                    selectedCardId = nextCard.cardId,
                    selectedMethod = nextCard.toMoneyActionMethodUi(),
                )
            }
        }

        fun withdrawMoney(amount: Double) {
            viewModelScope.launch {}
        }

        private fun updateSelectedMethodFromDefault(cards: List<SavedCardUi>) {
            val defaultCard = findDefaultSavedCard(cards)
            _uiState.update {
                it.copy(
                    selectedCardId = defaultCard?.cardId,
                    selectedMethod = defaultCard?.toMoneyActionMethodUi(),
                )
            }
        }
    }
