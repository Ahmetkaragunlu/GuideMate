package com.ahmetkaragunlu.guidemate.screens.guide.wallet.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.ahmetkaragunlu.guidemate.screens.common.toLocalCurrency
import com.ahmetkaragunlu.guidemate.screens.guide.wallet.model.BankAccount
import compose.icons.TablerIcons
import compose.icons.tablericons.CreditCard
import compose.icons.tablericons.Refresh
import java.util.Currency
import java.util.Locale

@Composable
fun WithdrawBottomSheetContent(
    availableBalance: Double,
    selectedBank: BankAccount?,
    onConfirm: (Double) -> Unit
) {
    var amountText by remember { mutableStateOf("") }

    val currencySymbol = remember {
        try {
            Currency.getInstance(Locale.getDefault()).symbol
        } catch (e: Exception) {
            "₺"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.spacing_medium))
            .padding(bottom = dimensionResource(R.dimen.spacing_medium)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.withdraw_title),
            color = colorResource(R.color.brand_color),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))

        EditTextField(
            value = amountText,
            onValueChange = { newValue ->
                if (newValue.isEmpty() || newValue.all { it.isDigit() }) amountText = newValue
            },
            placeholder = R.string.zero,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorResource(R.color.brand_color),
                unfocusedBorderColor = Color.LightGray
            ),
            trailingIcon = {
                Text(
                    text = currencySymbol,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(end = 16.dp),
                    color = Color.DarkGray
                )
            })

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.available_balance_format, availableBalance.toLocalCurrency()),
                color = colorResource(R.color.text_color),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = stringResource(R.string.withdraw_all),
                color = colorResource(R.color.brand_color),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .clickable { amountText = availableBalance.toInt().toString() }
                    .padding(4.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(dimensionResource(R.dimen.radius_medium)))
                .background(Color(0xFFF0F4F8))
                .padding(dimensionResource(R.dimen.spacing_medium)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    TablerIcons.CreditCard,
                    contentDescription = null,
                    tint = colorResource(R.color.brand_color),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = selectedBank?.bankName ?: stringResource(R.string.bank_not_selected),
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = selectedBank?.maskedIban ?: stringResource(R.string.default_masked_iban),
                        color = colorResource(R.color.text_color),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Row(modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .clickable {  }
                .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    TablerIcons.Refresh,
                    contentDescription = null,
                    tint = colorResource(R.color.brand_color),
                    modifier = Modifier.size(dimensionResource(R.dimen.spacing_medium))
                )
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_tiny)))
                Text(
                    text = stringResource(R.string.change),
                    color = colorResource(R.color.brand_color),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.radius_medium)))

        Text(
            text = stringResource(R.string.withdraw_info_text),
            color = colorResource(R.color.text_color),
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onConfirm(amountText.toDoubleOrNull() ?: 0.0) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(dimensionResource(R.dimen.radius_large)),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.brand_color))
        ) {
            Text(
                text = stringResource(R.string.confirm),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}