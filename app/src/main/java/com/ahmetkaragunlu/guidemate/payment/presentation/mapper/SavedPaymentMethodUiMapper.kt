package com.ahmetkaragunlu.guidemate.payment.presentation.mapper

import com.ahmetkaragunlu.guidemate.payment.domain.model.SavedPaymentMethod
import com.ahmetkaragunlu.guidemate.payment.presentation.model.PaymentCardAssociation
import com.ahmetkaragunlu.guidemate.payment.presentation.model.PaymentCardType
import com.ahmetkaragunlu.guidemate.payment.presentation.model.SavedPaymentCardUiModel

fun SavedPaymentMethod.toUiModel(): SavedPaymentCardUiModel =
    SavedPaymentCardUiModel(
        cardId = id,
        bankName = bankName ?: alias.orEmpty(),
        bankCode = bankCode,
        cardFamily = cardFamily,
        cardAssociation = cardAssociation.toCardAssociation(),
        cardType = cardType.toCardType(),
        lastFourDigits = lastFourDigits,
        cardHolderName = cardHolderName,
        expiryMonth = expiryMonth,
        expiryYear = expiryYear,
        isDefault = isDefault,
    )

private fun String?.toCardAssociation(): PaymentCardAssociation? =
    when (this?.uppercase()) {
        "VISA" -> PaymentCardAssociation.VISA
        "MASTER_CARD", "MASTERCARD" -> PaymentCardAssociation.MASTER_CARD
        "TROY" -> PaymentCardAssociation.TROY
        "AMERICAN_EXPRESS", "AMEX" -> PaymentCardAssociation.AMERICAN_EXPRESS
        else -> null
    }

private fun String?.toCardType(): PaymentCardType? =
    PaymentCardType.entries.firstOrNull { it.name == this?.uppercase() }
