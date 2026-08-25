package com.ahmetkaragunlu.guidemate.navigation.guide

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.navigation.components.AppBottomBar
import com.ahmetkaragunlu.guidemate.navigation.components.AppTopBar
import com.ahmetkaragunlu.guidemate.navigation.components.BottomBarItem
import com.ahmetkaragunlu.guidemate.navigation.chat.ChatDestination
import com.ahmetkaragunlu.guidemate.navigation.guide.wallet.GuideWalletDestination
import com.ahmetkaragunlu.guidemate.navigation.guide.tours.GuideTourDestination
import com.ahmetkaragunlu.guidemate.navigation.navigateBottomBar
import com.ahmetkaragunlu.guidemate.chat.presentation.viewmodel.ChatListViewModel
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.earnings.GuideEarningsViewModel
import com.ahmetkaragunlu.guidemate.home.presentation.guide.GuideHomeViewModel
import com.ahmetkaragunlu.guidemate.navigation.navigateTo
import com.ahmetkaragunlu.guidemate.navigation.notification.toGuideDestination
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationNavigationTarget
import com.ahmetkaragunlu.guidemate.notification.presentation.NotificationViewModel
import com.ahmetkaragunlu.guidemate.notification.presentation.NotificationSyncEffect
import com.ahmetkaragunlu.guidemate.notification.presentation.components.NotificationBottomSheet
import com.ahmetkaragunlu.guidemate.notification.presentation.permission.NotificationPermissionEffect
import compose.icons.TablerIcons
import compose.icons.tablericons.CreditCard
import compose.icons.tablericons.Home
import compose.icons.tablericons.MessageCircle2
import compose.icons.tablericons.Ticket
import compose.icons.tablericons.User

@Composable
fun GuideNavigation(
    routeNavController: NavController,
    onLogoutClick: () -> Unit,
    pendingNotificationTarget: NotificationNavigationTarget?,
    onNotificationNavigationHandled: (NotificationNavigationTarget) -> Unit,
    homeViewModel: GuideHomeViewModel = hiltViewModel(),
    earningsViewModel: GuideEarningsViewModel = hiltViewModel(),
    notificationViewModel: NotificationViewModel = hiltViewModel(),
    chatListViewModel: ChatListViewModel = hiltViewModel(),
) {
    NotificationPermissionEffect()
    NotificationSyncEffect(notificationViewModel::refresh)
    val guideNavController = rememberNavController()
    val context = LocalContext.current
    val navBackStackEntry by guideNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val navigationUiConfig = currentDestination.guideNavigationUiConfig()
    val userName by homeViewModel.userName.collectAsStateWithLifecycle()
    val notificationsUiState by notificationViewModel.uiState.collectAsStateWithLifecycle()
    val chatListUiState by chatListViewModel.uiState.collectAsStateWithLifecycle()
    val activeChatId =
        navBackStackEntry
            ?.takeIf { it.destination.hasRoute<ChatDestination.Detail>() }
            ?.toRoute<ChatDestination.Detail>()
            ?.chatId
    val activeChat = chatListUiState.chats.firstOrNull { it.chatId == activeChatId }
    var showNotifications by rememberSaveable { mutableStateOf(false) }
    var customBackAction by remember { mutableStateOf<(() -> Unit)?>(null) }

    LaunchedEffect(pendingNotificationTarget) {
        pendingNotificationTarget?.let { target ->
            target.notificationId?.let(notificationViewModel::markRead)
            guideNavController.navigateTo(target.toGuideDestination())
            onNotificationNavigationHandled(target)
        }
    }

    LaunchedEffect(notificationsUiState.errorMessage) {
        notificationsUiState.errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            notificationViewModel.onMessageShown()
        }
    }

    Box {
        Scaffold(
            topBar = {
                AppTopBar(
                    config =
                        navigationUiConfig.topBar.copy(
                            chatTitle = activeChat?.name.orEmpty(),
                            chatAvatarResId = activeChat?.avatarResId ?: R.drawable.example,
                            chatAvatarUrl = activeChat?.avatarUrl,
                        ),
                    userName = userName,
                    onBackClick = {
                        customBackAction?.invoke() ?: guideNavController.navigateUp()
                    },
                    onLogoutClick = onLogoutClick,
                    unreadNotificationCount = notificationsUiState.unreadCount,
                    onNotificationClick = { showNotifications = true },
                )
            },
            bottomBar = {
                if (navigationUiConfig.showBottomBar) {
                    val selectedDestination = currentDestination.guideBottomBarDestination()
                    AppBottomBar(
                        selectedDestination = selectedDestination,
                        items = guideBottomBarItems,
                        badgeCounts =
                            mapOf(
                                GuideBottomBarDestination.CHAT to
                                    chatListUiState.totalUnreadCount,
                            ),
                        onDestinationClick = { destination ->
                            guideNavController.navigateBottomBar(
                                destination = destination.toRoute(),
                                startDestination = GuideDestination.Home,
                            )
                        },
                    )
                }
            },
        ) { innerPadding ->
            NavHost(
                navController = guideNavController,
                startDestination = GuideDestination.Home,
                modifier = Modifier.padding(innerPadding),
            ) {
                guideNavGraph(
                    guideNavController = guideNavController,
                    routeNavController = routeNavController,
                    homeViewModel = homeViewModel,
                    earningsViewModel = earningsViewModel,
                    recentNotifications = notificationsUiState.recentNotifications,
                    chatListViewModel = chatListViewModel,
                    onBackActionChanged = { customBackAction = it },
                )
            }
        }

        NotificationBottomSheet(
            isVisible = showNotifications,
            uiState = notificationsUiState,
            onDismiss = { showNotifications = false },
            onNotificationClick = { target ->
                showNotifications = false
                target.notificationId?.let(notificationViewModel::markRead)
                guideNavController.navigateTo(target.toGuideDestination())
            },
            onMarkAllRead = notificationViewModel::markAllRead,
            onRefresh = notificationViewModel::refresh,
            onLoadMore = notificationViewModel::loadMore,
        )
    }
}

private enum class GuideBottomBarDestination {
    HOME,
    TOURS,
    WALLET,
    CHAT,
    PROFILE,
}

private val guideBottomBarItems =
    listOf(
        BottomBarItem(
            label = R.string.guide_home,
            icon = TablerIcons.Home,
            destination = GuideBottomBarDestination.HOME,
        ),
        BottomBarItem(
            label = R.string.guide_tours,
            icon = TablerIcons.Ticket,
            destination = GuideBottomBarDestination.TOURS,
        ),
        BottomBarItem(
            label = R.string.guide_wallet,
            icon = TablerIcons.CreditCard,
            destination = GuideBottomBarDestination.WALLET,
        ),
        BottomBarItem(
            label = R.string.guide_chat,
            icon = TablerIcons.MessageCircle2,
            destination = GuideBottomBarDestination.CHAT,
        ),
        BottomBarItem(
            label = R.string.guide_profile,
            icon = TablerIcons.User,
            destination = GuideBottomBarDestination.PROFILE,
        ),
    )

private fun NavDestination?.guideBottomBarDestination(): GuideBottomBarDestination? =
    when {
        this == null || hasRoute<GuideDestination.Home>() -> GuideBottomBarDestination.HOME
        hasRoute<GuideTourDestination.MyTours>() -> GuideBottomBarDestination.TOURS
        hasRoute<GuideWalletDestination.Wallet>() -> GuideBottomBarDestination.WALLET
        hasRoute<GuideDestination.Chat>() -> GuideBottomBarDestination.CHAT
        hasRoute<GuideDestination.Profile>() -> GuideBottomBarDestination.PROFILE
        else -> null
    }

private fun GuideBottomBarDestination.toRoute(): Any =
    when (this) {
        GuideBottomBarDestination.HOME -> GuideDestination.Home
        GuideBottomBarDestination.TOURS -> GuideTourDestination.MyTours
        GuideBottomBarDestination.WALLET -> GuideWalletDestination.Wallet
        GuideBottomBarDestination.CHAT -> GuideDestination.Chat
        GuideBottomBarDestination.PROFILE -> GuideDestination.Profile
    }
