package com.ahmetkaragunlu.guidemate.navigation.tourist.payment

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ahmetkaragunlu.guidemate.navigation.RootDestination
import com.ahmetkaragunlu.guidemate.navigation.navigateBottomBar
import com.ahmetkaragunlu.guidemate.navigation.navigateTo
import com.ahmetkaragunlu.guidemate.navigation.tourist.TouristDestination
import com.ahmetkaragunlu.guidemate.navigation.tourist.account.TouristAccountStart
import com.ahmetkaragunlu.guidemate.screens.tourist.booking.checkout.TourCheckoutScreen
import com.ahmetkaragunlu.guidemate.screens.tourist.payment.PaymentStatusScreen
import com.ahmetkaragunlu.guidemate.screens.tourist.payment.PaymentSuccessScreen
import com.ahmetkaragunlu.guidemate.screens.tourist.payment.model.PaymentPurpose
import com.ahmetkaragunlu.guidemate.screens.tourist.wallet.TouristWalletScreen
import com.ahmetkaragunlu.guidemate.screens.tourist.wallet.transactions.TouristWalletTransactionsScreen

internal fun NavGraphBuilder.touristPaymentNavGraph(
    touristNavController: NavController,
    routeNavController: NavController,
) {
    val returnToTouristHome: () -> Unit = {
        touristNavController.navigateBottomBar(
            destination = TouristDestination.Home,
            startDestination = TouristDestination.Home,
        )
    }
    val onPaymentFinished: (PaymentPurpose) -> Unit = { purpose ->
        when (purpose) {
            PaymentPurpose.WALLET_TOP_UP ->
                touristNavController.popBackStack(
                    route = TouristPaymentDestination.Wallet,
                    inclusive = false,
                )
            PaymentPurpose.TOUR_BOOKING -> {
                touristNavController.popBackStack(
                    route = TouristDestination.Home,
                    inclusive = false,
                )
                touristNavController.navigateBottomBar(
                    destination = TouristDestination.Trips,
                    startDestination = TouristDestination.Home,
                )
            }
        }
    }

    composable<TouristPaymentDestination.Wallet> {
        TouristWalletScreen(
            onNavigateToSavedCards = {
                routeNavController.navigateTo(
                    RootDestination.TouristAccount(TouristAccountStart.SAVED_CARDS),
                )
            },
            onNavigateToTransactions = {
                touristNavController.navigateTo(TouristPaymentDestination.WalletTransactions)
            },
            onNavigateToPayment = { paymentAttemptId ->
                touristNavController.navigateTo(
                    TouristPaymentDestination.Status(paymentAttemptId),
                )
            },
        )
    }
    composable<TouristPaymentDestination.WalletTransactions> {
        TouristWalletTransactionsScreen()
    }
    composable<TouristPaymentDestination.Checkout> {
        TourCheckoutScreen(
            onNavigateToSavedCards = {
                routeNavController.navigateTo(
                    RootDestination.TouristAccount(TouristAccountStart.SAVED_CARDS),
                )
            },
            onNavigateToPayment = { paymentAttemptId ->
                touristNavController.navigateTo(
                    TouristPaymentDestination.Status(paymentAttemptId),
                )
            },
        )
    }
    composable<TouristPaymentDestination.Status> {
        PaymentStatusScreen(
            onPaymentSucceeded = { paymentAttemptId ->
                touristNavController.navigate(
                    TouristPaymentDestination.Success(paymentAttemptId),
                ) {
                    popUpTo<TouristPaymentDestination.Status> {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            onExitPayment = touristNavController::navigateUp,
            onPaymentUnavailable = returnToTouristHome,
        )
    }
    composable<TouristPaymentDestination.Success> {
        PaymentSuccessScreen(
            onFinished = onPaymentFinished,
            onPaymentUnavailable = returnToTouristHome,
        )
    }
}
