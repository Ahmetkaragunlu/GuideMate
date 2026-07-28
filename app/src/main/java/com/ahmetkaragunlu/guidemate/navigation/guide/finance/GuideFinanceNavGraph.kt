package com.ahmetkaragunlu.guidemate.navigation.guide.finance

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ahmetkaragunlu.guidemate.navigation.navigateTo
import com.ahmetkaragunlu.guidemate.screens.guide.earnings.GuideEarningsScreen
import com.ahmetkaragunlu.guidemate.screens.guide.earnings.viewmodel.GuideEarningsViewModel
import com.ahmetkaragunlu.guidemate.screens.guide.wallet.GuideMyWalletScreen
import com.ahmetkaragunlu.guidemate.screens.guide.wallet.transactions.GuideWalletTransactionsScreen

internal fun NavGraphBuilder.guideFinanceNavGraph(
    navController: NavController,
    earningsViewModel: GuideEarningsViewModel,
) {
    composable<GuideFinanceDestination.Earnings> {
        val earningsUiState = earningsViewModel.uiState.collectAsStateWithLifecycle()
        GuideEarningsScreen(uiState = earningsUiState.value)
    }
    composable<GuideFinanceDestination.Wallet> {
        val earningsUiState = earningsViewModel.uiState.collectAsStateWithLifecycle()
        GuideMyWalletScreen(
            earnings = earningsUiState.value.allEarnings,
            onNavigateToEarnings = {
                navController.navigateTo(GuideFinanceDestination.Earnings)
            },
            onNavigateToTransactions = {
                navController.navigateTo(GuideFinanceDestination.WalletTransactions)
            },
        )
    }
    composable<GuideFinanceDestination.WalletTransactions> {
        GuideWalletTransactionsScreen()
    }
}
