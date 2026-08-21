package com.ahmetkaragunlu.guidemate.navigation.guide.wallet

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ahmetkaragunlu.guidemate.navigation.navigateTo
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.earnings.GuideEarningsScreen
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.earnings.GuideEarningsViewModel
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.GuideMyWalletScreen
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.transactions.GuideWalletTransactionsScreen

internal fun NavGraphBuilder.guideWalletNavGraph(
    navController: NavController,
    earningsViewModel: GuideEarningsViewModel,
) {
    composable<GuideWalletDestination.Earnings> {
        val earningsUiState = earningsViewModel.uiState.collectAsStateWithLifecycle()
        GuideEarningsScreen(uiState = earningsUiState.value)
    }
    composable<GuideWalletDestination.Wallet> {
        val earningsUiState = earningsViewModel.uiState.collectAsStateWithLifecycle()
        GuideMyWalletScreen(
            earnings = earningsUiState.value.allEarnings,
            onNavigateToEarnings = {
                navController.navigateTo(GuideWalletDestination.Earnings)
            },
            onNavigateToTransactions = {
                navController.navigateTo(GuideWalletDestination.WalletTransactions)
            },
        )
    }
    composable<GuideWalletDestination.WalletTransactions> {
        GuideWalletTransactionsScreen()
    }
}
