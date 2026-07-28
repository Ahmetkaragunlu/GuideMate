package com.ahmetkaragunlu.guidemate.navigation.guide.finance

import kotlinx.serialization.Serializable

object GuideFinanceDestination {
    @Serializable data object Earnings

    @Serializable data object Wallet

    @Serializable data object WalletTransactions
}
