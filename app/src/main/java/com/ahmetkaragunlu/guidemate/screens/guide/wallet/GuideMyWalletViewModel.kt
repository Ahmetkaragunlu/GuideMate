package com.ahmetkaragunlu.guidemate.screens.guide.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.screens.guide.wallet.model.BankAccount
import com.ahmetkaragunlu.guidemate.screens.guide.wallet.model.EarningSummary
import com.ahmetkaragunlu.guidemate.screens.guide.wallet.model.GuideWalletUiState
import com.ahmetkaragunlu.guidemate.screens.guide.wallet.model.Transaction
import com.ahmetkaragunlu.guidemate.screens.guide.wallet.model.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GuideMyWalletViewModel @Inject constructor(
) : ViewModel() {

    val uiState: StateFlow<GuideWalletUiState> = flow {
        val mockData = GuideWalletUiState(
            totalBalance = 20000.0,
            selectedBankAccount = BankAccount(
                id = "bank_1",
                bankName = "İş Bankası",
                maskedIban = "TR12 **** **** 5678"
            ),
            earningSummaries = listOf(
                EarningSummary("1", "Şubat 2026", 10000.0),
                EarningSummary("2", "Ocak 2026", 8500.0),
                EarningSummary("3", "Aralık 2025", 12000.0),
                EarningSummary("4", "Kasım 2025", 7000.0)
            ),
            recentTransactions = listOf(
                Transaction("1", "Ayasofya Turu", 1707996600000, "15 Şub 2026, 14:30", 750.0, TransactionType.TOUR_INCOME),
                Transaction("2", "Para Çekimi", 1707729300000, "12 Şub 2026, 09:15", 5000.0, TransactionType.WITHDRAWAL),
                Transaction("3", "Kapadokya Balon Turu", 1707544800000, "10 Şub 2026, 06:00", 1500.0, TransactionType.TOUR_INCOME),
                Transaction("4", "Para Çekimi", 1707147900000, "05 Şub 2026, 16:45", 3000.0, TransactionType.WITHDRAWAL)
            )
        )
        emit(mockData)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = GuideWalletUiState()
    )

    fun withdrawMoney(amount: Double) {
        viewModelScope.launch {
        }
    }
}