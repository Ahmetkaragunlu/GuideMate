package com.ahmetkaragunlu.guidemate.common.result

inline fun <T, R> DataResult<T>.mapSuccess(transform: (T) -> R): DataResult<R> =
    when (this) {
        is DataResult.Success -> DataResult.Success(transform(data))
        is DataResult.Error -> this
    }
