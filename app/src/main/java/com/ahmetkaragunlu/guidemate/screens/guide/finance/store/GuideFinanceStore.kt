package com.ahmetkaragunlu.guidemate.screens.guide.finance.store

import com.ahmetkaragunlu.guidemate.screens.guide.finance.model.BankAccountUiModel
import com.ahmetkaragunlu.guidemate.screens.guide.finance.model.GuideFinanceState
import com.ahmetkaragunlu.guidemate.screens.guide.wallet.model.WalletTransactionStatus
import com.ahmetkaragunlu.guidemate.screens.guide.wallet.model.WalletTransactionType
import com.ahmetkaragunlu.guidemate.screens.guide.wallet.model.WalletTransactionUiModel
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Singleton
class GuideFinanceStore
    @Inject
    constructor() {
        private val _state = MutableStateFlow(createMockState())
        val state: StateFlow<GuideFinanceState> = _state.asStateFlow()

        fun deleteBankAccount(bankAccountId: String) {
            _state.update { current ->
                val remainingAccounts =
                    current.bankAccounts.filterNot { it.bankAccountId == bankAccountId }
                val normalizedAccounts =
                    if (
                        remainingAccounts.isNotEmpty() &&
                            remainingAccounts.none { it.isDefault }
                    ) {
                        remainingAccounts.mapIndexed { index, account ->
                            account.copy(isDefault = index == 0)
                        }
                    } else {
                        remainingAccounts
                    }
                current.copy(bankAccounts = normalizedAccounts)
            }
        }

        fun makeDefaultBankAccount(bankAccountId: String) {
            _state.update { current ->
                if (current.bankAccounts.none { it.bankAccountId == bankAccountId }) {
                    return@update current
                }
                current.copy(
                    bankAccounts =
                        current.bankAccounts
                            .map { account ->
                                account.copy(isDefault = account.bankAccountId == bankAccountId)
                            }
                            .sortedByDescending { it.isDefault },
                )
            }
        }

        fun addBankAccount(
            bankName: String,
            accountHolderName: String,
            iban: String,
        ) {
            _state.update { current ->
                val compactIban = iban.replace(" ", "").uppercase()
                val account =
                    BankAccountUiModel(
                        bankAccountId = UUID.randomUUID().toString(),
                        bankName = bankName.trim(),
                        accountHolderName = accountHolderName.trim(),
                        maskedIban = compactIban.toMaskedIban(),
                        isDefault = current.bankAccounts.isEmpty(),
                    )
                current.copy(bankAccounts = current.bankAccounts + account)
            }
        }

        fun nextBankAccount(currentBankAccountId: String?): BankAccountUiModel? {
            val accounts = state.value.bankAccounts
            if (accounts.isEmpty()) return null
            val currentIndex =
                accounts
                    .indexOfFirst { it.bankAccountId == currentBankAccountId }
                    .coerceAtLeast(0)
            return accounts[(currentIndex + 1) % accounts.size]
        }

        fun addPendingWithdrawal(
            amountMinor: Long,
            bankAccountId: String,
        ): Boolean {
            val current = state.value
            if (
                amountMinor <= 0 ||
                    amountMinor > current.availableWithdrawalBalanceMinor ||
                    current.bankAccounts.none { it.bankAccountId == bankAccountId }
            ) {
                return false
            }

            val now = Instant.now()
            val transaction =
                WalletTransactionUiModel(
                    id = UUID.randomUUID().toString(),
                    occurredAt = now,
                    amountMinor = amountMinor,
                    type = WalletTransactionType.WITHDRAWAL,
                    status = WalletTransactionStatus.PENDING,
                    bankAccountId = bankAccountId,
                )
            _state.update { it.copy(recentTransactions = listOf(transaction) + it.recentTransactions) }
            return true
        }

        private fun String.toMaskedIban(): String =
            if (length < 8) {
                this
            } else {
                "${take(4)} **** **** **** **** **${takeLast(4)}"
            }

        private companion object {
            fun createMockState(): GuideFinanceState =
                GuideFinanceState(
                    balanceMinor = 2_000_000,
                    bankAccounts =
                        listOf(
                            BankAccountUiModel(
                                bankAccountId = "guide-bank-account-1",
                                bankName = "İş Bankası",
                                accountHolderName = "Ahmet Karagünlü",
                                maskedIban = "TR12 **** **** **** **** **34",
                                isDefault = true,
                            ),
                            BankAccountUiModel(
                                bankAccountId = "guide-bank-account-2",
                                bankName = "Akbank",
                                accountHolderName = "Ahmet Karagünlü",
                                maskedIban = "TR98 **** **** **** **** **76",
                                isDefault = false,
                            ),
                        ),
                    recentTransactions =
                        listOf(
                            WalletTransactionUiModel(
                                id = "1",
                                occurredAt = Instant.parse("2026-02-15T11:30:00Z"),
                                amountMinor = 75_000,
                                type = WalletTransactionType.TOUR_INCOME,
                                referenceTitle = "Ayasofya Turu",
                            ),
                            WalletTransactionUiModel(
                                id = "2",
                                occurredAt = Instant.parse("2026-02-12T06:15:00Z"),
                                amountMinor = 500_000,
                                type = WalletTransactionType.WITHDRAWAL,
                            ),
                            WalletTransactionUiModel(
                                id = "3",
                                occurredAt = Instant.parse("2026-02-10T03:00:00Z"),
                                amountMinor = 150_000,
                                type = WalletTransactionType.TOUR_INCOME,
                                referenceTitle = "Kapadokya Balon Turu",
                            ),
                            WalletTransactionUiModel(
                                id = "4",
                                occurredAt = Instant.parse("2026-02-05T13:45:00Z"),
                                amountMinor = 300_000,
                                type = WalletTransactionType.WITHDRAWAL,
                            ),
                        ),
                )
        }
    }
