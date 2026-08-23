package com.ahmetkaragunlu.guidemate.reservation.presentation.checkout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import com.ahmetkaragunlu.guidemate.common.ui.components.EditButton
import com.ahmetkaragunlu.guidemate.common.ui.formatting.toPlatformCurrencyFromMinorUnit
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentMethod
import com.ahmetkaragunlu.guidemate.payment.presentation.components.PaymentCurrencySelector
import com.ahmetkaragunlu.guidemate.payment.presentation.components.PaymentQuoteSummary
import com.ahmetkaragunlu.guidemate.reservation.presentation.model.TourCheckoutUiState

@Composable
internal fun TourCheckoutContent(
    uiState: TourCheckoutUiState,
    onDecreaseParticipant: () -> Unit,
    onIncreaseParticipant: () -> Unit,
    onPaymentMethodSelected: (PaymentMethod) -> Unit,
    onChargeCurrencySelected: (String) -> Unit,
    onTermsAcceptedChange: (Boolean) -> Unit,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(R.dimen.spacing_medium))
                .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
    ) {
        Column(
            modifier =
                Modifier
                    .widthIn(max = 380.dp)
                    .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_tiny)))
            CheckoutIntro()
            TourSummaryCard(
                uiState = uiState,
                onDecreaseParticipant = onDecreaseParticipant,
                onIncreaseParticipant = onIncreaseParticipant,
            )
            CheckoutPaymentMethodSection(
                uiState = uiState,
                onPaymentMethodSelected = onPaymentMethodSelected,
            )
            if (uiState.selectedMethod == PaymentMethod.HOSTED_CARD) {
                PaymentCurrencySelector(
                    currencies = uiState.chargeCurrencies,
                    selectedCurrencyCode = uiState.selectedChargeCurrencyCode,
                    onCurrencySelected = onChargeCurrencySelected,
                )
                uiState.quote?.let { PaymentQuoteSummary(quote = it) }
            }
            TermsRow(
                checked = uiState.termsAccepted,
                onCheckedChange = onTermsAcceptedChange,
            )
            uiState.validationErrorResId?.let { errorResId ->
                Text(
                    text = stringResource(errorResId),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
            uiState.paymentActionError?.let { errorMessage ->
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
            CheckoutTotal(totalMinor = uiState.totalMinor)
        }

        EditButton(
            text =
                if (
                    uiState.selectedMethod == PaymentMethod.HOSTED_CARD &&
                        uiState.quote == null
                ) {
                    R.string.payment_get_quote
                } else {
                    R.string.continue_to_secure_payment
                },
            onClick = onContinue,
            isLoading = uiState.isPaymentActionInProgress,
            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.spacing_extra_large)),
        )
    }
}

@Composable
private fun CheckoutIntro() {
    Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_tiny))) {
        Text(
            text = stringResource(R.string.checkout_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = stringResource(R.string.checkout_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = colorResource(R.color.text_color),
        )
    }
}

@Composable
private fun TourSummaryCard(
    uiState: TourCheckoutUiState,
    onDecreaseParticipant: () -> Unit,
    onIncreaseParticipant: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F7FC)),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.spacing_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
        ) {
            Text(
                text = uiState.tourTitle,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "${uiState.date} • ${uiState.location}",
                style = MaterialTheme.typography.bodySmall,
                color = colorResource(R.color.text_color),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.checkout_participant_count),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text =
                            stringResource(
                                R.string.checkout_remaining_capacity,
                                uiState.availableCapacity,
                            ),
                        style = MaterialTheme.typography.bodySmall,
                        color = colorResource(R.color.text_color),
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onDecreaseParticipant,
                        enabled = uiState.canDecreaseParticipants,
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = null)
                    }
                    Text(
                        text = uiState.participantCount.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    IconButton(
                        onClick = onIncreaseParticipant,
                        enabled = uiState.canIncreaseParticipants,
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(R.string.checkout_unit_price),
                    color = colorResource(R.color.text_color),
                )
                Text(
                    text = uiState.unitPriceMinor.toPlatformCurrencyFromMinorUnit(),
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.brand_color),
                )
            }
        }
    }
}

@Composable
private fun TermsRow(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable { onCheckedChange(!checked) },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors =
                CheckboxDefaults.colors(
                    checkedColor = colorResource(R.color.brand_color),
                ),
        )
        Text(
            text = stringResource(R.string.checkout_terms_acceptance),
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
private fun CheckoutTotal(totalMinor: Long) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(R.dimen.spacing_small)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.checkout_total),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = totalMinor.toPlatformCurrencyFromMinorUnit(),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.brand_color),
        )
    }
}
