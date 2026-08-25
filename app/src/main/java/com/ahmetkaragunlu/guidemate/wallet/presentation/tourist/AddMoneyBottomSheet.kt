package com.ahmetkaragunlu.guidemate.wallet.presentation.tourist

import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.wallet.presentation.components.MoneyActionBottomSheetContent
import com.ahmetkaragunlu.guidemate.wallet.presentation.tourist.model.TouristWalletUiState
import com.ahmetkaragunlu.guidemate.payment.presentation.components.PaymentCurrencySelector
import com.ahmetkaragunlu.guidemate.payment.presentation.components.PaymentQuoteSummary
import com.ahmetkaragunlu.guidemate.wallet.presentation.components.model.MoneyActionMethodType
import com.ahmetkaragunlu.guidemate.wallet.presentation.components.model.MoneyActionMethodUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddMoneyBottomSheet(
    sheetState: SheetState,
    uiState: TouristWalletUiState,
    onAmountChange: (String) -> Unit,
    onPresetAmountClick: (Int) -> Unit,
    onChargeCurrencySelected: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        MoneyActionBottomSheetContent(
            title = stringResource(R.string.add_money_title),
            amountText = uiState.topUpAmount,
            onAmountChange = onAmountChange,
            actionButtonText =
                stringResource(
                    if (uiState.topUpQuote == null) {
                        R.string.payment_get_quote
                    } else {
                        R.string.continue_to_secure_payment
                    },
                ),
            helperText = stringResource(R.string.add_money_info_text),
            selectedMethod =
                MoneyActionMethodUi(
                    id = "hosted-card",
                    title = stringResource(R.string.hosted_card_payment),
                    subtitle = stringResource(R.string.hosted_card_selection_notice),
                    type = MoneyActionMethodType.CARD,
                ),
            presetAmounts = listOf(100, 250, 500, 1000),
            onPresetAmountClick = onPresetAmountClick,
            onChangeMethodClick = { },
            onConfirm = onConfirm,
            showChangeMethodAction = false,
            isActionInProgress = uiState.isPaymentActionInProgress,
            extraContent = {
                PaymentCurrencySelector(
                    currencies = uiState.chargeCurrencies,
                    selectedCurrencyCode = uiState.selectedChargeCurrencyCode,
                    onCurrencySelected = onChargeCurrencySelected,
                )
                uiState.topUpQuote?.let { quote -> PaymentQuoteSummary(quote = quote) }
                uiState.paymentActionError?.let { errorMessage ->
                    androidx.compose.material3.Text(
                        text = errorMessage,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                    )
                }
            },
        )
    }
}
