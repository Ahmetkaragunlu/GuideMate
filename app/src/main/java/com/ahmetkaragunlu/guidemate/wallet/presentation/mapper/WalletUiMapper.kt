package com.ahmetkaragunlu.guidemate.wallet.presentation.mapper

import com.ahmetkaragunlu.guidemate.wallet.domain.model.WalletTransaction
import com.ahmetkaragunlu.guidemate.wallet.domain.model.WalletTransactionDirection
import com.ahmetkaragunlu.guidemate.wallet.domain.model.WalletTransactionType
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.model.WalletTransactionStatus
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.model.WalletTransactionType as GuideTransactionType
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.model.WalletTransactionUiModel as GuideTransactionUiModel
import com.ahmetkaragunlu.guidemate.wallet.presentation.tourist.model.TouristWalletTransactionStatus
import com.ahmetkaragunlu.guidemate.wallet.presentation.tourist.model.TouristWalletTransactionType
import com.ahmetkaragunlu.guidemate.wallet.presentation.tourist.model.TouristWalletTransactionUiModel

fun WalletTransaction.toTouristUiModel(): TouristWalletTransactionUiModel? {
    val touristType =
        when (type) {
            WalletTransactionType.TOP_UP -> TouristWalletTransactionType.TOP_UP
            WalletTransactionType.TOUR_PURCHASE -> TouristWalletTransactionType.TOUR_PURCHASE
            WalletTransactionType.REFUND -> TouristWalletTransactionType.REFUND
            else -> return null
        }
    return TouristWalletTransactionUiModel(
        transactionId = id,
        referenceTitle = referenceTitle,
        amountMinor = signedAmountMinor,
        currencyCode = currencyCode,
        type = touristType,
        status =
            if (touristType == TouristWalletTransactionType.REFUND) {
                TouristWalletTransactionStatus.REFUNDED
            } else {
                TouristWalletTransactionStatus.COMPLETED
            },
        createdAt = occurredAt,
    )
}

fun WalletTransaction.toGuideUiModel(): GuideTransactionUiModel? {
    val guideType =
        when (type) {
            WalletTransactionType.GUIDE_EARNING -> GuideTransactionType.TOUR_INCOME
            WalletTransactionType.EARNING_REVERSAL -> GuideTransactionType.EARNING_REVERSAL
            WalletTransactionType.WITHDRAWAL -> GuideTransactionType.WITHDRAWAL
            else -> return null
        }
    return GuideTransactionUiModel(
        id = id,
        occurredAt = occurredAt,
        amountMinor = amountMinor,
        currencyCode = currencyCode,
        type = guideType,
        referenceTitle = referenceTitle,
        status = WalletTransactionStatus.COMPLETED,
    )
}

private val WalletTransaction.signedAmountMinor: Long
    get() =
        when (direction) {
            WalletTransactionDirection.CREDIT -> amountMinor
            WalletTransactionDirection.DEBIT -> -amountMinor
        }
