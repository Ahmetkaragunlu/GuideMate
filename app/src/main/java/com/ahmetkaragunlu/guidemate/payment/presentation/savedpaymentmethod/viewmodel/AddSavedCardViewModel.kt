package com.ahmetkaragunlu.guidemate.payment.presentation.savedpaymentmethod.viewmodel

import androidx.lifecycle.ViewModel
import com.ahmetkaragunlu.guidemate.payment.presentation.model.SavedPaymentCardUiModel
import com.ahmetkaragunlu.guidemate.wallet.data.mock.tourist.TouristWalletStore
import com.ahmetkaragunlu.guidemate.payment.presentation.savedpaymentmethod.sandbox.SandboxCardMetadata
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddSavedCardViewModel
    @Inject
    constructor(
        private val walletStore: TouristWalletStore,
    ) : ViewModel() {
        fun addSandboxCard(
            metadata: SandboxCardMetadata,
            cardHolderName: String,
            expiryMonth: String,
            expiryYear: String,
        ) {
            walletStore.addCard(
                SavedPaymentCardUiModel(
                    cardId = UUID.randomUUID().toString(),
                    bankName = metadata.bankName,
                    bankCode = metadata.bankCode,
                    cardFamily = metadata.cardFamily,
                    cardAssociation = metadata.cardAssociation,
                    cardType = metadata.cardType,
                    lastFourDigits = metadata.cardNumber.takeLast(4),
                    cardHolderName = cardHolderName.trim(),
                    expiryMonth = expiryMonth.padStart(2, '0'),
                    expiryYear = "$FULL_YEAR_PREFIX$expiryYear",
                    isDefault = false,
                ),
            )
        }

        private companion object {
            const val FULL_YEAR_PREFIX = "20"
        }
    }
