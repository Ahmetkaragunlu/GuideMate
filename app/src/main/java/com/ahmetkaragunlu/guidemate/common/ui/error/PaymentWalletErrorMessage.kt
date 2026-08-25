package com.ahmetkaragunlu.guidemate.common.ui.error

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.result.BackendErrorCode
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider

internal fun BackendErrorCode.paymentWalletErrorMessage(resourceProvider: ResourceProvider): String? =
    when (this) {
        BackendErrorCode.IDEMPOTENCY_CONFLICT ->
            resourceProvider.getString(R.string.error_idempotency_conflict)
        BackendErrorCode.PAYMENT_NOT_FOUND -> resourceProvider.getString(R.string.error_payment_not_found)
        BackendErrorCode.PAYMENT_INITIALIZATION_FAILED ->
            resourceProvider.getString(R.string.error_payment_initialization_failed)
        BackendErrorCode.PAYMENT_VERIFICATION_FAILED ->
            resourceProvider.getString(R.string.error_payment_verification_failed)
        BackendErrorCode.PAYMENT_NOT_CANCELLABLE ->
            resourceProvider.getString(R.string.error_payment_not_cancellable)
        BackendErrorCode.PAYMENT_CURRENCY_NOT_SUPPORTED ->
            resourceProvider.getString(R.string.error_payment_currency_not_supported)
        BackendErrorCode.FX_QUOTE_UNAVAILABLE ->
            resourceProvider.getString(R.string.error_payment_quote_unavailable)
        BackendErrorCode.FX_QUOTE_EXPIRED ->
            resourceProvider.getString(R.string.error_payment_quote_expired)
        BackendErrorCode.CARD_INSUFFICIENT_FUNDS ->
            resourceProvider.getString(R.string.error_card_insufficient_funds)
        BackendErrorCode.PAYMENT_METHOD_DECLINED ->
            resourceProvider.getString(R.string.error_payment_method_declined)
        BackendErrorCode.INVALID_AMOUNT -> resourceProvider.getString(R.string.error_invalid_amount)
        BackendErrorCode.INSUFFICIENT_WALLET_BALANCE ->
            resourceProvider.getString(R.string.checkout_error_insufficient_balance)
        BackendErrorCode.INSUFFICIENT_WITHDRAWABLE_BALANCE ->
            resourceProvider.getString(R.string.error_insufficient_withdrawable_balance)
        BackendErrorCode.BANK_ACCOUNT_NOT_FOUND ->
            resourceProvider.getString(R.string.error_bank_account_not_found)
        BackendErrorCode.BANK_ACCOUNT_INVALID ->
            resourceProvider.getString(R.string.error_bank_account_invalid)
        BackendErrorCode.BANK_ACCOUNT_ALREADY_EXISTS ->
            resourceProvider.getString(R.string.error_bank_account_already_exists)
        BackendErrorCode.SAVED_CARD_NOT_FOUND ->
            resourceProvider.getString(R.string.error_saved_card_not_found)
        BackendErrorCode.SAVED_CARD_SYNC_FAILED ->
            resourceProvider.getString(R.string.error_saved_card_sync_failed)
        BackendErrorCode.SAVED_CARD_PROVIDER_UNAVAILABLE ->
            resourceProvider.getString(R.string.error_saved_card_provider_unavailable)
        BackendErrorCode.REFUND_FAILED -> resourceProvider.getString(R.string.error_refund_failed)
        BackendErrorCode.REFUND_AMOUNT_EXCEEDED ->
            resourceProvider.getString(R.string.error_refund_amount_exceeded)
        else -> null
    }
