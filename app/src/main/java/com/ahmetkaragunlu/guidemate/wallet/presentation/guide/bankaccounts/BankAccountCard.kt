package com.ahmetkaragunlu.guidemate.wallet.presentation.guide.bankaccounts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.wallet.data.mock.guide.model.BankAccountUiModel
import compose.icons.TablerIcons
import compose.icons.tablericons.Trash

@Composable
internal fun BankAccountCard(
    account: BankAccountUiModel,
    onDeleteClick: () -> Unit,
    onMakeDefaultClick: () -> Unit,
) {
    val cardModifier =
        if (account.isDefault) {
            Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = colorResource(R.color.brand_color),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.radius_large)),
                )
        } else {
            Modifier.fillMaxWidth()
        }

    Card(
        modifier = cardModifier,
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_large)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.spacing_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(
                    verticalArrangement =
                        Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
                ) {
                    if (account.isDefault) {
                        DefaultBankAccountBadge()
                    }

                    Text(
                        text = account.bankName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = TablerIcons.Trash,
                        contentDescription = null,
                        tint = Color.Gray,
                    )
                }
            }

            Text(
                text = account.maskedIban,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )

            BankAccountInfoItem(
                label = stringResource(R.string.account_holder_name),
                value = account.accountHolderName,
            )

            if (!account.isDefault) {
                OutlinedButton(
                    onClick = onMakeDefaultClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Text(
                        text = stringResource(R.string.set_as_default),
                        style = MaterialTheme.typography.labelLarge,
                        color = colorResource(R.color.text_color),
                    )
                }
            }
        }
    }
}

@Composable
private fun DefaultBankAccountBadge() {
    Text(
        text = stringResource(R.string.default_bank_account),
        modifier =
            Modifier
                .clip(RoundedCornerShape(dimensionResource(R.dimen.radius_medium)))
                .background(colorResource(R.color.brand_color).copy(alpha = 0.12f))
                .padding(horizontal = 10.dp, vertical = 6.dp),
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        color = colorResource(R.color.brand_color),
    )
}

@Composable
private fun BankAccountInfoItem(
    label: String,
    value: String,
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = colorResource(R.color.text_color),
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_tiny)))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
        )
    }
}
