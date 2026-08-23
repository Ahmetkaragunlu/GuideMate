package com.ahmetkaragunlu.guidemate.wallet.presentation.guide

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.formatting.toPlatformCurrencyFromMinorUnit
import com.ahmetkaragunlu.guidemate.wallet.presentation.components.content.MoneyActionBottomSheetContent
import com.ahmetkaragunlu.guidemate.wallet.presentation.components.model.MoneyActionMethodType
import com.ahmetkaragunlu.guidemate.wallet.presentation.components.model.MoneyActionMethodUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WithdrawBottomSheet(
    sheetState: SheetState,
    amount: String,
    selectedMethod: MoneyActionMethodUi?,
    availableBalanceMinor: Long,
    onAmountChange: (String) -> Unit,
    onChangeMethodClick: () -> Unit,
    onWithdrawAllClick: () -> Unit,
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit,
    isActionInProgress: Boolean,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        MoneyActionBottomSheetContent(
            title = stringResource(R.string.withdraw_title),
            amountText = amount,
            onAmountChange = onAmountChange,
            actionButtonText = stringResource(R.string.confirm),
            helperText = stringResource(R.string.withdraw_info_text),
            selectedMethod = selectedMethod,
            methodType = MoneyActionMethodType.BANK_ACCOUNT,
            presetAmounts = listOf(100, 250, 500, 1000),
            onPresetAmountClick = { onAmountChange(it.toString()) },
            onChangeMethodClick = onChangeMethodClick,
            onConfirm = onConfirm,
            isActionInProgress = isActionInProgress,
            extraContent = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(R.dimen.spacing_tiny)),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text =
                            stringResource(
                                R.string.available_balance_format,
                                availableBalanceMinor.toPlatformCurrencyFromMinorUnit(),
                            ),
                        color = colorResource(R.color.text_color),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Text(
                        text = stringResource(R.string.withdraw_all),
                        color = colorResource(R.color.brand_color),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        modifier =
                            Modifier
                                .clickable(onClick = onWithdrawAllClick)
                                .padding(start = dimensionResource(R.dimen.spacing_medium)),
                    )
                }
            },
        )
    }
}
