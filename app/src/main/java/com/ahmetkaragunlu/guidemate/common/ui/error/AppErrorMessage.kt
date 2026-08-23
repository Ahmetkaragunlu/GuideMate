package com.ahmetkaragunlu.guidemate.common.ui.error

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.AppFieldError
import com.ahmetkaragunlu.guidemate.common.result.BackendErrorCode
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider

fun AppError.toMessage(resourceProvider: ResourceProvider): String =
    when (this) {
        AppError.NoResponseFromServer -> resourceProvider.getString(R.string.error_no_response_from_server)
        AppError.GenericFailure -> resourceProvider.getString(R.string.error_generic_failure)
        AppError.SessionExpired -> resourceProvider.getString(R.string.error_session_expired)
        AppError.NoInternet -> resourceProvider.getString(R.string.error_no_internet)
        AppError.InvalidImageType -> resourceProvider.getString(R.string.error_image_invalid_type)
        AppError.ImageTooLarge -> resourceProvider.getString(R.string.error_image_too_large)
        AppError.ImageUnavailable -> resourceProvider.getString(R.string.error_image_source_unavailable)
        AppError.Unknown -> resourceProvider.getString(R.string.error_unknown)
        is AppError.Server -> resourceProvider.getString(R.string.error_server, code)
        is AppError.Backend -> code.toMessage(resourceProvider, retryAfterSeconds)
    }

fun AppError.fieldMessage(
    field: String,
    resourceProvider: ResourceProvider,
): String? =
    (this as? AppError.Backend)
        ?.fieldErrors
        ?.firstOrNull { it.field == field }
        ?.toMessage(resourceProvider)

private fun BackendErrorCode?.toMessage(
    resourceProvider: ResourceProvider,
    retryAfterSeconds: Long?,
): String =
    when (this) {
        BackendErrorCode.USER_NOT_FOUND -> resourceProvider.getString(R.string.error_user_not_found)
        BackendErrorCode.EMAIL_ALREADY_EXISTS -> resourceProvider.getString(R.string.error_email_already_exists)
        BackendErrorCode.ACCOUNT_PENDING_VERIFICATION ->
            resourceProvider.getString(R.string.error_account_pending_verification)
        BackendErrorCode.ACCOUNT_DISABLED -> resourceProvider.getString(R.string.error_account_disabled)
        BackendErrorCode.INVALID_CREDENTIALS -> resourceProvider.getString(R.string.error_invalid_credentials)
        BackendErrorCode.CURRENT_PASSWORD_INCORRECT ->
            resourceProvider.getString(R.string.error_current_password_incorrect)
        BackendErrorCode.PASSWORD_POLICY_VIOLATION ->
            resourceProvider.getString(R.string.error_password_policy)
        BackendErrorCode.PASSWORD_SAME_AS_CURRENT ->
            resourceProvider.getString(R.string.error_password_same_as_current)
        BackendErrorCode.PASSWORDS_DO_NOT_MATCH ->
            resourceProvider.getString(R.string.error_passwords_do_not_match)
        BackendErrorCode.ROLE_ALREADY_SELECTED ->
            resourceProvider.getString(R.string.error_role_already_selected)
        BackendErrorCode.ROLE_NOT_FOUND -> resourceProvider.getString(R.string.error_role_not_found)
        BackendErrorCode.INVALID_INSTALLATION_ID ->
            resourceProvider.getString(R.string.error_invalid_installation)
        BackendErrorCode.INVALID_TOKEN,
        BackendErrorCode.INVALID_REFRESH_TOKEN,
        -> resourceProvider.getString(R.string.error_invalid_token)
        BackendErrorCode.TOKEN_EXPIRED,
        BackendErrorCode.REFRESH_TOKEN_EXPIRED,
        -> resourceProvider.getString(R.string.error_session_expired)
        BackendErrorCode.TOKEN_ALREADY_USED -> resourceProvider.getString(R.string.error_token_already_used)
        BackendErrorCode.REFRESH_TOKEN_REPLAY -> resourceProvider.getString(R.string.error_session_expired)
        BackendErrorCode.GOOGLE_LOGIN_FAILED -> resourceProvider.getString(R.string.error_google_login_failed)
        BackendErrorCode.GOOGLE_ACCOUNT_NOT_FOUND ->
            resourceProvider.getString(R.string.error_google_account_not_found)
        BackendErrorCode.GOOGLE_ACCOUNT_MISMATCH ->
            resourceProvider.getString(R.string.error_google_account_mismatch)
        BackendErrorCode.UNAUTHORIZED -> resourceProvider.getString(R.string.error_session_expired)
        BackendErrorCode.FORBIDDEN -> resourceProvider.getString(R.string.error_forbidden)
        BackendErrorCode.VALIDATION_FAILED -> resourceProvider.getString(R.string.error_validation_failed)
        BackendErrorCode.MALFORMED_REQUEST -> resourceProvider.getString(R.string.error_malformed_request)
        BackendErrorCode.DATA_CONFLICT -> resourceProvider.getString(R.string.error_data_conflict)
        BackendErrorCode.RATE_LIMITED ->
            retryAfterSeconds?.let {
                val seconds = it.toResourceQuantity()
                resourceProvider.getQuantityString(
                    R.plurals.error_rate_limited_with_seconds,
                    seconds,
                    seconds,
                )
            } ?: resourceProvider.getString(R.string.error_rate_limited)
        BackendErrorCode.EMAIL_DELIVERY_FAILED ->
            resourceProvider.getString(R.string.error_email_delivery_failed)
        BackendErrorCode.MEDIA_NOT_FOUND -> resourceProvider.getString(R.string.error_media_not_found)
        BackendErrorCode.MEDIA_INVALID_TYPE ->
            resourceProvider.getString(R.string.error_image_invalid_type)
        BackendErrorCode.MEDIA_TOO_LARGE -> resourceProvider.getString(R.string.error_image_too_large)
        BackendErrorCode.MEDIA_STORAGE_FAILED ->
            resourceProvider.getString(R.string.error_media_storage_failed)
        BackendErrorCode.MEDIA_IN_USE -> resourceProvider.getString(R.string.error_media_in_use)
        BackendErrorCode.MEDIA_PURPOSE_MISMATCH ->
            resourceProvider.getString(R.string.error_media_purpose_mismatch)
        BackendErrorCode.GUIDE_PROFILE_NOT_FOUND ->
            resourceProvider.getString(R.string.guide_profile_not_found)
        BackendErrorCode.INVALID_LANGUAGE_CODE ->
            resourceProvider.getString(R.string.error_invalid_language_code)
        BackendErrorCode.TOUR_NOT_FOUND -> resourceProvider.getString(R.string.error_tour_not_found)
        BackendErrorCode.TOUR_NOT_APPROVED ->
            resourceProvider.getString(R.string.error_tour_not_approved)
        BackendErrorCode.TOUR_CHANGE_PENDING ->
            resourceProvider.getString(R.string.error_tour_change_pending)
        BackendErrorCode.TOUR_LOCATION_LOCKED ->
            resourceProvider.getString(R.string.error_tour_location_locked)
        BackendErrorCode.TOUR_NOT_ARCHIVABLE ->
            resourceProvider.getString(R.string.error_tour_not_archivable)
        BackendErrorCode.TOUR_REVIEW_NOT_FOUND ->
            resourceProvider.getString(R.string.error_tour_review_not_found)
        BackendErrorCode.TOUR_REVIEW_STATE_INVALID ->
            resourceProvider.getString(R.string.error_tour_review_state_invalid)
        BackendErrorCode.INVALID_CATEGORY_CODE ->
            resourceProvider.getString(R.string.error_invalid_category_code)
        BackendErrorCode.INVALID_COUNTRY_CODE ->
            resourceProvider.getString(R.string.error_invalid_country_code)
        BackendErrorCode.INVALID_TIME_ZONE ->
            resourceProvider.getString(R.string.error_invalid_time_zone)
        BackendErrorCode.SESSION_NOT_FOUND ->
            resourceProvider.getString(R.string.error_session_not_found)
        BackendErrorCode.SESSION_NOT_BOOKABLE ->
            resourceProvider.getString(R.string.error_session_not_bookable)
        BackendErrorCode.SESSION_ALREADY_STARTED ->
            resourceProvider.getString(R.string.error_session_already_started)
        BackendErrorCode.SESSION_HAS_RESERVATIONS ->
            resourceProvider.getString(R.string.error_session_has_reservations)
        BackendErrorCode.CAPACITY_NOT_AVAILABLE ->
            resourceProvider.getString(R.string.error_capacity_not_available)
        BackendErrorCode.CAPACITY_BELOW_BOOKED_COUNT ->
            resourceProvider.getString(R.string.error_capacity_below_booked_count)
        BackendErrorCode.SESSION_STATUS_NOT_MANAGEABLE ->
            resourceProvider.getString(R.string.error_session_status_not_manageable)
        BackendErrorCode.SCHEDULE_CONFLICT ->
            resourceProvider.getString(R.string.error_schedule_conflict)
        BackendErrorCode.CONCURRENT_UPDATE ->
            resourceProvider.getString(R.string.error_concurrent_update)
        BackendErrorCode.RESERVATION_NOT_FOUND ->
            resourceProvider.getString(R.string.error_reservation_not_found)
        BackendErrorCode.RESERVATION_ALREADY_EXISTS ->
            resourceProvider.getString(R.string.error_reservation_already_exists)
        BackendErrorCode.RESERVATION_NOT_CANCELLABLE ->
            resourceProvider.getString(R.string.error_reservation_not_cancellable)
        BackendErrorCode.REVIEW_NOT_ALLOWED ->
            resourceProvider.getString(R.string.error_review_not_allowed)
        BackendErrorCode.REVIEW_ALREADY_EXISTS ->
            resourceProvider.getString(R.string.error_review_already_exists)
        BackendErrorCode.IDEMPOTENCY_CONFLICT ->
            resourceProvider.getString(R.string.error_idempotency_conflict)
        BackendErrorCode.PAYMENT_NOT_FOUND ->
            resourceProvider.getString(R.string.error_payment_not_found)
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
        BackendErrorCode.INVALID_AMOUNT ->
            resourceProvider.getString(R.string.error_invalid_amount)
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
        BackendErrorCode.INTERNAL_SERVER_ERROR -> resourceProvider.getString(R.string.error_generic_failure)
        null -> resourceProvider.getString(R.string.error_generic_failure)
    }

private fun AppFieldError.toMessage(resourceProvider: ResourceProvider): String =
    when (code) {
        "FIELD_REQUIRED" -> resourceProvider.getString(R.string.error_field_required)
        "INVALID_EMAIL" -> resourceProvider.getString(R.string.email_error_message)
        "INVALID_SIZE" -> resourceProvider.getString(R.string.error_invalid_field_size)
        "INVALID_FORMAT" -> resourceProvider.getString(R.string.error_invalid_field_format)
        else -> resourceProvider.getString(R.string.error_invalid_field)
    }

private fun Long.toResourceQuantity(): Int =
    coerceIn(0, Int.MAX_VALUE.toLong()).toInt()
