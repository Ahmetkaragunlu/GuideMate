package com.ahmetkaragunlu.guidemate.payment.presentation.savedpaymentmethod.sandbox

import com.ahmetkaragunlu.guidemate.payment.presentation.model.PaymentCardAssociation
import com.ahmetkaragunlu.guidemate.payment.presentation.model.PaymentCardType

data class SandboxCardMetadata(
    val cardNumber: String,
    val bankName: String,
    val bankCode: Int? = null,
    val cardFamily: String? = null,
    val cardAssociation: PaymentCardAssociation,
    val cardType: PaymentCardType,
) {
    val binNumber: String
        get() = cardNumber.take(BIN_LENGTH)

    private companion object {
        const val BIN_LENGTH = 8
    }
}

// Temporary MVP catalog. The backend integration will replace this with iyzico metadata.
object SandboxCardCatalog {
    const val SAMPLE_CARD_NUMBER = "5526 0800 0000 0006"

    private val cards =
        listOf(
            SandboxCardMetadata(
                cardNumber = "5526080000000006",
                bankName = "Akbank",
                bankCode = 46,
                cardFamily = "Axess",
                cardAssociation = PaymentCardAssociation.MASTER_CARD,
                cardType = PaymentCardType.CREDIT_CARD,
            ),
            SandboxCardMetadata(
                cardNumber = "5400360000000003",
                bankName = "Garanti BBVA",
                bankCode = 62,
                cardFamily = "Bonus",
                cardAssociation = PaymentCardAssociation.MASTER_CARD,
                cardType = PaymentCardType.CREDIT_CARD,
            ),
            SandboxCardMetadata(
                cardNumber = "4543590000000006",
                bankName = "Türkiye İş Bankası",
                bankCode = 64,
                cardFamily = "Maximum",
                cardAssociation = PaymentCardAssociation.VISA,
                cardType = PaymentCardType.CREDIT_CARD,
            ),
            SandboxCardMetadata(
                cardNumber = "4910050000000006",
                bankName = "VakıfBank",
                bankCode = 15,
                cardFamily = "World",
                cardAssociation = PaymentCardAssociation.VISA,
                cardType = PaymentCardType.CREDIT_CARD,
            ),
            SandboxCardMetadata(
                cardNumber = "9792020000000001",
                bankName = "QNB",
                cardFamily = "CardFinans",
                cardAssociation = PaymentCardAssociation.TROY,
                cardType = PaymentCardType.CREDIT_CARD,
            ),
        )

    fun findByBin(cardNumber: String): SandboxCardMetadata? {
        val digits = cardNumber.filter(Char::isDigit)
        if (digits.length < BIN_LENGTH) return null
        return cards.firstOrNull { it.binNumber == digits.take(BIN_LENGTH) }
    }

    fun findByCardNumber(cardNumber: String): SandboxCardMetadata? {
        val digits = cardNumber.filter(Char::isDigit)
        return cards.firstOrNull { it.cardNumber == digits }
    }

    private const val BIN_LENGTH = 8
}
