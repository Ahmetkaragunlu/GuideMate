package com.ahmetkaragunlu.guidemate.screens.tourist.payment

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.screens.tourist.payment.model.PaymentAttemptStatus
import com.ahmetkaragunlu.guidemate.screens.tourist.payment.model.PaymentPurpose
import kotlinx.coroutines.delay

private const val MOCK_PAYMENT_STEP_DELAY_MILLIS = 5_000L

@Composable
fun PaymentStatusScreen(
    onPaymentSucceeded: (String) -> Unit,
    onExitPayment: () -> Unit,
    onPaymentUnavailable: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PaymentStatusViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val attempt = uiState.attempt
    val shouldBlockBackNavigation =
        when (attempt?.status) {
            PaymentAttemptStatus.REDIRECTING,
            PaymentAttemptStatus.VERIFYING,
            PaymentAttemptStatus.SUCCEEDED,
            -> true
            else -> false
        }

    BackHandler(enabled = shouldBlockBackNavigation) { }
    LaunchedEffect(attempt?.status) {
        when (attempt?.status) {
            PaymentAttemptStatus.REDIRECTING -> {
                delay(MOCK_PAYMENT_STEP_DELAY_MILLIS)
                viewModel.markProviderRedirectCompleted()
            }
            PaymentAttemptStatus.VERIFYING -> {
                delay(MOCK_PAYMENT_STEP_DELAY_MILLIS)
                viewModel.markMockVerificationCompleted()
            }
            PaymentAttemptStatus.SUCCEEDED -> {
                onPaymentSucceeded(attempt.paymentAttemptId)
            }
            else -> Unit
        }
    }

    PaymentStatusContent(
        attempt = attempt,
        onPrimaryAction = {
            when (attempt?.status) {
                PaymentAttemptStatus.FAILED,
                PaymentAttemptStatus.TIMEOUT,
                -> viewModel.retryPayment()
                PaymentAttemptStatus.CANCELLED,
                -> onExitPayment()
                PaymentAttemptStatus.SUCCEEDED ->
                    onPaymentSucceeded(attempt.paymentAttemptId)
                null -> onPaymentUnavailable()
                else -> Unit
            }
        },
        onSecondaryAction = {
            when (attempt?.status) {
                PaymentAttemptStatus.REDIRECTING -> viewModel.cancelPayment()
                PaymentAttemptStatus.FAILED,
                PaymentAttemptStatus.TIMEOUT,
                -> onExitPayment()
                else -> Unit
            }
        },
        modifier = modifier,
    )
}

@Composable
fun PaymentSuccessScreen(
    onFinished: (PaymentPurpose) -> Unit,
    onPaymentUnavailable: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PaymentStatusViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val attempt = uiState.attempt

    BackHandler(enabled = attempt != null) { }

    PaymentStatusContent(
        attempt = attempt,
        onPrimaryAction = {
            if (attempt == null) {
                onPaymentUnavailable()
            } else {
                onFinished(attempt.purpose)
            }
        },
        onSecondaryAction = { },
        modifier = modifier,
    )
}
