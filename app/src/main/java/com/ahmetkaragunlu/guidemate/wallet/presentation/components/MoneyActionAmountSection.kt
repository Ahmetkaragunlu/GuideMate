package com.ahmetkaragunlu.guidemate.wallet.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.KeyboardType
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.components.EditTextField
import com.ahmetkaragunlu.guidemate.common.ui.formatting.isValidCurrencyInput
import com.ahmetkaragunlu.guidemate.common.ui.formatting.platformCurrencySymbol
import com.ahmetkaragunlu.guidemate.common.ui.formatting.toPlatformCurrencyFromMinorUnit

@Composable
internal fun MoneyAmountField(
    amountText: String,
    onAmountChange: (String) -> Unit,
) {
    EditTextField(
        value = amountText,
        onValueChange = { newValue ->
            if (newValue.isValidCurrencyInput()) onAmountChange(newValue)
        },
        placeholder = R.string.zero,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
        colors =
            OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorResource(R.color.brand_color),
                unfocusedBorderColor = Color.LightGray,
            ),
        trailingIcon = {
            Text(
                text = platformCurrencySymbol(),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(end = dimensionResource(R.dimen.spacing_medium)),
                color = Color.DarkGray,
            )
        },
    )
}

@Composable
internal fun PresetAmountsSection(
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
            val formattedAmount = (amount.toLong() * 100).toPlatformCurrencyFromMinorUnit()
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
