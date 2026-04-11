package com.ahmetkaragunlu.guidemate.screens.common.profile.account.savedcards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.components.EditButton
import com.ahmetkaragunlu.guidemate.components.EditTextField
import com.ahmetkaragunlu.guidemate.screens.common.profile.account.savedcards.model.AddSavedCardFormState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSavedCardBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    formState: AddSavedCardFormState,
    onCardNumberChange: (String) -> Unit,
    onCardHolderNameChange: (String) -> Unit,
    onExpiryMonthChange: (String) -> Unit,
    onExpiryYearChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },

    ) {
        Column(
            modifier =
                modifier
                    .fillMaxWidth().verticalScroll(rememberScrollState())
                    .padding(dimensionResource(R.dimen.spacing_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
        ) {
            Text(
                text = stringResource(R.string.add_new_card),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            SavedCardFormField(
                label = stringResource(R.string.card_number),
                value = formState.cardNumber,
                onValueChange = onCardNumberChange,
                placeholder = R.string.card_number_placeholder,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                isError = formState.cardNumber.isNotEmpty() && !formState.isCardNumberValid,
                supportingText =
                    if (formState.cardNumber.isNotEmpty() && !formState.isCardNumberValid) {
                        R.string.card_number_error_message
                    } else {
                        null
                    },
            )

            SavedCardFormField(
                label = stringResource(R.string.card_holder_name),
                value = formState.cardHolderName,
                onValueChange = onCardHolderNameChange,
                placeholder = R.string.card_holder_name_placeholder,
                keyboardOptions = KeyboardOptions.Default,
                isError = formState.cardHolderName.isNotEmpty() && !formState.isCardHolderNameValid,
                supportingText =
                    if (formState.cardHolderName.isNotEmpty() && !formState.isCardHolderNameValid) {
                        R.string.card_holder_name_error_message
                    } else {
                        null
                    },
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = stringResource(R.string.expiry_date),
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(start = 4.dp),
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    EditTextField(
                        value = formState.expiryMonth,
                        onValueChange = onExpiryMonthChange,
                        placeholder = R.string.expiry_month_placeholder,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        isError = formState.expiryMonth.isNotEmpty() && !formState.isExpiryMonthValid,
                        supportingText =
                            if (formState.expiryMonth.isNotEmpty() && !formState.isExpiryMonthValid) {
                                R.string.expiry_month_error_message
                            } else {
                                null
                            },
                        modifier = Modifier.weight(1f),
                    )

                    EditTextField(
                        value = formState.expiryYear,
                        onValueChange = onExpiryYearChange,
                        placeholder = R.string.expiry_year_placeholder,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        isError = formState.expiryYear.isNotEmpty() && !formState.isExpiryYearValid,
                        supportingText =
                            if (formState.expiryYear.isNotEmpty() && !formState.isExpiryYearValid) {
                                R.string.expiry_year_error_message
                            } else {
                                null
                            },
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            EditButton(
                text = R.string.add_card_action,
                onClick = onConfirm,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                )
        }
    }
}

@Composable
private fun SavedCardFormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: Int,
    keyboardOptions: KeyboardOptions,
    isError: Boolean,
    supportingText: Int?,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(start = 4.dp),
        )

        EditTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder,
            keyboardOptions = keyboardOptions,
            isError = isError,
            supportingText = supportingText,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
