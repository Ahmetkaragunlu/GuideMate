package com.ahmetkaragunlu.guidemate.screens.guide.profile.account.bankaccounts.model

data class AddBankAccountFormState(
    val bankName: String? = null,
    val accountHolderName: String = "",
    val ibanBody: String = "",
    val isIbanValid: Boolean = false,
) {
    val isAccountHolderNameValid: Boolean
        get() = accountHolderName.isNotBlank()

    val isIbanComplete: Boolean
        get() = ibanBody.length == IBAN_BODY_LENGTH

    val canSubmit: Boolean
        get() =
            isAccountHolderNameValid &&
                isIbanComplete &&
                isIbanValid &&
                !bankName.isNullOrBlank()

    private companion object {
        const val IBAN_BODY_LENGTH = 24
    }
}
