package com.ahmetkaragunlu.guidemate.wallet.data.mock.guide.model

import com.ahmetkaragunlu.guidemate.wallet.presentation.components.model.MoneyActionMethodType
import com.ahmetkaragunlu.guidemate.wallet.presentation.components.model.MoneyActionMethodUi

data class BankAccountUiModel(
    val bankAccountId: String,
    val bankName: String,
    val accountHolderName: String,
    val maskedIban: String,
    val isDefault: Boolean,
)

fun BankAccountUiModel.toMoneyActionMethodUi(): MoneyActionMethodUi =
    MoneyActionMethodUi(
        id = bankAccountId,
        title = bankName,
        subtitle = maskedIban,
        type = MoneyActionMethodType.BANK_ACCOUNT,
    )
