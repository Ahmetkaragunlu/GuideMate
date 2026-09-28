package com.ahmetkaragunlu.guidemate.wallet.data.mapper

import com.ahmetkaragunlu.guidemate.common.network.model.response.ApiPageResponse
import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.wallet.data.remote.model.response.WalletResponse
import com.ahmetkaragunlu.guidemate.wallet.data.remote.model.response.WalletTransactionResponse
import com.ahmetkaragunlu.guidemate.wallet.domain.model.WalletAccount
import com.ahmetkaragunlu.guidemate.wallet.domain.model.WalletTransaction
import com.ahmetkaragunlu.guidemate.wallet.domain.model.WalletTransactionDirection
import com.ahmetkaragunlu.guidemate.wallet.domain.model.WalletTransactionType

fun WalletResponse.toDomain(): WalletAccount =
    WalletAccount(
        balanceMinor = balanceMinor,
        availableBalanceMinor = availableBalanceMinor,
        currencyCode = currencyCode,
    )

fun ApiPageResponse<WalletTransactionResponse>.toDomain(): PagedResult<WalletTransaction> =
    PagedResult(
        items = content.map(WalletTransactionResponse::toDomain),
        page = page,
        size = size,
        totalElements = totalElements,
        totalPages = totalPages,
        isFirst = isFirst,
        isLast = isLast,
    )

private fun WalletTransactionResponse.toDomain(): WalletTransaction =
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
