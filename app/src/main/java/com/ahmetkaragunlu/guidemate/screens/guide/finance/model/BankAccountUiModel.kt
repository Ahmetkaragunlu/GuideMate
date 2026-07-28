package com.ahmetkaragunlu.guidemate.screens.guide.finance.model

import com.ahmetkaragunlu.guidemate.screens.common.moneyaction.model.MoneyActionMethodType
import com.ahmetkaragunlu.guidemate.screens.common.moneyaction.model.MoneyActionMethodUi

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
