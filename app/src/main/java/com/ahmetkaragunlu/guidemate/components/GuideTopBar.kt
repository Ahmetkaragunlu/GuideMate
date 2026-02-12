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
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.features.guide_graph.GuideRoute
import compose.icons.TablerIcons
import compose.icons.tablericons.Bell


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuideTopBar(
    currentRoute: String,
    userName: String?
) {
    if (currentRoute == GuideRoute.GuideHomeScreen.route) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.welcome_message, userName ?: ""),
                    style = MaterialTheme.typography.bodyLarge,
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
                Text(
                    text = stringResource(id = getGuideScreenTitle(currentRoute)),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {},
            actions = {}
        )
    }

}


fun getGuideScreenTitle(route: String?): Int {
    return when (route) {
        GuideRoute.GuideHomeScreen.route -> R.string.guide_home
        GuideRoute.GuideMyToursScreen.route -> R.string.guide_tours
        GuideRoute.GuideMyWalletScreen.route -> R.string.guide_wallet
        GuideRoute.GuideChatScreen.route -> R.string.guide_chat
        GuideRoute.GuideProfileScreen.route -> R.string.guide_profile
        else -> R.string.app_name
    }
}