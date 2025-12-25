package com.ahmetkaragunlu.guidemate.navigation


import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination


fun NavController.switchRoot(route: String) {
    this.navigate(route) {
        popUpTo(0) { inclusive = true }
    }
}

fun NavController.navigateTo(route: String) {
    this.navigate(route) {
        launchSingleTop = true
        restoreState = false
    }
}

fun NavController.navigateBottomBar(route: String) {
    this.navigate(route) {
        popUpTo(id = graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}



