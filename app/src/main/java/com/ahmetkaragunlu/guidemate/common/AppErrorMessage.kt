package com.ahmetkaragunlu.guidemate.common

import com.ahmetkaragunlu.guidemate.R

fun AppError.toMessage(resourceProvider: ResourceProvider): String =
    when (this) {
        AppError.NoResponseFromServer -> resourceProvider.getString(R.string.error_no_response_from_server)
        AppError.GenericFailure -> resourceProvider.getString(R.string.error_generic_failure)
        AppError.SessionExpired -> resourceProvider.getString(R.string.error_session_expired)
        AppError.NoInternet -> resourceProvider.getString(R.string.error_no_internet)
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

fun AppError.isTerminalSessionError(): Boolean =
    this is AppError.SessionExpired ||
        (
            this is AppError.Backend &&
                code in
                    setOf(
                        BackendErrorCode.ACCOUNT_DISABLED,
                        BackendErrorCode.INVALID_REFRESH_TOKEN,
                        BackendErrorCode.REFRESH_TOKEN_EXPIRED,
                        BackendErrorCode.REFRESH_TOKEN_REPLAY,
                        BackendErrorCode.UNAUTHORIZED,
                    )
        )

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
