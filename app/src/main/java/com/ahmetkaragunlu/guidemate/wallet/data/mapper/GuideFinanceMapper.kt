package com.ahmetkaragunlu.guidemate.wallet.data.mapper

import com.ahmetkaragunlu.guidemate.common.network.model.response.ApiPageResponse
import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.wallet.data.remote.model.response.BankAccountResponse
import com.ahmetkaragunlu.guidemate.wallet.data.remote.model.response.GuideEarningResponse
import com.ahmetkaragunlu.guidemate.wallet.data.remote.model.response.MonthlyGuideEarningResponse
import com.ahmetkaragunlu.guidemate.wallet.data.remote.model.response.WithdrawalResponse
import com.ahmetkaragunlu.guidemate.wallet.domain.model.BankAccount
import com.ahmetkaragunlu.guidemate.wallet.domain.model.GuideEarning
import com.ahmetkaragunlu.guidemate.wallet.domain.model.GuideEarningStatus
import com.ahmetkaragunlu.guidemate.wallet.domain.model.MonthlyGuideEarning
import com.ahmetkaragunlu.guidemate.wallet.domain.model.PayoutMode
import com.ahmetkaragunlu.guidemate.wallet.domain.model.Withdrawal
import com.ahmetkaragunlu.guidemate.wallet.domain.model.WithdrawalStatus

fun ApiPageResponse<GuideEarningResponse>.toGuideEarningsDomain(): PagedResult<GuideEarning> =
    toPagedResult(GuideEarningResponse::toDomain)

fun ApiPageResponse<BankAccountResponse>.toBankAccountsDomain(): PagedResult<BankAccount> =
    toPagedResult(BankAccountResponse::toDomain)

fun ApiPageResponse<WithdrawalResponse>.toWithdrawalsDomain(): PagedResult<Withdrawal> =
    toPagedResult(WithdrawalResponse::toDomain)

fun GuideEarningResponse.toDomain(): GuideEarning =
    GuideEarning(
        id = earningId,
        reservationId = reservationId,
        grossMinor = grossMinor,
        platformFeeMinor = platformFeeMinor,
        netMinor = netMinor,
        currencyCode = currencyCode,
        status = GuideEarningStatus.valueOf(status),
        availableAt = availableAt,
        createdAt = createdAt,
    )

fun MonthlyGuideEarningResponse.toDomain(): MonthlyGuideEarning =
    MonthlyGuideEarning(
        year = year,
        month = month,
        netEarningsMinor = netEarningsMinor,
        currencyCode = currencyCode,
        pendingEarningsMinor = pendingEarningsMinor,
    )

fun BankAccountResponse.toDomain(): BankAccount =
    BankAccount(
        id = bankAccountId,
        maskedIban = maskedIban,
        bankCode = bankCode,
        bankName = bankName,
        accountHolderName = accountHolderName,
        isDefault = defaultAccount,
        createdAt = createdAt,
    )

fun WithdrawalResponse.toDomain(): Withdrawal =
    Withdrawal(
        id = withdrawalId,
        bankAccountId = bankAccountId,
        maskedIban = maskedIban,
        amountMinor = amountMinor,
        currencyCode = currencyCode,
        status = WithdrawalStatus.valueOf(status),
        payoutMode = PayoutMode.valueOf(payoutMode),
        requestedAt = requestedAt,
        completedAt = completedAt,
        failureCode = failureCode,
    )

private fun <Dto, Domain> ApiPageResponse<Dto>.toPagedResult(
    transform: (Dto) -> Domain,
): PagedResult<Domain> =
    PagedResult(
        items = content.map(transform),
        page = page,
        size = size,
        totalElements = totalElements,
        totalPages = totalPages,
        isFirst = isFirst,
        isLast = isLast,
    )
