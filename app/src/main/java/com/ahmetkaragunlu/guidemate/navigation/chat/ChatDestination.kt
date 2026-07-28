package com.ahmetkaragunlu.guidemate.navigation.chat

import kotlinx.serialization.Serializable

object ChatDestination {
    @Serializable
    data class Detail(
        val chatId: String,
    )
}
