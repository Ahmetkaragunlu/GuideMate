package com.ahmetkaragunlu.guidemate.wallet.data.mock.guide

import com.ahmetkaragunlu.guidemate.wallet.data.mock.guide.model.BankAccountUiModel
import com.ahmetkaragunlu.guidemate.wallet.data.mock.guide.model.GuideWalletState
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.model.WalletTransactionStatus
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.model.WalletTransactionType
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.model.WalletTransactionUiModel
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Singleton
class GuideWalletStore
    @Inject
    constructor() {
        private val mutableState = MutableStateFlow(createMockState())
        val state: StateFlow<GuideWalletState> = mutableState.asStateFlow()

        fun deleteBankAccount(bankAccountId: String) {
            mutableState.update { current ->
                val remainingAccounts =
                    current.bankAccounts.filterNot { it.bankAccountId == bankAccountId }
                val normalizedAccounts =
                    if (remainingAccounts.isNotEmpty() && remainingAccounts.none { it.isDefault }) {
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
            mutableState.update { current ->
                if (current.bankAccounts.none { it.bankAccountId == bankAccountId }) {
                    return@update current
                }
                current.copy(
                    bankAccounts =
                        current.bankAccounts
                            .map { account ->
                                account.copy(isDefault = account.bankAccountId == bankAccountId)
                            }.sortedByDescending { it.isDefault },
                )
            }
        }

        fun addBankAccount(
            bankName: String,
            accountHolderName: String,
            iban: String,
        ) {
            mutableState.update { current ->
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
                accounts.indexOfFirst { it.bankAccountId == currentBankAccountId }.coerceAtLeast(0)
            return accounts[(currentIndex + 1) % accounts.size]
        }

        fun addPendingWithdrawal(
            amountMinor: Long,
            availableBalanceMinor: Long,
            currencyCode: String,
            bankAccountId: String,
        ): Boolean {
            val current = state.value
            if (
                amountMinor <= 0 ||
                    amountMinor > availableBalanceMinor ||
                    current.bankAccounts.none { it.bankAccountId == bankAccountId }
            ) {
                return false
            }

            val transaction =
                WalletTransactionUiModel(
                    id = UUID.randomUUID().toString(),
                    occurredAt = Instant.now(),
                    amountMinor = amountMinor,
                    currencyCode = currencyCode,
                    type = WalletTransactionType.WITHDRAWAL,
                    status = WalletTransactionStatus.PENDING,
                    bankAccountId = bankAccountId,
                )
            mutableState.update {
                it.copy(pendingWithdrawals = listOf(transaction) + it.pendingWithdrawals)
            }
            return true
        }

        private fun String.toMaskedIban(): String =
            if (length < 8) this else "${take(4)} **** **** **** **** **${takeLast(4)}"

        private companion object {
            fun createMockState(): GuideWalletState =
                GuideWalletState(
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
                )
        }
    }
