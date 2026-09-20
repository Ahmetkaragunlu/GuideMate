package com.ahmetkaragunlu.guidemate.testing.wallet

import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.testing.common.emptyPage
import com.ahmetkaragunlu.guidemate.wallet.domain.model.BankAccount
import com.ahmetkaragunlu.guidemate.wallet.domain.model.GuideEarning
import com.ahmetkaragunlu.guidemate.wallet.domain.model.MonthlyGuideEarning
import com.ahmetkaragunlu.guidemate.wallet.domain.model.PayoutMode
import com.ahmetkaragunlu.guidemate.wallet.domain.model.Withdrawal
import com.ahmetkaragunlu.guidemate.wallet.domain.model.WithdrawalStatus
import com.ahmetkaragunlu.guidemate.wallet.domain.repository.GuideFinanceRepository
import java.time.Instant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

data class WithdrawalCall(
    val bankAccountId: String,
    val amountMinor: Long,
    val idempotencyKey: String,
)

data class AddBankAccountCall(
    val iban: String,
    val accountHolderName: String,
)

class GuideFinanceFakeResults {
    var bankAccounts: DataResult<PagedResult<BankAccount>> =
        DataResult.Success(emptyPage())
    var withdrawal: DataResult<Withdrawal> = DataResult.Success(testWithdrawal())
    var addBankAccount: DataResult<BankAccount> = DataResult.Success(testBankAccount())
    var makeDefaultBankAccount: DataResult<BankAccount> =
        DataResult.Success(testBankAccount())
    var deleteBankAccount: DataResult<Unit> = DataResult.Success(Unit)
    var monthlyEarnings: DataResult<List<MonthlyGuideEarning>> = DataResult.Success(emptyList())
}

class GuideFinanceFakeCalls {
    var getBankAccounts = 0
    var withdrawal: WithdrawalCall? = null
    val withdrawals = mutableListOf<WithdrawalCall>()
    var addBankAccount: AddBankAccountCall? = null
    val makeDefaultBankAccountRequests = mutableListOf<String>()
    val deleteBankAccountRequests = mutableListOf<String>()
    var getMonthlyEarnings = 0
    val requestedMonthlyEarningsYears = mutableListOf<Int>()
}

class FakeGuideFinanceRepository : GuideFinanceRepository {
    override val financeChanges: Flow<Unit> = MutableSharedFlow()
    val results = GuideFinanceFakeResults()
    val calls = GuideFinanceFakeCalls()

    override suspend fun getEarnings(
        year: Int,
        page: Int,
        size: Int,
    ): DataResult<PagedResult<GuideEarning>> = error("Not required by this test fixture")

    override suspend fun getMonthlyEarnings(
        year: Int,
    ): DataResult<List<MonthlyGuideEarning>> {
        calls.getMonthlyEarnings++
        calls.requestedMonthlyEarningsYears += year
        return results.monthlyEarnings
    }

    override suspend fun getBankAccounts(
        page: Int,
        size: Int,
    ): DataResult<PagedResult<BankAccount>> {
        calls.getBankAccounts++
        return results.bankAccounts
    }

    override suspend fun addBankAccount(
        iban: String,
        accountHolderName: String,
    ): DataResult<BankAccount> {
        calls.addBankAccount =
            AddBankAccountCall(
                iban = iban,
                accountHolderName = accountHolderName,
            )
        return results.addBankAccount
    }

    override suspend fun makeDefaultBankAccount(
        bankAccountId: String,
    ): DataResult<BankAccount> {
        calls.makeDefaultBankAccountRequests += bankAccountId
        return results.makeDefaultBankAccount
    }

    override suspend fun deleteBankAccount(bankAccountId: String): DataResult<Unit> {
        calls.deleteBankAccountRequests += bankAccountId
        return results.deleteBankAccount
    }

    override suspend fun getWithdrawals(
        page: Int,
        size: Int,
    ): DataResult<PagedResult<Withdrawal>> = error("Not required by this test fixture")

    override suspend fun requestWithdrawal(
        bankAccountId: String,
        amountMinor: Long,
        idempotencyKey: String,
    ): DataResult<Withdrawal> {
        val call =
            WithdrawalCall(
                bankAccountId = bankAccountId,
                amountMinor = amountMinor,
                idempotencyKey = idempotencyKey,
            )
        calls.withdrawal = call
        calls.withdrawals += call
        return results.withdrawal
    }
}

fun testBankAccount(
    id: String = "bank-1",
    isDefault: Boolean = true,
): BankAccount =
    BankAccount(
        id = id,
        maskedIban = "TR** **** 1234",
        bankCode = "00010",
        bankName = "Test Bank",
        accountHolderName = "Ada Guide",
        isDefault = isDefault,
        createdAt = Instant.parse("2026-01-01T00:00:00Z"),
    )

fun testWithdrawal(): Withdrawal =
    Withdrawal(
        id = "withdrawal-1",
        bankAccountId = "bank-1",
        maskedIban = "TR** **** 1234",
        amountMinor = 5_000,
        currencyCode = "USD",
        status = WithdrawalStatus.PENDING,
        payoutMode = PayoutMode.SIMULATED,
        requestedAt = Instant.parse("2026-01-01T00:00:00Z"),
        completedAt = null,
        failureCode = null,
    )
