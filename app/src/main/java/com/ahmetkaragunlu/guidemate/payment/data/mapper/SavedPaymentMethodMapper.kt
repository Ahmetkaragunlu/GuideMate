package com.ahmetkaragunlu.guidemate.payment.data.mapper

import com.ahmetkaragunlu.guidemate.payment.data.remote.model.response.SavedPaymentMethodResponse
import com.ahmetkaragunlu.guidemate.payment.domain.model.SavedPaymentMethod

fun SavedPaymentMethodResponse.toDomain(): SavedPaymentMethod =
    SavedPaymentMethod(
        id = savedPaymentMethodId,
        alias = alias,
        bankName = bankName,
        bankCode = bankCode,
        cardFamily = cardFamily,
        cardAssociation = cardAssociation,
        cardType = cardType,
        lastFourDigits = lastFourDigits,
        cardHolderName = cardHolderName,
        expiryMonth = expiryMonth,
        expiryYear = expiryYear,
    )
