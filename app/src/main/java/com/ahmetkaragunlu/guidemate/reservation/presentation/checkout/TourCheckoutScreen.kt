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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.components.AgreementBottomSheet
import com.ahmetkaragunlu.guidemate.common.ui.components.GuideMateContentState
import com.ahmetkaragunlu.guidemate.payment.presentation.status.PaymentVerificationContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TourCheckoutScreen(
    onNavigateToPayment: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TourCheckoutViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var hasCompletedInitialResume by remember { mutableStateOf(false) }
    val termsSheetState = rememberModalBottomSheetState()

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

    if (uiState.isWalletPaymentVerifying) {
        PaymentVerificationContent(modifier = modifier)
    } else {
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
                onTermsClick = viewModel::onTermsCheckboxClicked,
                onContinue = viewModel::continueCheckout,
            )
        }
    }

    if (uiState.showTermsSheet) {
        AgreementBottomSheet(
            sheetState = termsSheetState,
            titleResId = R.string.reservation_agreement_title,
            bodyResId = R.string.reservation_agreement_full_text,
            hasUserReadAgreement = uiState.hasUserReadTerms,
            onDismiss = viewModel::dismissTermsSheet,
            onMarkAgreementAsRead = viewModel::markTermsAsRead,
            onAcceptAgreement = viewModel::acceptTerms,
        )
    }
}
