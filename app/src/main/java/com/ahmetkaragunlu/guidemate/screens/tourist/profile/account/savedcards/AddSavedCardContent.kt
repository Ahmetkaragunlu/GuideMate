package com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.savedcards

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.components.EditButton
import com.ahmetkaragunlu.guidemate.components.EditTextField
import com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.savedcards.components.PaymentCardAssociationLabel
import com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.savedcards.model.AddSavedCardFormErrors
import com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.savedcards.model.AddSavedCardFormState
import com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.savedcards.sandbox.SandboxCardMetadata

@Composable
fun AddSavedCardContent(
    formState: AddSavedCardFormState,
    formErrors: AddSavedCardFormErrors,
    detectedCard: SandboxCardMetadata?,
    onCardNumberChange: (String) -> Unit,
    onCardHolderNameChange: (String) -> Unit,
    onExpiryMonthChange: (String) -> Unit,
    onExpiryYearChange: (String) -> Unit,
    onCvvChange: (String) -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(R.dimen.spacing_medium)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .widthIn(max = 380.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = dimensionResource(R.dimen.spacing_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
        ) {
            SecureCardHeader()

            CardFormField(
                label = stringResource(R.string.card_number),
                value = formState.cardNumber,
                onValueChange = onCardNumberChange,
                placeholderResId = R.string.card_number_placeholder,
                keyboardType = KeyboardType.Number,
                errorResId = formErrors.cardNumberErrorResId,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.CreditCard,
                        contentDescription = null,
                        tint = colorResource(R.color.brand_color),
                    )
                },
            )

            detectedCard?.let { DetectedCardSummary(metadata = it) }

            CardFormField(
                label = stringResource(R.string.card_holder_name),
                value = formState.cardHolderName,
                onValueChange = onCardHolderNameChange,
                placeholderResId = R.string.card_holder_name_placeholder,
                keyboardType = KeyboardType.Text,
                errorResId = formErrors.cardHolderErrorResId,
            )

            Text(
                text = stringResource(R.string.expiry_date),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
            ) {
                EditTextField(
                    value = formState.expiryMonth,
                    onValueChange = onExpiryMonthChange,
                    placeholder = R.string.expiry_month_placeholder,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = formErrors.expiryMonthErrorResId != null,
                    supportingText = formErrors.expiryMonthErrorResId,
                    modifier = Modifier.weight(1f),
                )
                EditTextField(
                    value = formState.expiryYear,
                    onValueChange = onExpiryYearChange,
                    placeholder = R.string.expiry_year_placeholder,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = formErrors.expiryYearErrorResId != null,
                    supportingText = formErrors.expiryYearErrorResId,
                    modifier = Modifier.weight(1f),
                )
            }

            CardFormField(
                label = stringResource(R.string.cvv),
                value = formState.cvv,
                onValueChange = onCvvChange,
                placeholderResId = R.string.cvv_placeholder,
                keyboardType = KeyboardType.NumberPassword,
                errorResId = formErrors.cvvErrorResId,
                visualTransformation = PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = colorResource(R.color.brand_color),
                    )
                },
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))

            EditButton(
                text = R.string.add_card_action,
                onClick = onConfirm,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
        }

        Text(
            text = stringResource(R.string.app_version),
            style = MaterialTheme.typography.bodySmall,
            color = colorResource(R.color.text_color),
            textAlign = TextAlign.Center,
            modifier =
                Modifier
                    .widthIn(max = 380.dp)
                    .fillMaxWidth()
                    .padding(
                        top = dimensionResource(R.dimen.spacing_small),
                        bottom = dimensionResource(R.dimen.spacing_large),
                    ),
        )
    }
}

@Composable
private fun SecureCardHeader() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
    ) {
        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = null,
            tint = colorResource(R.color.brand_color),
            modifier =
                Modifier
                    .size(48.dp)
                    .background(
                        color = colorResource(R.color.brand_color).copy(alpha = 0.10f),
                        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
                    )
                    .padding(dimensionResource(R.dimen.spacing_small)),
        )
        Text(
            text = stringResource(R.string.secure_card_form_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Text(
            text = stringResource(R.string.secure_card_form_description),
            style = MaterialTheme.typography.bodyMedium,
            color = colorResource(R.color.text_color),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun DetectedCardSummary(
    metadata: SandboxCardMetadata,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(
                    color = colorResource(R.color.brand_color).copy(alpha = 0.08f),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
                )
                .padding(dimensionResource(R.dimen.spacing_medium)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_tiny))) {
            Text(
                text = metadata.bankName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
            )
            metadata.cardFamily?.let { cardFamily ->
                Text(
                    text = cardFamily,
                    style = MaterialTheme.typography.bodySmall,
                    color = colorResource(R.color.text_color),
                )
            }
        }
        PaymentCardAssociationLabel(
            association = metadata.cardAssociation,
        )
    }
}

@Composable
private fun CardFormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes placeholderResId: Int,
    keyboardType: KeyboardType,
    @StringRes errorResId: Int?,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_tiny)),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium,
        )
        EditTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholderResId,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            isError = errorResId != null,
            supportingText = errorResId,
            visualTransformation = visualTransformation,
            leadingIcon = leadingIcon,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
