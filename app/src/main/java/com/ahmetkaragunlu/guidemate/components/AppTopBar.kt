package com.ahmetkaragunlu.guidemate.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.features.tourist_graph.TouristRoute


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TouristAppBar(
    currentRoute: String,
    navController: NavController,
    userName: String?
) {
    val titleResId = getTouristScreenTitle(currentRoute)

    if (currentRoute == TouristRoute.TouristHomeScreen.route) {
        TopAppBar(
            title = {
                Text(text = stringResource(id = titleResId, userName?: ""))
            },
            navigationIcon = { },
            actions = {}
        )
    } else {
        CenterAlignedTopAppBar(
            title = {
                Text(text = stringResource(id = titleResId))
            },
            navigationIcon = { },
            actions = {}
        )
    }
}

fun getTouristScreenTitle(route: String?): Int {
    return when (route) {
        TouristRoute.TouristHomeScreen.route -> R.string.welcome_message
        TouristRoute.TouristExploreScreen.route -> R.string.tourist_explore
        TouristRoute.TouristMyTripsScreen.route -> R.string.tourist_trips
        TouristRoute.TouristChatScreen.route -> R.string.tourist_chat
        TouristRoute.TouristProfileScreen.route -> R.string.tourist_profile
        else -> R.string.app_name
    }
}
