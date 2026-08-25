package com.ahmetkaragunlu.guidemate.wallet.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.formatting.toCurrencyMinorUnitsOrNull
import com.ahmetkaragunlu.guidemate.wallet.presentation.components.model.MoneyActionMethodType
import com.ahmetkaragunlu.guidemate.wallet.presentation.components.model.MoneyActionMethodUi

@Composable
fun MoneyActionBottomSheetContent(
    modifier: Modifier = Modifier,
    title: String,
    amountText: String,
    onAmountChange: (String) -> Unit,
    actionButtonText: String,
    helperText: String,
    selectedMethod: MoneyActionMethodUi?,
    methodType: MoneyActionMethodType = MoneyActionMethodType.CARD,
    presetAmounts: List<Int>,
    onPresetAmountClick: (Int) -> Unit,
    onChangeMethodClick: () -> Unit,
    onConfirm: (Long) -> Unit,
    showChangeMethodAction: Boolean = true,
    isActionInProgress: Boolean = false,
    extraContent: @Composable (() -> Unit)? = null,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = dimensionResource(R.dimen.spacing_medium))
                .padding(bottom = dimensionResource(R.dimen.spacing_medium)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MoneyActionHeader(title = title)
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))
        MoneyAmountField(
            amountText = amountText,
            onAmountChange = onAmountChange,
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))
        if (extraContent != null) {
            extraContent()
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))
        }
        if (presetAmounts.isNotEmpty()) {
            PresetAmountsSection(
                presetAmounts = presetAmounts,
                amountText = amountText,
                onPresetAmountClick = onPresetAmountClick,
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))
        }
        SelectedMethodCard(
            selectedMethod = selectedMethod,
            methodType = methodType,
            onChangeMethodClick = onChangeMethodClick,
            showChangeMethodAction = showChangeMethodAction,
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))
        MoneyActionHelperText(helperText = helperText)
        Spacer(modifier = Modifier.height(24.dp))
        MoneyActionConfirmButton(
            amountText = amountText,
            actionButtonText = actionButtonText,
            enabled = selectedMethod != null && amountText.toCurrencyMinorUnitsOrNull()?.let { it > 0 } == true,
            isLoading = isActionInProgress,
            onConfirm = onConfirm,
        )
    }
}

@Composable
private fun MoneyActionHeader(title: String) {
    Text(
        text = title,
        color = colorResource(R.color.brand_color),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
private fun MoneyActionHelperText(helperText: String) {
    Text(
        text = helperText,
        color = colorResource(R.color.text_color),
        style = MaterialTheme.typography.bodySmall,
    )
}

@Composable
private fun MoneyActionConfirmButton(
    amountText: String,
    actionButtonText: String,
    enabled: Boolean,
    isLoading: Boolean,
    onConfirm: (Long) -> Unit,
) {
    Button(
        onClick = { amountText.toCurrencyMinorUnitsOrNull()?.let(onConfirm) },
        enabled = enabled && !isLoading,
        modifier = Modifier.fillMaxWidth().height(56.dp),
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_large)),
        colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.brand_color)),
    ) {
        if (isLoading) {
            androidx.compose.material3.CircularProgressIndicator(
                modifier = Modifier.size(18.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp,
            )
        } else {
            Text(
                text = actionButtonText,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}
