package com.ahmetkaragunlu.guidemate.wallet.domain.repository

import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.wallet.domain.model.BankAccount
import com.ahmetkaragunlu.guidemate.wallet.domain.model.GuideEarning
import com.ahmetkaragunlu.guidemate.wallet.domain.model.MonthlyGuideEarning
import com.ahmetkaragunlu.guidemate.wallet.domain.model.Withdrawal
import kotlinx.coroutines.flow.Flow

interface GuideFinanceRepository {
    val financeChanges: Flow<Unit>

    suspend fun getEarnings(
        year: Int,
        page: Int,
        size: Int,
    ): DataResult<PagedResult<GuideEarning>>

    suspend fun getMonthlyEarnings(year: Int): DataResult<List<MonthlyGuideEarning>>

    suspend fun getBankAccounts(
        page: Int,
        size: Int,
    ): DataResult<PagedResult<BankAccount>>

    suspend fun addBankAccount(
        iban: String,
        accountHolderName: String,
    ): DataResult<BankAccount>

    suspend fun makeDefaultBankAccount(bankAccountId: String): DataResult<BankAccount>

    suspend fun deleteBankAccount(bankAccountId: String): DataResult<Unit>

    suspend fun getWithdrawals(
        page: Int,
        size: Int,
    ): DataResult<PagedResult<Withdrawal>>

    suspend fun requestWithdrawal(
        bankAccountId: String,
        amountMinor: Long,
        idempotencyKey: String,
    ): DataResult<Withdrawal>
}
