package com.ahmetkaragunlu.guidemate.wallet.presentation.mapper

import com.ahmetkaragunlu.guidemate.payment.presentation.model.SavedPaymentCardUiModel
import com.ahmetkaragunlu.guidemate.wallet.presentation.components.model.MoneyActionMethodType
import com.ahmetkaragunlu.guidemate.wallet.presentation.components.model.MoneyActionMethodUi
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.bankaccounts.model.BankAccountUiModel

fun SavedPaymentCardUiModel.toMoneyActionMethodUi(): MoneyActionMethodUi =
    MoneyActionMethodUi(
        id = cardId,
        title = displayName,
        subtitle = maskedCardNumber,
        type = MoneyActionMethodType.CARD,
    )

fun BankAccountUiModel.toMoneyActionMethodUi(): MoneyActionMethodUi =
    MoneyActionMethodUi(
        id = bankAccountId,
        title = bankName,
        subtitle = maskedIban,
        type = MoneyActionMethodType.BANK_ACCOUNT,
    )
