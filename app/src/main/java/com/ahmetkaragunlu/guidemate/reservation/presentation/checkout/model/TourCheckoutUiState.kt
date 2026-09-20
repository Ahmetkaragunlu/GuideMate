package com.ahmetkaragunlu.guidemate.reservation.presentation.checkout.model

import androidx.annotation.StringRes
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.payment.domain.model.CheckoutCurrency
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentMethod
import com.ahmetkaragunlu.guidemate.payment.domain.model.PaymentQuote
import com.ahmetkaragunlu.guidemate.payment.presentation.model.PaymentLaunch

data class TourCheckoutUiState(
    val loadState: ContentLoadState = ContentLoadState.LOADING,
    val summary: TourCheckoutSummaryUiState = TourCheckoutSummaryUiState(),
    val participantCount: Int = 1,
    val wallet: CheckoutWalletUiState = CheckoutWalletUiState(),
    val payment: CheckoutPaymentSelectionUiState = CheckoutPaymentSelectionUiState(),
    val terms: CheckoutTermsUiState = CheckoutTermsUiState(),
    val submission: CheckoutSubmissionUiState = CheckoutSubmissionUiState(),
) {
    val totalMinor: Long
        get() = summary.unitPriceMinor * participantCount

    val canDecreaseParticipants: Boolean
        get() = participantCount > 1

    val canIncreaseParticipants: Boolean
        get() = participantCount < summary.availableCapacity

    val isWalletPaymentVerifying: Boolean
        get() =
            payment.selectedMethod == PaymentMethod.WALLET &&
                submission.isPaymentActionInProgress
}

data class TourCheckoutSummaryUiState(
    val tourTitle: String = "",
    val date: String = "",
    val location: String = "",
    val unitPriceMinor: Long = 0,
    val availableCapacity: Int = 0,
)

data class CheckoutWalletUiState(
    val balanceMinor: Long = 0,
    val currencyCode: String = "USD",
)

data class CheckoutPaymentSelectionUiState(
    val chargeCurrencies: List<CheckoutCurrency> = emptyList(),
    val selectedChargeCurrencyCode: String? = null,
    val quote: PaymentQuote? = null,
    val selectedMethod: PaymentMethod = PaymentMethod.HOSTED_CARD,
)

data class CheckoutTermsUiState(
    val isAccepted: Boolean = false,
    val isSheetVisible: Boolean = false,
    val hasBeenRead: Boolean = false,
)

data class CheckoutSubmissionUiState(
    val isPaymentActionInProgress: Boolean = false,
    val paymentActionError: String? = null,
    val paymentLaunch: PaymentLaunch? = null,
    @param:StringRes val validationErrorResId: Int? = null,
)
