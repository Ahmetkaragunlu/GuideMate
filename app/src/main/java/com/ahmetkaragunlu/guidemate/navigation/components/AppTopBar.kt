package com.ahmetkaragunlu.guidemate.navigation.components

import androidx.annotation.StringRes
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.components.EditAlertDialog
import com.ahmetkaragunlu.guidemate.common.ui.image.GuideMateImage
import compose.icons.TablerIcons
import compose.icons.tablericons.ArrowLeft
import compose.icons.tablericons.Bell
import compose.icons.tablericons.Logout

data class AppTopBarConfig(
    val isHome: Boolean,
    val isChatDetail: Boolean,
    val showBackButton: Boolean,
    val showLogoutButton: Boolean,
    @param:StringRes val titleResId: Int,
    val chatTitle: String = "",
    @param:DrawableRes val chatAvatarResId: Int = R.drawable.ic_default_avatar,
    val chatAvatarUrl: String? = null,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    config: AppTopBarConfig,
    userName: String?,
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit,
    unreadNotificationCount: Int = 0,
    onNotificationClick: () -> Unit = {},
) {
    var showLogoutConfirmation by rememberSaveable { mutableStateOf(false) }

    if (config.isHome) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.welcome_message, userName ?: ""),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                )
            },
            actions = {
                IconButton(
                    onClick = onNotificationClick,
                    modifier = Modifier.padding(end = dimensionResource(R.dimen.spacing_tiny)),
                ) {
                    BadgedBox(
                        badge = {
                            if (unreadNotificationCount > 0) {
                                Badge {
                                    Text(text = unreadNotificationCount.coerceAtMost(99).toString())
                                }
                            }
                        },
                    ) {
                        Icon(
                            imageVector = TablerIcons.Bell,
                            contentDescription = stringResource(R.string.notifications),
                        )
                    }
                }
            },
        )
    } else {
        CenterAlignedTopAppBar(
            title = {
                if (config.isChatDetail) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        GuideMateImage(
                            fallbackImageResId = config.chatAvatarResId,
                            imageUrl = config.chatAvatarUrl,
                            contentDescription = null,
                            modifier =
                                Modifier
                                    .size(32.dp)
                                    .clip(CircleShape),
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_small)))
                        Text(
                            text = config.chatTitle,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                } else {
                    Text(
                        text = stringResource(id = config.titleResId),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                    )
                }
            },
            navigationIcon = {
                if (config.showBackButton) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = TablerIcons.ArrowLeft,
                            contentDescription = null,
                        )
                    }
                }
            },
            actions = {
                if (config.showLogoutButton) {
                    IconButton(onClick = { showLogoutConfirmation = true }) {
                        Icon(
                            imageVector = TablerIcons.Logout,
                            contentDescription = null,
                        )
                    }
                }
            },
        )
    }

    if (showLogoutConfirmation) {
        EditAlertDialog(
            title = R.string.logout_confirmation_title,
            text = R.string.logout_confirmation_message,
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutConfirmation = false
                        onLogoutClick()
                    },
                ) {
                    Text(
                        text = stringResource(R.string.yes),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutConfirmation = false }) {
                    Text(text = stringResource(R.string.no))
                }
            },
            onDismissRequest = { showLogoutConfirmation = false },
        )
    }
}
