package com.ahmetkaragunlu.guidemate.common

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

interface ResourceProvider {
    fun getString(
        @StringRes id: Int,
    ): String

    fun getString(
        @StringRes id: Int,
        vararg args: Any,
    ): String
}

class ResourceProviderImpl
@Inject
constructor(
    @ApplicationContext private val context: Context,
) : ResourceProvider {
    override fun getString(
        @StringRes id: Int,
    ): String = context.getString(id)

    override fun getString(
        @StringRes id: Int,
        vararg args: Any,
    ): String = context.getString(id, *args)
}
