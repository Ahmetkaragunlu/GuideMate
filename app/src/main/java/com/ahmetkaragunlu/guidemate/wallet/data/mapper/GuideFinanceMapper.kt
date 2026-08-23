package com.ahmetkaragunlu.guidemate.wallet.data.mapper

import com.ahmetkaragunlu.guidemate.common.network.model.ApiPageResponse
import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.wallet.data.remote.model.BankAccountResponseDto
import com.ahmetkaragunlu.guidemate.wallet.data.remote.model.GuideEarningResponseDto
import com.ahmetkaragunlu.guidemate.wallet.data.remote.model.MonthlyGuideEarningResponseDto
import com.ahmetkaragunlu.guidemate.wallet.data.remote.model.WithdrawalResponseDto
import com.ahmetkaragunlu.guidemate.wallet.domain.model.BankAccount
import com.ahmetkaragunlu.guidemate.wallet.domain.model.GuideEarning
import com.ahmetkaragunlu.guidemate.wallet.domain.model.GuideEarningStatus
import com.ahmetkaragunlu.guidemate.wallet.domain.model.MonthlyGuideEarning
import com.ahmetkaragunlu.guidemate.wallet.domain.model.PayoutMode
import com.ahmetkaragunlu.guidemate.wallet.domain.model.Withdrawal
import com.ahmetkaragunlu.guidemate.wallet.domain.model.WithdrawalStatus

fun ApiPageResponse<GuideEarningResponseDto>.toGuideEarningsDomain(): PagedResult<GuideEarning> =
    toPagedResult(GuideEarningResponseDto::toDomain)

fun ApiPageResponse<BankAccountResponseDto>.toBankAccountsDomain(): PagedResult<BankAccount> =
    toPagedResult(BankAccountResponseDto::toDomain)

fun ApiPageResponse<WithdrawalResponseDto>.toWithdrawalsDomain(): PagedResult<Withdrawal> =
    toPagedResult(WithdrawalResponseDto::toDomain)

fun GuideEarningResponseDto.toDomain(): GuideEarning =
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

fun MonthlyGuideEarningResponseDto.toDomain(): MonthlyGuideEarning =
    MonthlyGuideEarning(
        year = year,
        month = month,
        netEarningsMinor = netEarningsMinor,
        currencyCode = currencyCode,
    )

fun BankAccountResponseDto.toDomain(): BankAccount =
    BankAccount(
        id = bankAccountId,
        maskedIban = maskedIban,
        bankCode = bankCode,
        bankName = bankName,
        accountHolderName = accountHolderName,
        isDefault = defaultAccount,
        createdAt = createdAt,
    )

fun WithdrawalResponseDto.toDomain(): Withdrawal =
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
