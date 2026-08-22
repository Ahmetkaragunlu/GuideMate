package com.ahmetkaragunlu.guidemate.wallet.presentation.guide

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.components.EditAlertDialog
import com.ahmetkaragunlu.guidemate.common.ui.components.GuideMateContentState
import com.ahmetkaragunlu.guidemate.common.ui.formatting.toCurrencyInput
import com.ahmetkaragunlu.guidemate.common.ui.formatting.toPlatformCurrencyFromMinorUnit
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.earnings.model.MonthlyEarningUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuideMyWalletScreen(
    earnings: List<MonthlyEarningUiModel>,
    onNavigateToEarnings: () -> Unit,
    onNavigateToTransactions: () -> Unit,
    viewModel: GuideMyWalletViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    var withdrawAmount by rememberSaveable { mutableStateOf("") }
    var amountAwaitingConfirmation by rememberSaveable { mutableStateOf<Long?>(null) }
    var showInsufficientBalanceDialog by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    GuideMateContentState(
        state = uiState.loadState,
        onRetry = viewModel::refresh,
    ) {}
    if (uiState.loadState != ContentLoadState.CONTENT) {
        return
    }

    LaunchedEffect(showBottomSheet) {
        if (showBottomSheet) {
            viewModel.resetSelectedBankAccountToDefault()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GuideMyWalletContent(
            uiState = uiState,
            earnings = earnings,
            onWithdrawClick = { showBottomSheet = true },
            onNavigateToEarnings = onNavigateToEarnings,
            onNavigateToTransactions = onNavigateToTransactions,
        )

        if (showBottomSheet) {
            WithdrawBottomSheet(
                sheetState = sheetState,
                amount = withdrawAmount,
                selectedMethod = uiState.selectedMethod,
                availableBalanceMinor = uiState.availableBalanceMinor,
                onAmountChange = { withdrawAmount = it },
                onChangeMethodClick = viewModel::selectNextBankAccount,
                onWithdrawAllClick = {
                    withdrawAmount = uiState.availableBalanceMinor.toCurrencyInput()
                },
                onDismiss = { showBottomSheet = false },
                onConfirm = { amountMinor ->
                    if (amountMinor > uiState.availableBalanceMinor) {
                        showInsufficientBalanceDialog = true
                    } else {
                        amountAwaitingConfirmation = amountMinor
                    }
                },
            )
        }

        if (showInsufficientBalanceDialog) {
            EditAlertDialog(
                title = R.string.insufficient_balance_title,
                text = R.string.withdrawal_insufficient_balance_description,
                textFormatArguments =
                    listOf(uiState.availableBalanceMinor.toPlatformCurrencyFromMinorUnit()),
                confirmButton = {
                    TextButton(onClick = { showInsufficientBalanceDialog = false }) {
                        Text(
                            text = stringResource(R.string.ok),
                            color = colorResource(R.color.brand_color),
                        )
                    }
                },
                onDismissRequest = { showInsufficientBalanceDialog = false },
            )
        }

        amountAwaitingConfirmation?.let { amountMinor ->
            EditAlertDialog(
                title = R.string.withdrawal_confirmation_title,
                text = R.string.withdrawal_confirmation_description,
                textFormatArguments = listOf(amountMinor.toPlatformCurrencyFromMinorUnit()),
                textModifier = Modifier.offset(y = (-4).dp),
                compactText = true,
                confirmButton = {
                    TextButton(
                        onClick = {
                            amountAwaitingConfirmation = null
                            if (viewModel.requestWithdrawal(amountMinor)) {
                                withdrawAmount = ""
                                showBottomSheet = false
                            }
                        },
                    ) {
                        Text(
                            text = stringResource(R.string.yes),
                            color = colorResource(R.color.brand_color),
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { amountAwaitingConfirmation = null }) {
                        Text(
                            text = stringResource(R.string.no),
                            color = colorResource(R.color.text_color),
                        )
                    }
                },
                onDismissRequest = { amountAwaitingConfirmation = null },
            )
        }

        if (uiState.isWithdrawalRequestSubmitted) {
            EditAlertDialog(
                title = R.string.withdrawal_request_received_title,
                text = R.string.withdrawal_request_received_description,
                textModifier = Modifier.offset(y = (-4).dp),
                compactText = true,
                confirmButton = {
                    TextButton(onClick = viewModel::dismissWithdrawalConfirmation) {
                        Text(
                            text = stringResource(R.string.ok),
                            color = colorResource(R.color.brand_color),
                        )
                    }
                },
                onDismissRequest = viewModel::dismissWithdrawalConfirmation,
            )
        }
    }
}
