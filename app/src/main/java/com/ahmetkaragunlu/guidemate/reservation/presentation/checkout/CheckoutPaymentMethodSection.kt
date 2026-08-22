package com.ahmetkaragunlu.guidemate.reservation.presentation.checkout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.formatting.toCurrencyFromMinorUnit
import com.ahmetkaragunlu.guidemate.reservation.presentation.model.TourCheckoutUiState
import com.ahmetkaragunlu.guidemate.payment.presentation.model.SavedPaymentCardUiModel
import com.ahmetkaragunlu.guidemate.payment.presentation.status.model.PaymentMethod

@Composable
internal fun CheckoutPaymentMethodSection(
    uiState: TourCheckoutUiState,
    onPaymentMethodSelected: (PaymentMethod) -> Unit,
    onCardSelected: (String) -> Unit,
    onManageCardsClick: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))) {
        Text(
            text = stringResource(R.string.checkout_payment_method),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
        PaymentMethodOption(
            title = stringResource(R.string.wallet_payment),
            subtitle =
                stringResource(
                    R.string.wallet_balance_format,
                    uiState.walletBalanceMinor.toCurrencyFromMinorUnit(
                        uiState.walletCurrencyCode,
                    ),
                ),
            icon = Icons.Default.Wallet,
            selected = uiState.selectedMethod == PaymentMethod.WALLET,
            onClick = { onPaymentMethodSelected(PaymentMethod.WALLET) },
        )
        PaymentMethodOption(
            title = stringResource(R.string.saved_card_payment),
            subtitle = stringResource(R.string.secure_payment_provider_short),
            icon = Icons.Default.CreditCard,
            selected = uiState.selectedMethod == PaymentMethod.SAVED_CARD,
            onClick = { onPaymentMethodSelected(PaymentMethod.SAVED_CARD) },
        )

        if (uiState.selectedMethod == PaymentMethod.SAVED_CARD) {
            if (uiState.savedCards.isEmpty()) {
                Text(
                    text = stringResource(R.string.checkout_no_card),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorResource(R.color.text_color),
                )
                Text(
                    text = stringResource(R.string.add_card),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.brand_color),
                    modifier =
                        Modifier
                            .clickable(onClick = onManageCardsClick)
                            .padding(vertical = dimensionResource(R.dimen.spacing_tiny)),
                )
            } else {
                uiState.savedCards.forEach { card ->
                    SavedCardOption(
                        card = card,
                        selected = card.cardId == uiState.selectedCardId,
                        onClick = { onCardSelected(card.cardId) },
                    )
                }
            }
        }
    }
}

@Composable
private fun PaymentMethodOption(
    title: String,
    subtitle: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
        color =
            if (selected) {
                colorResource(R.color.brand_color).copy(alpha = 0.08f)
            } else {
                MaterialTheme.colorScheme.surface
            },
        border =
            BorderStroke(
                width = 1.dp,
                color =
                    if (selected) {
                        colorResource(R.color.brand_color)
                    } else {
                        Color(0xFFE5E7EB)
                    },
            ),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.spacing_medium)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = colorResource(R.color.brand_color),
                modifier = Modifier.size(24.dp),
            )
            Column(
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(start = dimensionResource(R.dimen.spacing_small)),
            ) {
                Text(text = title, fontWeight = FontWeight.SemiBold)
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = colorResource(R.color.text_color),
                )
            }
            RadioButton(
                selected = selected,
                onClick = onClick,
                colors =
                    RadioButtonDefaults.colors(
                        selectedColor = colorResource(R.color.brand_color),
                    ),
            )
        }
    }
}

@Composable
private fun SavedCardOption(
    card: SavedPaymentCardUiModel,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(vertical = dimensionResource(R.dimen.spacing_small)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors =
                RadioButtonDefaults.colors(
                    selectedColor = colorResource(R.color.brand_color),
                ),
        )
        Column {
            Text(
                text = card.displayName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = card.maskedCardNumber,
                style = MaterialTheme.typography.bodySmall,
                color = colorResource(R.color.text_color),
            )
        }
    }
}
