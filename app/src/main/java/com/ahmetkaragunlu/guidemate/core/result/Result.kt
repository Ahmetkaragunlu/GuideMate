package com.ahmetkaragunlu.guidemate.core.result

sealed interface Result<out T> {

    data class Success<T>(val data: T) : Result<T>
    data class Error(val message: String, val exception: Throwable? = null) : Result<Nothing>
}