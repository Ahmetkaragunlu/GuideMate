package com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.savedcards.viewmodel

import androidx.lifecycle.ViewModel
import com.ahmetkaragunlu.guidemate.screens.tourist.finance.model.SavedPaymentCardUiModel
import com.ahmetkaragunlu.guidemate.screens.tourist.finance.store.TouristFinanceStore
import com.ahmetkaragunlu.guidemate.screens.tourist.profile.account.savedcards.sandbox.SandboxCardMetadata
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddSavedCardViewModel
    @Inject
    constructor(
        private val financeStore: TouristFinanceStore,
    ) : ViewModel() {
        fun addSandboxCard(
            metadata: SandboxCardMetadata,
            cardHolderName: String,
            expiryMonth: String,
            expiryYear: String,
        ) {
            financeStore.addCard(
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
