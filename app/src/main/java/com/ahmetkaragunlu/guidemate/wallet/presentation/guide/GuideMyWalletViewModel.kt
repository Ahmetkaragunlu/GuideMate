package com.ahmetkaragunlu.guidemate.wallet.presentation.guide

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.error.toMessage
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.wallet.domain.model.BankAccount
import com.ahmetkaragunlu.guidemate.wallet.domain.model.WalletAccount
import com.ahmetkaragunlu.guidemate.wallet.domain.repository.GuideFinanceRepository
import com.ahmetkaragunlu.guidemate.wallet.domain.repository.WalletRepository
import com.ahmetkaragunlu.guidemate.wallet.presentation.mapper.toMoneyActionMethodUi
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.bankaccounts.model.BankAccountUiModel
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.bankaccounts.model.toUiModel
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.model.GuideWalletUiState
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.model.WalletTransactionUiModel
import com.ahmetkaragunlu.guidemate.wallet.presentation.mapper.toGuideUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val GUIDE_WALLET_PREVIEW_SIZE = 3
private const val GUIDE_BANK_ACCOUNT_PAGE_SIZE = 50

@HiltViewModel
class GuideMyWalletViewModel
    @Inject
    constructor(
        private val walletRepository: WalletRepository,
        private val financeRepository: GuideFinanceRepository,
        private val resourceProvider: ResourceProvider,
        private val savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val walletAccount = MutableStateFlow<WalletAccount?>(null)
        private val canonicalTransactions = MutableStateFlow<List<WalletTransactionUiModel>>(emptyList())
        private val bankAccounts = MutableStateFlow<List<BankAccountUiModel>>(emptyList())
        private val actionState = MutableStateFlow(GuideWalletUiState())
        private var refreshJob: Job? = null

        val uiState: StateFlow<GuideWalletUiState> =
            combine(
                walletAccount,
                canonicalTransactions,
                bankAccounts,
                actionState,
            ) { account, transactions, accounts, action ->
                val defaultAccount = accounts.firstOrNull { it.isDefault } ?: accounts.firstOrNull()
                val selectedAccount =
                    accounts.firstOrNull { it.bankAccountId == action.selectedBankAccountId }
                        ?: defaultAccount
                action.copy(
                    availableBalanceMinor = account?.availableBalanceMinor ?: 0,
                    currencyCode = account?.currencyCode ?: action.currencyCode,
                    selectedBankAccountId = selectedAccount?.bankAccountId,
                    defaultMethod = defaultAccount?.toMoneyActionMethodUi(),
                    selectedMethod = selectedAccount?.toMoneyActionMethodUi(),
                    recentTransactions = transactions,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = GuideWalletUiState(),
            )

        init {
            refresh()
            viewModelScope.launch {
                financeRepository.financeChanges.collect { refresh() }
            }
        }

        fun refresh() {
            if (refreshJob?.isActive == true) return
            refreshJob =
                viewModelScope.launch {
                    val hasContent = walletAccount.value != null
                    if (!hasContent) {
                        actionState.update {
                            it.copy(loadState = ContentLoadState.LOADING, errorMessage = null)
                        }
                    }
                    val result = loadWalletData()
                    if (!result) {
                        actionState.update {
                            it.copy(
                                loadState =
                                    if (hasContent) ContentLoadState.CONTENT else ContentLoadState.ERROR,
                            )
                        }
                    }
                }
        }

        fun resetSelectedBankAccountToDefault() {
            actionState.update { it.copy(selectedBankAccountId = null) }
        }

        fun selectNextBankAccount() {
            val accounts = bankAccounts.value
            if (accounts.isEmpty()) return
            val currentIndex =
                accounts.indexOfFirst { it.bankAccountId == uiState.value.selectedBankAccountId }
                    .coerceAtLeast(0)
            val nextAccount = accounts[(currentIndex + 1) % accounts.size]
            actionState.update { it.copy(selectedBankAccountId = nextAccount.bankAccountId) }
        }

        fun requestWithdrawal(amountMinor: Long) {
            val bankAccountId = uiState.value.selectedBankAccountId ?: return
            if (amountMinor <= 0 || actionState.value.isWithdrawalInProgress) return
            val requestFingerprint = "$bankAccountId:$amountMinor"
            val idempotencyKey = idempotencyKeyFor(requestFingerprint)
            viewModelScope.launch {
                actionState.update {
                    it.copy(isWithdrawalInProgress = true, actionErrorMessage = null)
                }
                when (
                    val result =
                        financeRepository.requestWithdrawal(
                            bankAccountId = bankAccountId,
                            amountMinor = amountMinor,
                            idempotencyKey = idempotencyKey,
                        )
                ) {
                    is DataResult.Success -> {
                        clearWithdrawalIdempotency()
                        actionState.update {
                            it.copy(
                                isWithdrawalInProgress = false,
                                isWithdrawalRequestSubmitted = true,
                            )
                        }
                        refresh()
                    }
                    is DataResult.Error ->
                        actionState.update {
                            it.copy(
                                isWithdrawalInProgress = false,
                                actionErrorMessage = result.error.toMessage(resourceProvider),
                            )
                        }
                }
            }
        }

        fun dismissWithdrawalConfirmation() {
            actionState.update { it.copy(isWithdrawalRequestSubmitted = false) }
        }

        fun clearActionError() {
            actionState.update { it.copy(actionErrorMessage = null) }
        }

        private suspend fun loadWalletData(): Boolean =
            coroutineScope {
                val walletDeferred = async { walletRepository.getWallet() }
                val transactionsDeferred =
                    async {
                        walletRepository.getTransactions(page = 0, size = GUIDE_WALLET_PREVIEW_SIZE)
                    }
                val bankAccountsDeferred =
                    async {
                        financeRepository.getBankAccounts(page = 0, size = GUIDE_BANK_ACCOUNT_PAGE_SIZE)
                    }
                val wallet = walletDeferred.await()
                val transactions = transactionsDeferred.await()
                val accounts = bankAccountsDeferred.await()
                val error =
                    listOf(wallet, transactions, accounts)
                        .filterIsInstance<DataResult.Error>()
                        .firstOrNull()
                if (error != null) {
                    actionState.update {
                        it.copy(errorMessage = error.error.toMessage(resourceProvider))
                    }
                    return@coroutineScope false
                }

                walletAccount.value = (wallet as DataResult.Success).data
                canonicalTransactions.value =
                    (transactions as DataResult.Success).data.items.mapNotNull { it.toGuideUiModel() }
                bankAccounts.value =
                    (accounts as DataResult.Success).data.items
                        .map(BankAccount::toUiModel)
                        .sortedByDescending { it.isDefault }
                actionState.update {
                    it.copy(loadState = ContentLoadState.CONTENT, errorMessage = null)
                }
                true
            }

        private fun idempotencyKeyFor(fingerprint: String): String {
            val storedFingerprint = savedStateHandle.get<String>(WITHDRAWAL_FINGERPRINT)
            val storedKey = savedStateHandle.get<String>(WITHDRAWAL_IDEMPOTENCY_KEY)
            if (storedFingerprint == fingerprint && storedKey != null) return storedKey
            return UUID.randomUUID().toString().also { key ->
                savedStateHandle[WITHDRAWAL_FINGERPRINT] = fingerprint
                savedStateHandle[WITHDRAWAL_IDEMPOTENCY_KEY] = key
            }
        }

        private fun clearWithdrawalIdempotency() {
            savedStateHandle.remove<String>(WITHDRAWAL_FINGERPRINT)
            savedStateHandle.remove<String>(WITHDRAWAL_IDEMPOTENCY_KEY)
        }

        private companion object {
            const val WITHDRAWAL_FINGERPRINT = "withdrawal_fingerprint"
            const val WITHDRAWAL_IDEMPOTENCY_KEY = "withdrawal_idempotency_key"
        }
    }
