package com.ahmetkaragunlu.guidemate.common.result

sealed interface AppError {
    data object NoResponseFromServer : AppError
    data object GenericFailure : AppError
    data object SessionExpired : AppError
    data object NoInternet : AppError
    data object Unknown : AppError
    data class Server(
        val code: Int,
    ) : AppError

    data class Backend(
        val code: BackendErrorCode?,
        val fallbackMessage: String?,
        val fieldErrors: List<AppFieldError> = emptyList(),
        val retryAfterSeconds: Long? = null,
    ) : AppError
}
