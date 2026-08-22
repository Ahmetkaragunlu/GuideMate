package com.ahmetkaragunlu.guidemate.wallet.presentation.tourist.transactions.model

import androidx.annotation.StringRes
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.wallet.presentation.tourist.model.TouristWalletTransactionType

enum class TouristWalletTransactionFilter(
    @param:StringRes val titleResId: Int,
    val transactionType: TouristWalletTransactionType?,
) {
    ALL(
        titleResId = R.string.wallet_filter_all,
        transactionType = null,
    ),
    TOP_UP(
        titleResId = R.string.wallet_filter_top_up,
        transactionType = TouristWalletTransactionType.TOP_UP,
    ),
    TOUR_PURCHASE(
        titleResId = R.string.wallet_filter_tour_payments,
        transactionType = TouristWalletTransactionType.TOUR_PURCHASE,
    ),
    REFUND(
        titleResId = R.string.wallet_filter_refunds,
        transactionType = TouristWalletTransactionType.REFUND,
    ),
}
