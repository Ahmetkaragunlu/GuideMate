package com.ahmetkaragunlu.guidemate.navigation.guide.wallet

import kotlinx.serialization.Serializable

object GuideWalletDestination {
    @Serializable data object Earnings

    @Serializable data object Wallet

    @Serializable data object WalletTransactions
}
