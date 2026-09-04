package com.ahmetkaragunlu.guidemate.navigation.tourist.payment

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.ahmetkaragunlu.guidemate.navigation.RootDestination
import com.ahmetkaragunlu.guidemate.navigation.navigateBottomBar
import com.ahmetkaragunlu.guidemate.navigation.navigateTo
import com.ahmetkaragunlu.guidemate.navigation.tourist.TouristDestination
import com.ahmetkaragunlu.guidemate.navigation.tourist.account.TouristAccountStart
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentPurpose
import com.ahmetkaragunlu.guidemate.payment.presentation.hosted.HostedPaymentScreen
import com.ahmetkaragunlu.guidemate.payment.presentation.status.PaymentStatusScreen
import com.ahmetkaragunlu.guidemate.payment.presentation.status.PaymentSuccessScreen
import com.ahmetkaragunlu.guidemate.reservation.presentation.checkout.TourCheckoutScreen
import com.ahmetkaragunlu.guidemate.wallet.presentation.tourist.TouristWalletScreen
import com.ahmetkaragunlu.guidemate.wallet.presentation.tourist.transactions.TouristWalletTransactionsScreen

internal fun NavGraphBuilder.touristPaymentNavGraph(
    touristNavController: NavController,
    routeNavController: NavController,
    onBackActionChanged: ((() -> Unit)?) -> Unit,
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
            onNavigateToPayment = { paymentId ->
                touristNavController.navigateTo(TouristPaymentDestination.Hosted(paymentId))
            },
        )
    }
    composable<TouristPaymentDestination.WalletTransactions> {
        TouristWalletTransactionsScreen()
    }
    composable<TouristPaymentDestination.Checkout> {
        TourCheckoutScreen(
            onNavigateToPayment = { paymentId, requiresHostedCheckout ->
                touristNavController.navigateTo(
                    if (requiresHostedCheckout) {
                        TouristPaymentDestination.Hosted(paymentId)
                    } else {
                        TouristPaymentDestination.Status(
                            paymentId = paymentId,
                            openHostedIfRequired = false,
                        )
                    },
                )
            },
        )
    }
    composable<TouristPaymentDestination.Hosted> { backStackEntry ->
        val paymentId = backStackEntry.toRoute<TouristPaymentDestination.Hosted>().paymentId
        HostedPaymentScreen(
            onVerificationRequired = {
                touristNavController.navigate(
                    TouristPaymentDestination.Status(
                        paymentId = paymentId,
                        openHostedIfRequired = false,
                    ),
                ) {
                    popUpTo<TouristPaymentDestination.Hosted> { inclusive = true }
                    launchSingleTop = true
                }
            },
            onBackActionChanged = onBackActionChanged,
        )
    }
    composable<TouristPaymentDestination.Status> {
        PaymentStatusScreen(
            onHostedCheckoutRequired = { paymentId ->
                touristNavController.navigate(
                    TouristPaymentDestination.Hosted(paymentId),
                ) {
                    popUpTo<TouristPaymentDestination.Status> { inclusive = true }
                    launchSingleTop = true
                }
            },
            onPaymentSucceeded = { paymentId ->
                touristNavController.navigate(
                    TouristPaymentDestination.Success(paymentId),
                ) {
                    popUpTo<TouristPaymentDestination.Status> { inclusive = true }
                    launchSingleTop = true
                }
            },
            onExitPayment = touristNavController::navigateUp,
        )
    }
    composable<TouristPaymentDestination.Success> {
        PaymentSuccessScreen(
            onFinished = onPaymentFinished,
            onPaymentUnavailable = returnToTouristHome,
        )
    }
}
