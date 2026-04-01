package com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.savedcards.components

import androidx.compose.foundation.border
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
import androidx.compose.material3.OutlinedButton
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
import com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.savedcards.model.SavedCardUi
import compose.icons.TablerIcons
import compose.icons.tablericons.Trash

@Composable
fun SavedCardItem(
    card: SavedCardUi,
    onDeleteClick: () -> Unit,
    onMakeDefaultClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardModifier =
        if (card.isDefault) {
            modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = colorResource(id = R.color.brand_color),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.radius_large)),
                )
        } else {
            modifier.fillMaxWidth()
        }

    Card(
        modifier = cardModifier,
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_large)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.spacing_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
                ) {
                    if (card.isDefault) {
                        DefaultBadge()
                    }

                    Text(
                        text = card.bankName,
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
                text = card.cardNumber,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                SavedCardInfoItem(
                    label = stringResource(R.string.card_holder),
                    value = card.cardHolderName,
                    modifier = Modifier.weight(1f),
                )

                SavedCardInfoItem(
                    label = stringResource(R.string.expiry_date),
                    value = card.expiryDate,
                    modifier = Modifier.weight(0.6f),
                )
            }

            if (!card.isDefault) {

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
