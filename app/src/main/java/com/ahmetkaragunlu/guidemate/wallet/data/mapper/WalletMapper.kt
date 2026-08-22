package com.ahmetkaragunlu.guidemate.wallet.data.mapper

import com.ahmetkaragunlu.guidemate.common.network.model.ApiPageResponse
import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.wallet.data.remote.model.WalletResponseDto
import com.ahmetkaragunlu.guidemate.wallet.data.remote.model.WalletTransactionResponseDto
import com.ahmetkaragunlu.guidemate.wallet.domain.model.WalletAccount
import com.ahmetkaragunlu.guidemate.wallet.domain.model.WalletTransaction
import com.ahmetkaragunlu.guidemate.wallet.domain.model.WalletTransactionDirection
import com.ahmetkaragunlu.guidemate.wallet.domain.model.WalletTransactionType

fun WalletResponseDto.toDomain(): WalletAccount =
    WalletAccount(
        balanceMinor = balanceMinor,
        availableBalanceMinor = availableBalanceMinor,
        currencyCode = currencyCode,
    )

fun ApiPageResponse<WalletTransactionResponseDto>.toDomain(): PagedResult<WalletTransaction> =
    PagedResult(
        items = content.map(WalletTransactionResponseDto::toDomain),
        page = page,
        size = size,
        totalElements = totalElements,
        totalPages = totalPages,
        isFirst = isFirst,
        isLast = isLast,
    )

private fun WalletTransactionResponseDto.toDomain(): WalletTransaction =
    WalletTransaction(
        id = transactionId,
        direction = WalletTransactionDirection.valueOf(direction),
        type = WalletTransactionType.valueOf(type),
        amountMinor = amountMinor,
        currencyCode = currencyCode,
        referenceType = referenceType,
        referenceId = referenceId,
        referenceTitle = referenceTitle,
        occurredAt = occurredAt,
    )
