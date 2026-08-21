package com.ahmetkaragunlu.guidemate.common.ui.resource

import android.content.Context
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ResourceProviderImpl
@Inject
constructor(
    @param:ApplicationContext private val context: Context,
) : ResourceProvider {
    override fun getString(
        @StringRes id: Int,
    ): String = context.getString(id)

    override fun getString(
        @StringRes id: Int,
        vararg args: Any,
    ): String = context.getString(id, *args)

    override fun getQuantityString(
        @PluralsRes id: Int,
        quantity: Int,
        vararg args: Any,
    ): String = context.resources.getQuantityString(id, quantity, *args)
}
