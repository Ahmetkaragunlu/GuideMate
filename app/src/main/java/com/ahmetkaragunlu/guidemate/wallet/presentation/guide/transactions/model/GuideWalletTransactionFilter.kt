package com.ahmetkaragunlu.guidemate.wallet.presentation.guide.transactions.model

import androidx.annotation.StringRes
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.model.WalletTransactionType

enum class GuideWalletTransactionFilter(
    @param:StringRes val titleResId: Int,
    val transactionType: WalletTransactionType?,
) {
    ALL(
        titleResId = R.string.wallet_filter_all,
        transactionType = null,
    ),
    TOUR_INCOME(
        titleResId = R.string.wallet_filter_tour_income,
        transactionType = WalletTransactionType.TOUR_INCOME,
    ),
    WITHDRAWAL(
        titleResId = R.string.wallet_filter_withdrawals,
        transactionType = WalletTransactionType.WITHDRAWAL,
    ),
}
