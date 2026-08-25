package com.ahmetkaragunlu.guidemate.common.ui.error

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.result.BackendErrorCode
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider

internal fun BackendErrorCode.authErrorMessage(resourceProvider: ResourceProvider): String? =
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
        BackendErrorCode.REFRESH_TOKEN_REPLAY,
        -> resourceProvider.getString(R.string.error_session_expired)
        BackendErrorCode.TOKEN_ALREADY_USED -> resourceProvider.getString(R.string.error_token_already_used)
        BackendErrorCode.GOOGLE_LOGIN_FAILED -> resourceProvider.getString(R.string.error_google_login_failed)
        BackendErrorCode.GOOGLE_ACCOUNT_NOT_FOUND ->
            resourceProvider.getString(R.string.error_google_account_not_found)
        BackendErrorCode.GOOGLE_ACCOUNT_MISMATCH ->
            resourceProvider.getString(R.string.error_google_account_mismatch)
        BackendErrorCode.EMAIL_DELIVERY_FAILED ->
            resourceProvider.getString(R.string.error_email_delivery_failed)
        else -> null
    }
