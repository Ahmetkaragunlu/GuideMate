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
import com.ahmetkaragunlu.guidemate.features.guide_graph.GuideRoute
import compose.icons.TablerIcons
import compose.icons.tablericons.ArrowLeft
import compose.icons.tablericons.Bell
import compose.icons.tablericons.Logout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuideTopBar(
    currentRoute: String,
    navController: NavController,
    userName: String?,
) {
    val isChatDetail = currentRoute == GuideRoute.GuideChatDetailScreen.route

    if (currentRoute == GuideRoute.GuideHomeScreen.route) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.welcome_message, userName ?: ""),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                )
            },
            actions = {
                Icon(
                    imageVector = TablerIcons.Bell,
                    contentDescription = null,
                    modifier = Modifier.padding(end = dimensionResource(R.dimen.spacing_small)),
                )
            },
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
                            modifier =
                                Modifier
                                    .size(32.dp)
                                    .clip(CircleShape),
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Hans Müller",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                } else {
                    val titleResId = getGuideScreenTitle(currentRoute)
                    Text(
                        text = stringResource(id = titleResId),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                    )
                }
            },
            navigationIcon = {
                if (isChatDetail) {
                    IconButton(
                        onClick = { navController.navigateUp() },
                    ) {
                        Icon(
                            imageVector = TablerIcons.ArrowLeft,
                            contentDescription = null,
                        )
                    }
                }
            },
            actions = {
                if (currentRoute == GuideRoute.GuideProfileScreen.route) {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = TablerIcons.Logout,
                            contentDescription = null,
                        )
                    }
                }
            },
        )
    }
}

fun getGuideScreenTitle(route: String?): Int =
    when (route) {
        GuideRoute.GuideHomeScreen.route -> R.string.welcome_message // String kaynağını kontrol et (guide_home olabilir)
        GuideRoute.GuideMyToursScreen.route -> R.string.guide_tours
        GuideRoute.GuideMyWalletScreen.route -> R.string.guide_wallet
        GuideRoute.GuideChatScreen.route -> R.string.guide_chat
        GuideRoute.GuideProfileScreen.route -> R.string.guide_profile
        else -> R.string.app_name
    }
