package com.ahmetkaragunlu.guidemate.testing.common

import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider

class FakeResourceProvider : ResourceProvider {
    override fun getString(id: Int): String = "string-$id"

    override fun getString(id: Int, vararg args: Any): String = "string-$id"

    override fun getQuantityString(id: Int, quantity: Int, vararg args: Any): String =
        "quantity-$id-$quantity"
}
