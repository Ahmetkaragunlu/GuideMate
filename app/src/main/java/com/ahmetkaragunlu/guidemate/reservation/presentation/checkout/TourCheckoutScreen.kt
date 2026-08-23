package com.ahmetkaragunlu.guidemate.reservation.presentation.checkout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.ahmetkaragunlu.guidemate.common.ui.components.GuideMateContentState

@Composable
fun TourCheckoutScreen(
    onNavigateToPayment: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TourCheckoutViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var hasCompletedInitialResume by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.paymentLaunch) {
        uiState.paymentLaunch?.let { launch ->
            viewModel.onPaymentNavigationHandled()
            onNavigateToPayment(launch.paymentId, launch.requiresHostedCheckout)
        }
    }

    LifecycleResumeEffect(Unit) {
        if (hasCompletedInitialResume) {
            viewModel.refreshTour()
        } else {
            hasCompletedInitialResume = true
        }
        onPauseOrDispose { }
    }

    GuideMateContentState(
        state = uiState.loadState,
        onRetry = viewModel::refreshTour,
        modifier = modifier,
    ) {
        TourCheckoutContent(
            uiState = uiState,
            onDecreaseParticipant = viewModel::decreaseParticipantCount,
            onIncreaseParticipant = viewModel::increaseParticipantCount,
            onPaymentMethodSelected = viewModel::onPaymentMethodSelected,
            onChargeCurrencySelected = viewModel::onChargeCurrencySelected,
            onTermsAcceptedChange = viewModel::onTermsAcceptedChange,
            onContinue = viewModel::continueCheckout,
        )
    }
}
