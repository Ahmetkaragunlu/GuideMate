package com.ahmetkaragunlu.guidemate.screens.tourist.booking.checkout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun TourCheckoutScreen(
    onNavigateToSavedCards: () -> Unit,
    onNavigateToPayment: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TourCheckoutViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TourCheckoutContent(
        uiState = uiState,
        onDecreaseParticipant = viewModel::decreaseParticipantCount,
        onIncreaseParticipant = viewModel::increaseParticipantCount,
        onPaymentMethodSelected = viewModel::onPaymentMethodSelected,
        onCardSelected = viewModel::onCardSelected,
        onManageCardsClick = onNavigateToSavedCards,
        onTermsAcceptedChange = viewModel::onTermsAcceptedChange,
        onContinue = {
            viewModel.createPaymentAttempt()?.let(onNavigateToPayment)
        },
        modifier = modifier,
    )
}
