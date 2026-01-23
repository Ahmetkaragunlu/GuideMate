package com.ahmetkaragunlu.guidemate.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.features.tourist_graph.TouristRoute
import compose.icons.TablerIcons
import compose.icons.tablericons.Bell


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
                Text(
                    text = stringResource(id = titleResId, userName?: ""),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = { },
            actions = {
                Icon(
                    imageVector = TablerIcons.Bell,
                    contentDescription = null,
                    modifier = Modifier.padding(end = dimensionResource(R.dimen.spacing_small))
                )
            }
        )
    } else {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = stringResource(id = titleResId),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {

            },
            actions = {

            }
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
