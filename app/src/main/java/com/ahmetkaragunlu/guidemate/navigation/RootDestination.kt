package com.ahmetkaragunlu.guidemate.navigation

import com.ahmetkaragunlu.guidemate.navigation.guide.account.GuideAccountStart
import com.ahmetkaragunlu.guidemate.navigation.tourist.account.TouristAccountStart
import kotlinx.serialization.Serializable

object RootDestination {
    @Serializable data object Auth

    @Serializable data object Guide

    @Serializable data object Tourist

    @Serializable
    data class GuideAccount(
        val startDestination: GuideAccountStart,
    )

    @Serializable
    data class TouristAccount(
        val startDestination: TouristAccountStart,
    )
}
