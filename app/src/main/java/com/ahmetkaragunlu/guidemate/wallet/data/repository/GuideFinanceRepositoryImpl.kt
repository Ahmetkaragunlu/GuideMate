package com.ahmetkaragunlu.guidemate.wallet.data.repository

import com.ahmetkaragunlu.guidemate.common.network.ApiCallExecutor
import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.wallet.data.mapper.toBankAccountsDomain
import com.ahmetkaragunlu.guidemate.wallet.data.mapper.toDomain
import com.ahmetkaragunlu.guidemate.wallet.data.mapper.toGuideEarningsDomain
import com.ahmetkaragunlu.guidemate.wallet.data.mapper.toWithdrawalsDomain
import com.ahmetkaragunlu.guidemate.wallet.data.remote.api.GuideFinanceApi
import com.ahmetkaragunlu.guidemate.wallet.data.remote.model.AddBankAccountRequestDto
import com.ahmetkaragunlu.guidemate.wallet.data.remote.model.WithdrawalRequestDto
import com.ahmetkaragunlu.guidemate.wallet.domain.model.BankAccount
import com.ahmetkaragunlu.guidemate.wallet.domain.model.GuideEarning
import com.ahmetkaragunlu.guidemate.wallet.domain.model.MonthlyGuideEarning
import com.ahmetkaragunlu.guidemate.wallet.domain.model.Withdrawal
import com.ahmetkaragunlu.guidemate.wallet.domain.repository.GuideFinanceRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class GuideFinanceRepositoryImpl @Inject constructor(
    private val api: GuideFinanceApi,
    private val apiCallExecutor: ApiCallExecutor,
) : GuideFinanceRepository {
    private val mutableFinanceChanges = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    override val financeChanges: Flow<Unit> = mutableFinanceChanges.asSharedFlow()

    override suspend fun getEarnings(
        year: Int,
        page: Int,
        size: Int,
    ): DataResult<PagedResult<GuideEarning>> =
        apiCallExecutor.execute(
            request = { api.getEarnings(year = year, page = page, size = size) },
            transform = { it.toGuideEarningsDomain() },
        )

    override suspend fun getMonthlyEarnings(
        year: Int,
    ): DataResult<List<MonthlyGuideEarning>> =
        apiCallExecutor.execute(
            request = { api.getMonthlyEarnings(year) },
            transform = { response -> response.map { it.toDomain() } },
        )

    override suspend fun getBankAccounts(
        page: Int,
        size: Int,
    ): DataResult<PagedResult<BankAccount>> =
        apiCallExecutor.execute(
            request = { api.getBankAccounts(page = page, size = size) },
            transform = { it.toBankAccountsDomain() },
        )

    override suspend fun addBankAccount(
        iban: String,
        accountHolderName: String,
    ): DataResult<BankAccount> =
        apiCallExecutor.execute(
            request = {
                api.addBankAccount(
                    AddBankAccountRequestDto(
                        iban = iban,
                        accountHolderName = accountHolderName,
                    ),
                )
            },
            transform = { it.toDomain() },
        ).also(::emitChangeOnSuccess)

    override suspend fun makeDefaultBankAccount(
        bankAccountId: String,
    ): DataResult<BankAccount> =
        apiCallExecutor.execute(
            request = { api.makeDefaultBankAccount(bankAccountId) },
            transform = { it.toDomain() },
        ).also(::emitChangeOnSuccess)

    override suspend fun deleteBankAccount(bankAccountId: String): DataResult<Unit> =
        apiCallExecutor.executeUnit { api.deleteBankAccount(bankAccountId) }
            .also(::emitChangeOnSuccess)

    override suspend fun getWithdrawals(
        page: Int,
        size: Int,
    ): DataResult<PagedResult<Withdrawal>> =
        apiCallExecutor.execute(
            request = { api.getWithdrawals(page = page, size = size) },
            transform = { it.toWithdrawalsDomain() },
        )

    override suspend fun requestWithdrawal(
        bankAccountId: String,
        amountMinor: Long,
        idempotencyKey: String,
    ): DataResult<Withdrawal> =
        apiCallExecutor.execute(
            request = {
                api.requestWithdrawal(
                    idempotencyKey = idempotencyKey,
                    request =
                        WithdrawalRequestDto(
                            bankAccountId = bankAccountId,
                            amountMinor = amountMinor,
                        ),
                )
            },
            transform = { it.toDomain() },
        ).also(::emitChangeOnSuccess)

    private fun emitChangeOnSuccess(result: DataResult<*>) {
        if (result is DataResult.Success) mutableFinanceChanges.tryEmit(Unit)
    }
}
