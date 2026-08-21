package com.ahmetkaragunlu.guidemate.common.result

data class AppFieldError(
    val field: String,
    val code: String,
    val fallbackMessage: String?,
)
