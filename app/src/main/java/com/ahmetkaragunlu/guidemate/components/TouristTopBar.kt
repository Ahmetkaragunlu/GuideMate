package com.ahmetkaragunlu.guidemate.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.features.tourist_graph.TouristRoute
import compose.icons.TablerIcons
import compose.icons.tablericons.ArrowLeft
import compose.icons.tablericons.Bell


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TouristAppBar(
    currentRoute: String,
    navController: NavController,
    userName: String?
) {
    val isChatDetail = currentRoute == TouristRoute.TouristChatDetailScreen.route

    if (currentRoute == TouristRoute.TouristHomeScreen.route) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.welcome_message, userName ?: ""),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            },
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
                if (isChatDetail) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.example),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Ahmet Rehber",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                } else {
                    val titleResId = getTouristScreenTitle(currentRoute)
                    Text(
                        text = stringResource(id = titleResId),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            navigationIcon = {
                if (currentRoute == TouristRoute.TouristFilterScreen.route || isChatDetail) {
                    IconButton(
                        onClick = { navController.navigateUp() }
                    ) {
                        Icon(
                            imageVector = TablerIcons.ArrowLeft,
                            contentDescription = null
                        )
                    }
                }
            },
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
        TouristRoute.TouristFilterScreen.route -> R.string.filter
        else -> R.string.app_name
    }
}