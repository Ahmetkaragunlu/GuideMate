package com.ahmetkaragunlu.guidemate.common

sealed interface DataResult<out T> {
    data class Success<T>(
        val data: T,
    ) : DataResult<T>

    data class Error(
        val error: AppError,
        val exception: Throwable? = null,
    ) : DataResult<Nothing>
}
