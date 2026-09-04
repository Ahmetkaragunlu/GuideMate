package com.ahmetkaragunlu.guidemate.testing

import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.payment.domain.model.CheckoutCurrencies
import com.ahmetkaragunlu.guidemate.payment.domain.model.CheckoutCurrency
import com.ahmetkaragunlu.guidemate.payment.domain.model.CheckoutLocale
import com.ahmetkaragunlu.guidemate.payment.domain.model.Payment
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentMethod
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentPurpose
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentQuote
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentStatus
import com.ahmetkaragunlu.guidemate.payment.domain.model.SavedPaymentMethod
import com.ahmetkaragunlu.guidemate.payment.domain.repository.PaymentRepository
import com.ahmetkaragunlu.guidemate.payment.domain.repository.SavedPaymentMethodRepository
import com.ahmetkaragunlu.guidemate.wallet.domain.model.BankAccount
import com.ahmetkaragunlu.guidemate.wallet.domain.model.GuideEarning
import com.ahmetkaragunlu.guidemate.wallet.domain.model.MonthlyGuideEarning
import com.ahmetkaragunlu.guidemate.wallet.domain.model.PayoutMode
import com.ahmetkaragunlu.guidemate.wallet.domain.model.WalletAccount
import com.ahmetkaragunlu.guidemate.wallet.domain.model.WalletTransaction
import com.ahmetkaragunlu.guidemate.wallet.domain.model.Withdrawal
import com.ahmetkaragunlu.guidemate.wallet.domain.model.WithdrawalStatus
import com.ahmetkaragunlu.guidemate.wallet.domain.repository.GuideFinanceRepository
import com.ahmetkaragunlu.guidemate.wallet.domain.repository.WalletRepository
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeWalletRepository : WalletRepository {
    var walletResult: DataResult<WalletAccount> =
        DataResult.Success(WalletAccount(20_000, 20_000, "USD"))
    var transactionsResult: DataResult<PagedResult<WalletTransaction>> =
        DataResult.Success(emptyPage())

    override suspend fun getWallet(): DataResult<WalletAccount> = walletResult

    override suspend fun getTransactions(
        page: Int,
        size: Int,
    ): DataResult<PagedResult<WalletTransaction>> = transactionsResult
}

class FakeSavedPaymentMethodRepository : SavedPaymentMethodRepository {
    override val paymentMethodChanges: Flow<Unit> = MutableSharedFlow()
    var methodsResult: DataResult<List<SavedPaymentMethod>> = DataResult.Success(emptyList())

    override suspend fun getSavedPaymentMethods(): DataResult<List<SavedPaymentMethod>> =
        methodsResult

    override suspend fun makeDefault(
        savedPaymentMethodId: String
    ): DataResult<SavedPaymentMethod> = error("Not required by this test fixture")

    override suspend fun delete(savedPaymentMethodId: String): DataResult<Unit> =
        error("Not required by this test fixture")
}

class FakePaymentRepository : PaymentRepository {
    override val pendingPaymentId: Flow<String?> = MutableStateFlow(null)
    var currenciesResult: DataResult<CheckoutCurrencies> =
        DataResult.Success(
            CheckoutCurrencies(
                baseCurrencyCode = "USD",
                chargeCurrencies = listOf(CheckoutCurrency("USD", 2)),
            )
        )
    var topUpQuoteResult: DataResult<PaymentQuote> = DataResult.Success(testTopUpQuote())
    var topUpCheckoutResult: DataResult<Payment> = DataResult.Success(testTopUpPayment())
    var quotedTopUp: Pair<Long, String>? = null
    var checkedOutQuoteId: String? = null
    var checkoutIdempotencyKey: String? = null
    val paymentResults = ArrayDeque<DataResult<Payment>>()
    var cancelResult: DataResult<Payment> = DataResult.Success(testTopUpPayment())
    var cancelledPaymentId: String? = null
    var clearAllPendingPaymentCalls = 0

    override suspend fun getCheckoutCurrencies(): DataResult<CheckoutCurrencies> = currenciesResult

    override suspend fun quoteTour(
        sessionId: String,
        participantCount: Int,
        chargeCurrencyCode: String,
    ): DataResult<PaymentQuote> = error("Not required by this test fixture")

    override suspend fun quoteWalletTopUp(
        amountMinor: Long,
        chargeCurrencyCode: String,
    ): DataResult<PaymentQuote> {
        quotedTopUp = amountMinor to chargeCurrencyCode
        return topUpQuoteResult
    }

    override suspend fun checkoutTour(
        sessionId: String,
        participantCount: Int,
        method: PaymentMethod,
        quoteId: String?,
        locale: CheckoutLocale,
        idempotencyKey: String,
    ): DataResult<Payment> = error("Not required by this test fixture")

    override suspend fun checkoutWalletTopUp(
        quoteId: String,
        locale: CheckoutLocale,
        idempotencyKey: String,
    ): DataResult<Payment> {
        checkedOutQuoteId = quoteId
        checkoutIdempotencyKey = idempotencyKey
        return topUpCheckoutResult
    }

    override suspend fun getPayment(paymentId: String): DataResult<Payment> =
        paymentResults.removeFirst()

    override suspend fun cancelPayment(paymentId: String): DataResult<Payment> {
        cancelledPaymentId = paymentId
        return cancelResult
    }

    override suspend fun clearPendingPayment(paymentId: String) = Unit

    override suspend fun clearPendingPayment() {
        clearAllPendingPaymentCalls++
    }
}

class FakeGuideFinanceRepository : GuideFinanceRepository {
    override val financeChanges: Flow<Unit> = MutableSharedFlow()
    var bankAccountsResult: DataResult<PagedResult<BankAccount>> =
        DataResult.Success(emptyPage())
    var withdrawalResult: DataResult<Withdrawal> = DataResult.Success(testWithdrawal())
    var withdrawalRequest: Triple<String, Long, String>? = null
    var addBankAccountResult: DataResult<BankAccount> = DataResult.Success(testBankAccount())
    var addBankAccountRequest: Pair<String, String>? = null

    override suspend fun getEarnings(
        year: Int,
        page: Int,
        size: Int,
    ): DataResult<PagedResult<GuideEarning>> = error("Not required by this test fixture")

    override suspend fun getMonthlyEarnings(
        year: Int
    ): DataResult<List<MonthlyGuideEarning>> = error("Not required by this test fixture")

    override suspend fun getBankAccounts(
        page: Int,
        size: Int,
    ): DataResult<PagedResult<BankAccount>> = bankAccountsResult

    override suspend fun addBankAccount(
        iban: String,
        accountHolderName: String,
    ): DataResult<BankAccount> {
        addBankAccountRequest = iban to accountHolderName
        return addBankAccountResult
    }

    override suspend fun makeDefaultBankAccount(
        bankAccountId: String
    ): DataResult<BankAccount> = error("Not required by this test fixture")

    override suspend fun deleteBankAccount(bankAccountId: String): DataResult<Unit> =
        error("Not required by this test fixture")

    override suspend fun getWithdrawals(
        page: Int,
        size: Int,
    ): DataResult<PagedResult<Withdrawal>> = error("Not required by this test fixture")

    override suspend fun requestWithdrawal(
        bankAccountId: String,
        amountMinor: Long,
        idempotencyKey: String,
    ): DataResult<Withdrawal> {
        withdrawalRequest = Triple(bankAccountId, amountMinor, idempotencyKey)
        return withdrawalResult
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

fun testTopUpQuote(): PaymentQuote =
    PaymentQuote(
        id = "quote-1",
        purpose = PaymentPurpose.WALLET_TOP_UP,
        baseAmountMinor = 5_000,
        baseCurrencyCode = "USD",
        chargeAmountMinor = 5_000,
        chargeCurrencyCode = "USD",
        fxRate = BigDecimal.ONE,
        rateSource = "TEST",
        rateDate = LocalDate.of(2026, 1, 1),
        quotedAt = Instant.parse("2026-01-01T00:00:00Z"),
        expiresAt = Instant.parse("2099-01-01T00:00:00Z"),
    )

fun testTopUpPayment(): Payment =
    Payment(
        id = "payment-1",
        purpose = PaymentPurpose.WALLET_TOP_UP,
        method = PaymentMethod.HOSTED_CARD,
        status = PaymentStatus.REQUIRES_ACTION,
        amountMinor = 5_000,
        currencyCode = "USD",
        quoteId = "quote-1",
        chargeAmountMinor = 5_000,
        chargeCurrencyCode = "USD",
        fxRate = BigDecimal.ONE,
        fxRateSource = "TEST",
        fxQuotedAt = Instant.parse("2026-01-01T00:00:00Z"),
        paymentPageUrl = "https://sandbox.example.com/payment",
        expiresAt = Instant.parse("2099-01-01T00:00:00Z"),
        reservationId = null,
        reservationStatus = null,
        refundId = null,
        refundStatus = null,
        refundAmountMinor = null,
        refundChargeAmountMinor = null,
        refundChargeCurrencyCode = null,
        failureCode = null,
        createdAt = Instant.parse("2026-01-01T00:00:00Z"),
        updatedAt = Instant.parse("2026-01-01T00:00:00Z"),
    )

fun <T> emptyPage(): PagedResult<T> =
    PagedResult(
        items = emptyList(),
        page = 0,
        size = 20,
        totalElements = 0,
        totalPages = 0,
        isFirst = true,
        isLast = true,
    )
