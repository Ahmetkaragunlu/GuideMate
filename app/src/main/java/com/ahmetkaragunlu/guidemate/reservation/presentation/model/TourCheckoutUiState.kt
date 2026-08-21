package com.ahmetkaragunlu.guidemate.reservation.presentation.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.ahmetkaragunlu.guidemate.payment.presentation.model.SavedPaymentCardUiModel
import com.ahmetkaragunlu.guidemate.payment.presentation.status.model.PaymentMethod

data class TourCheckoutUiState(
    val sessionId: String = "",
    val tourTitle: String = "",
    val date: String = "",
    val location: String = "",
    val unitPriceMinor: Long = 0,
    val participantCount: Int = 1,
    val availableCapacity: Int = 0,
    val walletBalanceMinor: Long = 0,
    val savedCards: List<SavedPaymentCardUiModel> = emptyList(),
    val selectedMethod: PaymentMethod = PaymentMethod.SAVED_CARD,
    val selectedCardId: String? = null,
    val termsAccepted: Boolean = false,
    @param:StringRes val validationErrorResId: Int? = null,
) {
    val totalMinor: Long
        get() = unitPriceMinor * participantCount

    val canDecreaseParticipants: Boolean
        get() = participantCount > 1

    val canIncreaseParticipants: Boolean
        get() = participantCount < availableCapacity
}
