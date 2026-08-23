package com.ahmetkaragunlu.guidemate.payment.presentation.status

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.common.ui.components.GuideMateContentState
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentPurpose
import com.ahmetkaragunlu.guidemate.payment.presentation.status.model.PaymentUiStatus

@Composable
fun PaymentStatusScreen(
    onHostedCheckoutRequired: (String) -> Unit,
    onPaymentSucceeded: (String) -> Unit,
    onExitPayment: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PaymentStatusViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val payment = uiState.payment

    BackHandler(enabled = payment?.status == PaymentUiStatus.VERIFYING) { }

    LaunchedEffect(uiState.shouldOpenHostedCheckout, payment?.paymentId) {
        if (uiState.shouldOpenHostedCheckout && payment != null) {
            onHostedCheckoutRequired(payment.paymentId)
        }
    }
    LaunchedEffect(payment?.status) {
        if (payment?.status == PaymentUiStatus.SUCCEEDED) {
            onPaymentSucceeded(payment.paymentId)
        }
    }

    GuideMateContentState(
        state =
            when {
                uiState.isLoading -> ContentLoadState.LOADING
                uiState.errorMessage != null -> ContentLoadState.ERROR
                else -> ContentLoadState.CONTENT
            },
        onRetry = viewModel::refresh,
        modifier = modifier,
        errorMessage = uiState.errorMessage,
    ) {
        PaymentStatusContent(
            payment = payment,
            statusMessage = uiState.statusMessage,
            onPrimaryAction = onExitPayment,
            onSecondaryAction = onExitPayment,
            modifier = modifier,
        )
    }
}

@Composable
fun PaymentSuccessScreen(
    onFinished: (PaymentPurpose) -> Unit,
    onPaymentUnavailable: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PaymentStatusViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val payment = uiState.payment

    BackHandler(enabled = payment != null) { }

    GuideMateContentState(
        state =
            when {
                uiState.isLoading -> ContentLoadState.LOADING
                uiState.errorMessage != null -> ContentLoadState.ERROR
                else -> ContentLoadState.CONTENT
            },
        onRetry = viewModel::refresh,
        modifier = modifier,
        errorMessage = uiState.errorMessage,
    ) {
        PaymentStatusContent(
            payment = payment,
            statusMessage = uiState.statusMessage,
            onPrimaryAction = {
                if (payment == null) onPaymentUnavailable() else onFinished(payment.purpose)
            },
            onSecondaryAction = { },
            modifier = modifier,
        )
    }
}
