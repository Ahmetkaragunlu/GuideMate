package com.ahmetkaragunlu.guidemate.common.ui.resource

import androidx.annotation.PluralsRes
import androidx.annotation.StringRes

interface ResourceProvider {
    fun getString(
        @StringRes id: Int,
    ): String

    fun getString(
        @StringRes id: Int,
        vararg args: Any,
    ): String

    fun getQuantityString(
        @PluralsRes id: Int,
        quantity: Int,
        vararg args: Any,
    ): String
}
