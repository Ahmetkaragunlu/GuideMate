package com.ahmetkaragunlu.guidemate.common

import androidx.annotation.StringRes

interface ResourceProvider {
    fun getString(
        @StringRes id: Int,
    ): String

    fun getString(
        @StringRes id: Int,
        vararg args: Any,
    ): String
}

