package com.ahmetkaragunlu.guidemate.screens.common.moneyaction.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.components.EditTextField
import com.ahmetkaragunlu.guidemate.components.toLocalCurrency
import com.ahmetkaragunlu.guidemate.screens.common.moneyaction.model.MoneyActionMethodUi
import compose.icons.TablerIcons
import compose.icons.tablericons.CreditCard
import compose.icons.tablericons.Refresh
import java.util.Currency
import java.util.Locale

@Composable
fun MoneyActionBottomSheetContent(
    modifier: Modifier = Modifier,
    title: String,
    amountText: String,
    onAmountChange: (String) -> Unit,
    actionButtonText: String,
    helperText: String,
    selectedMethod: MoneyActionMethodUi?,
    presetAmounts: List<Int>,
    onPresetAmountClick: (Int) -> Unit,
    onChangeMethodClick: () -> Unit,
    onConfirm: (Double) -> Unit,
    extraContent: @Composable (() -> Unit)? = null,
) {
    val currencySymbol =
        try {
            Currency.getInstance(Locale.getDefault()).symbol
        } catch (_: Exception) {
            "₺"
        }

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
            currencySymbol = currencySymbol,
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
            onChangeMethodClick = onChangeMethodClick,
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))

        MoneyActionHelperText(helperText = helperText)

        Spacer(modifier = Modifier.height(24.dp))

        MoneyActionConfirmButton(
            amountText = amountText,
            actionButtonText = actionButtonText,
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
private fun MoneyAmountField(
    amountText: String,
    currencySymbol: String,
    onAmountChange: (String) -> Unit,
) {
    EditTextField(
        value = amountText,
        onValueChange = { newValue ->
            if (newValue.isEmpty() || newValue.all(Char::isDigit)) {
                onAmountChange(newValue)
            }
        },
        placeholder = R.string.zero,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
        colors =
            OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorResource(R.color.brand_color),
                unfocusedBorderColor = Color.LightGray,
            ),
        trailingIcon = {
            Text(
                text = currencySymbol,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(end = 16.dp),
                color = Color.DarkGray,
            )
        },
    )
}

@Composable
private fun PresetAmountsSection(
    presetAmounts: List<Int>,
    amountText: String,
    onPresetAmountClick: (Int) -> Unit,
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
    ) {
        presetAmounts.forEach { amount ->
            val formattedAmount = amount.toDouble().toLocalCurrency()
            FilterChip(
                selected = amountText == amount.toString(),
                onClick = { onPresetAmountClick(amount) },
                label = { Text(text = formattedAmount) },
                colors =
                    FilterChipDefaults.filterChipColors(
                        selectedContainerColor = colorResource(R.color.brand_color).copy(alpha = 0.12f),
                        selectedLabelColor = colorResource(R.color.brand_color),
                        labelColor = colorResource(R.color.text_color),
                    ),
            )
        }
    }
}

@Composable
private fun SelectedMethodCard(
    selectedMethod: MoneyActionMethodUi?,
    onChangeMethodClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(dimensionResource(R.dimen.radius_medium)))
                .background(Color(0xFFF0F4F8))
                .padding(dimensionResource(R.dimen.spacing_medium)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                TablerIcons.CreditCard,
                contentDescription = null,
                tint = colorResource(R.color.brand_color),
                modifier = Modifier.size(24.dp),
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = selectedMethod?.title ?: stringResource(R.string.bank_not_selected),
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = selectedMethod?.subtitle ?: stringResource(R.string.default_masked_iban),
                    color = colorResource(R.color.text_color),
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }

        Row(
            modifier =
                Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .clickable(onClick = onChangeMethodClick)
                    .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                TablerIcons.Refresh,
                contentDescription = null,
                tint = colorResource(R.color.brand_color),
                modifier = Modifier.size(dimensionResource(R.dimen.spacing_medium)),
            )
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_tiny)))
            Text(
                text = stringResource(R.string.change),
                color = colorResource(R.color.brand_color),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
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
    onConfirm: (Double) -> Unit,
) {
    Button(
        onClick = { onConfirm(amountText.toDoubleOrNull() ?: 0.0) },
        modifier =
            Modifier
                .fillMaxWidth()
                .height(56.dp),
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_large)),
        colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.brand_color)),
    ) {
        Text(
            text = actionButtonText,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}
