package com.ahmetkaragunlu.guidemate.common

data class AppFieldError(
    val field: String,
    val code: String,
    val fallbackMessage: String?,
)
