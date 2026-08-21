package com.ahmetkaragunlu.guidemate.payment.presentation.savedpaymentmethod

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.ahmetkaragunlu.guidemate.payment.presentation.savedpaymentmethod.model.AddSavedCardFormErrors
import com.ahmetkaragunlu.guidemate.payment.presentation.savedpaymentmethod.model.AddSavedCardFormState
import com.ahmetkaragunlu.guidemate.payment.presentation.savedpaymentmethod.sandbox.SandboxCardCatalog
import com.ahmetkaragunlu.guidemate.payment.presentation.savedpaymentmethod.sandbox.formatCardNumber
import com.ahmetkaragunlu.guidemate.payment.presentation.savedpaymentmethod.sandbox.sanitizeCardHolderName
import com.ahmetkaragunlu.guidemate.payment.presentation.savedpaymentmethod.sandbox.sanitizeCvv
import com.ahmetkaragunlu.guidemate.payment.presentation.savedpaymentmethod.sandbox.sanitizeExpiryPart
import com.ahmetkaragunlu.guidemate.payment.presentation.savedpaymentmethod.sandbox.validateSandboxCardForm
import com.ahmetkaragunlu.guidemate.payment.presentation.savedpaymentmethod.viewmodel.AddSavedCardViewModel

@Composable
fun AddSavedCardScreen(
    onCardAdded: () -> Unit,
    viewModel: AddSavedCardViewModel = hiltViewModel(),
) {
    var formState by remember { mutableStateOf(AddSavedCardFormState()) }
    var formErrors by remember { mutableStateOf(AddSavedCardFormErrors()) }
    val detectedCard = SandboxCardCatalog.findByBin(formState.cardNumber)

    AddSavedCardContent(
        formState = formState,
        formErrors = formErrors,
        detectedCard = detectedCard,
        onCardNumberChange = { value ->
            formState = formState.copy(cardNumber = formatCardNumber(value))
            formErrors = formErrors.copy(cardNumberErrorResId = null)
        },
        onCardHolderNameChange = { value ->
            formState = formState.copy(cardHolderName = sanitizeCardHolderName(value))
            formErrors = formErrors.copy(cardHolderErrorResId = null)
        },
        onExpiryMonthChange = { value ->
            formState = formState.copy(expiryMonth = sanitizeExpiryPart(value))
            formErrors = formErrors.copy(expiryMonthErrorResId = null)
        },
        onExpiryYearChange = { value ->
            formState = formState.copy(expiryYear = sanitizeExpiryPart(value))
            formErrors = formErrors.copy(expiryYearErrorResId = null)
        },
        onCvvChange = { value ->
            formState = formState.copy(cvv = sanitizeCvv(value))
            formErrors = formErrors.copy(cvvErrorResId = null)
        },
        onConfirm = {
            val validationErrors = validateSandboxCardForm(formState)
            formErrors = validationErrors
            if (!validationErrors.hasError) {
                SandboxCardCatalog.findByCardNumber(formState.cardNumber)?.let { cardMetadata ->
                    viewModel.addSandboxCard(
                        metadata = cardMetadata,
                        cardHolderName = formState.cardHolderName,
                        expiryMonth = formState.expiryMonth,
                        expiryYear = formState.expiryYear,
                    )
                    onCardAdded()
                }
            }
        },
    )
}
