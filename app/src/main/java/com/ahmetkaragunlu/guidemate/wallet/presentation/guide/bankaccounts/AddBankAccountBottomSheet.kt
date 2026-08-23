package com.ahmetkaragunlu.guidemate.wallet.presentation.guide.bankaccounts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.components.EditButton
import com.ahmetkaragunlu.guidemate.common.ui.components.EditTextField
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.bankaccounts.model.AddBankAccountFormState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddBankAccountBottomSheet(
    sheetState: SheetState,
    formState: AddBankAccountFormState,
    onAccountHolderNameChange: (String) -> Unit,
    onIbanChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    isSubmitting: Boolean,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = dimensionResource(R.dimen.spacing_medium))
                    .padding(bottom = dimensionResource(R.dimen.spacing_large)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
        ) {
            Text(
                text = stringResource(R.string.add_bank_account),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
            BankAccountFormField(
                label = stringResource(R.string.account_holder_name),
                value = formState.accountHolderName,
                onValueChange = onAccountHolderNameChange,
                placeholderResId = R.string.card_holder_name_placeholder,
            )
            BankAccountFormField(
                label = stringResource(R.string.iban),
                value = formState.ibanBody,
                onValueChange = onIbanChange,
                placeholderResId = R.string.iban_placeholder,
                isError = formState.isIbanComplete && !formState.isIbanValid,
                supportingTextResId =
                    when {
                        formState.isIbanComplete && !formState.isIbanValid ->
                            R.string.iban_error_message
                        else -> null
                    },
                keyboardOptions =
                    KeyboardOptions.Default.copy(
                        capitalization = KeyboardCapitalization.Characters,
                        keyboardType = KeyboardType.Ascii,
                    ),
                leadingIcon = {
                    Text(
                        text = "TR",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                    )
                },
                visualTransformation = TurkishIbanBodyVisualTransformation,
            )
            BankAccountFormField(
                label = stringResource(R.string.bank_name),
                value = formState.bankName.orEmpty(),
                onValueChange = {},
                placeholderResId = R.string.bank_name_placeholder,
                readOnly = true,
            )
            Text(
                text = stringResource(R.string.bank_account_security_notice),
                style = MaterialTheme.typography.bodySmall,
                color = colorResource(R.color.text_color),
            )
            EditButton(
                text = R.string.add_bank_account_action,
                onClick = onConfirm,
                enabled = formState.canSubmit,
                isLoading = isSubmitting,
            )
        }
    }
}

@Composable
private fun BankAccountFormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholderResId: Int,
    isError: Boolean = false,
    supportingTextResId: Int? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    readOnly: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_tiny))) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
        )
        EditTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholderResId,
            keyboardOptions = keyboardOptions,
            isError = isError,
            supportingText = supportingTextResId,
            readOnly = readOnly,
            leadingIcon = leadingIcon,
            visualTransformation = visualTransformation,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

private object TurkishIbanBodyVisualTransformation : VisualTransformation {
    private val separatorOffsets = intArrayOf(2, 6, 10, 14, 18, 22)

    override fun filter(text: AnnotatedString): TransformedText {
        val transformedText =
            buildString {
                text.forEachIndexed { index, character ->
                    if (index in separatorOffsets) append(' ')
                    append(character)
                }
            }
        val transformedSeparatorOffsets =
            separatorOffsets
                .filter { originalOffset -> originalOffset < text.length }
                .mapIndexed { index, originalOffset -> originalOffset + index }

        val offsetMapping =
            object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    val addedSeparators =
                        separatorOffsets.count { separatorOffset ->
                            separatorOffset < offset ||
                                (separatorOffset == offset && offset < text.length)
                        }
                    return (offset + addedSeparators).coerceAtMost(transformedText.length)
                }

                override fun transformedToOriginal(offset: Int): Int {
                    val precedingSeparators =
                        transformedSeparatorOffsets.count { separatorOffset ->
                            separatorOffset < offset
                        }
                    return (offset - precedingSeparators).coerceIn(0, text.length)
                }
            }

        return TransformedText(
            text = AnnotatedString(transformedText),
            offsetMapping = offsetMapping,
        )
    }
}
