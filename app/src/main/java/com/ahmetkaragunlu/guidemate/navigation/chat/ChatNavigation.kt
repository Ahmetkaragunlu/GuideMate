package com.ahmetkaragunlu.guidemate.navigation.chat

internal const val CHAT_ID_ARGUMENT = "chatId"

internal fun chatDetailRoutePattern(baseRoute: String): String =
    "$baseRoute/{$CHAT_ID_ARGUMENT}"

internal fun chatDetailRoute(
    baseRoute: String,
    chatId: String,
): String = "$baseRoute/$chatId"
