package com.ahmetkaragunlu.guidemate.common


sealed interface DataResult<out T> {
    data class Success<T>(val data: T) : DataResult<T>
    data class Error(val message: String, val exception: Throwable? = null) : DataResult<Nothing>
}