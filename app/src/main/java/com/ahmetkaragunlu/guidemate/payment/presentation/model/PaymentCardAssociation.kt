package com.ahmetkaragunlu.guidemate.payment.presentation.model

enum class PaymentCardAssociation(
    val displayName: String,
) {
    VISA(displayName = "Visa"),
    MASTER_CARD(displayName = "Mastercard"),
    TROY(displayName = "TROY"),
    AMERICAN_EXPRESS(displayName = "American Express"),
}
