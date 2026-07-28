package com.ahmetkaragunlu.guidemate.screens.tourist.wallet

import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.common.moneyaction.content.MoneyActionBottomSheetContent
import com.ahmetkaragunlu.guidemate.screens.tourist.wallet.model.TouristWalletUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddMoneyBottomSheet(
    sheetState: SheetState,
    uiState: TouristWalletUiState,
    onAmountChange: (String) -> Unit,
    onPresetAmountClick: (Int) -> Unit,
    onChangeCardClick: () -> Unit,
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
            actionButtonText = stringResource(R.string.continue_to_secure_payment),
            helperText = stringResource(R.string.add_money_info_text),
            selectedMethod = uiState.selectedMethod,
            presetAmounts = listOf(100, 250, 500, 1000),
            onPresetAmountClick = onPresetAmountClick,
            onChangeMethodClick = onChangeCardClick,
            onConfirm = onConfirm,
        )
    }
}
