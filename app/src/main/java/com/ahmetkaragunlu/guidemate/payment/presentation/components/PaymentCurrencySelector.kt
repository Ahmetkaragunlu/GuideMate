package com.ahmetkaragunlu.guidemate.payment.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.payment.domain.model.CheckoutCurrency

@Composable
fun PaymentCurrencySelector(
    currencies: List<CheckoutCurrency>,
    selectedCurrencyCode: String?,
    onCurrencySelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    androidx.compose.foundation.layout.Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
    ) {
        Text(
            text = stringResource(R.string.payment_charge_currency),
            style = MaterialTheme.typography.titleSmall,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
        ) {
            currencies.forEach { currency ->
                FilterChip(
                    selected = currency.currencyCode == selectedCurrencyCode,
                    onClick = { onCurrencySelected(currency.currencyCode) },
                    label = { Text(currency.currencyCode) },
                    colors =
                        FilterChipDefaults.filterChipColors(
                            selectedContainerColor =
                                colorResource(R.color.brand_color).copy(alpha = 0.12f),
                            selectedLabelColor = colorResource(R.color.brand_color),
                        ),
                )
            }
        }
    }
}

