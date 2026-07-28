package com.ahmetkaragunlu.guidemate.navigation

import androidx.navigation.NavController

fun <T : Any, C : Any> NavController.switchRoot(
    targetDestination: T,
    clearBackStackFrom: C,
) {
    navigate(targetDestination) {
        popUpTo(clearBackStackFrom) { inclusive = true }
        launchSingleTop = true
        restoreState = false
    }
}

fun <T : Any> NavController.navigateTo(destination: T) {
    navigate(destination) {
        launchSingleTop = true
        restoreState = false
    }
}

fun <T : Any, S : Any> NavController.navigateBottomBar(
    destination: T,
    startDestination: S,
) {
    if (destination == startDestination) {
        popBackStack(
            route = startDestination,
            inclusive = false,
        )
        return
    }

    navigate(destination) {
        popUpTo(startDestination) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
