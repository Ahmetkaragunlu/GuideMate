package com.ahmetkaragunlu.guidemate.common.ui.error

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.result.AppError
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
        ?.toFieldMessage(resourceProvider)

private fun BackendErrorCode?.toMessage(
    resourceProvider: ResourceProvider,
    retryAfterSeconds: Long?,
): String =
    commonErrorMessage(resourceProvider, retryAfterSeconds)
        ?: this?.authErrorMessage(resourceProvider)
        ?: this?.mediaProfileErrorMessage(resourceProvider)
        ?: this?.tourReservationErrorMessage(resourceProvider)
        ?: this?.paymentWalletErrorMessage(resourceProvider)
        ?: this?.chatNotificationErrorMessage(resourceProvider)
        ?: resourceProvider.getString(R.string.error_generic_failure)

private fun BackendErrorCode?.commonErrorMessage(
    resourceProvider: ResourceProvider,
    retryAfterSeconds: Long?,
): String? =
    when (this) {
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
        BackendErrorCode.INTERNAL_SERVER_ERROR -> resourceProvider.getString(R.string.error_generic_failure)
        else -> null
    }

private fun Long.toResourceQuantity(): Int =
    coerceIn(0, Int.MAX_VALUE.toLong()).toInt()
