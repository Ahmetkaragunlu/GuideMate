package com.ahmetkaragunlu.guidemate.payment.presentation.savedpaymentmethod.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.payment.presentation.model.SavedPaymentCardUiModel
import compose.icons.TablerIcons
import compose.icons.tablericons.Trash

@Composable
fun SavedCardItem(
    card: SavedPaymentCardUiModel,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
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
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
                ) {
                    Text(
                        text = card.bankName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    card.cardFamily?.let { cardFamily ->
                        Text(
                            text = cardFamily,
                            style = MaterialTheme.typography.bodySmall,
                            color = colorResource(R.color.text_color),
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    card.cardAssociation?.let { association ->
                        PaymentCardAssociationLabel(association = association)
                    }
                    IconButton(onClick = onDeleteClick) {
                        Icon(
                            imageVector = TablerIcons.Trash,
                            contentDescription = null,
                            tint = Color.Gray,
                        )
                    }
                }
            }

            Text(
                text = card.maskedCardNumber,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )

            if (card.cardHolderName != null || card.expiryDate != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    card.cardHolderName?.let { cardHolderName ->
                        SavedCardInfoItem(
                            label = stringResource(R.string.card_holder),
                            value = cardHolderName,
                            modifier = Modifier.weight(1f),
                        )
                    }

                    card.expiryDate?.let { expiryDate ->
                        SavedCardInfoItem(
                            label = stringResource(R.string.expiry_date),
                            value = expiryDate,
                            modifier = Modifier.weight(0.6f),
                        )
                    }
                }
            }
        }
    }
}
