package com.ahmetkaragunlu.guidemate.navigation.guide

import kotlinx.serialization.Serializable

object GuideDestination {
    @Serializable data object Home

    @Serializable data object Chat

    @Serializable data object Profile

    @Serializable data object ProfilePreview
}
