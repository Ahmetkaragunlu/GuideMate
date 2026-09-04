package com.ahmetkaragunlu.guidemate.navigation.tourist

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.ahmetkaragunlu.guidemate.navigation.navigateBottomBar
import com.ahmetkaragunlu.guidemate.navigation.navigateTo
import com.ahmetkaragunlu.guidemate.navigation.notification.toTouristDestination
import com.ahmetkaragunlu.guidemate.navigation.notification.marksNotificationAfterSuccessfulLoad
import com.ahmetkaragunlu.guidemate.chat.presentation.viewmodel.ChatListViewModel
import com.ahmetkaragunlu.guidemate.discovery.presentation.tourist.TouristExploreViewModel
import com.ahmetkaragunlu.guidemate.home.presentation.tourist.TouristHomeViewModel
import com.ahmetkaragunlu.guidemate.navigation.tourist.payment.TouristPaymentDestination
import com.ahmetkaragunlu.guidemate.payment.presentation.recovery.PaymentRecoveryViewModel
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationNavigationTarget
import com.ahmetkaragunlu.guidemate.notification.presentation.NotificationViewModel
import com.ahmetkaragunlu.guidemate.notification.presentation.NotificationSyncEffect
import com.ahmetkaragunlu.guidemate.notification.presentation.components.NotificationBottomSheet
import com.ahmetkaragunlu.guidemate.notification.presentation.permission.NotificationPermissionEffect
import compose.icons.TablerIcons
import compose.icons.tablericons.Compass
import compose.icons.tablericons.Home
import compose.icons.tablericons.MessageCircle2
import compose.icons.tablericons.Route
import compose.icons.tablericons.User

@Composable
fun TouristNavigation(
    routeNavController: NavController,
    onLogoutClick: () -> Unit,
    pendingNotificationTarget: NotificationNavigationTarget?,
    onNotificationNavigationHandled: (NotificationNavigationTarget) -> Unit,
    homeViewModel: TouristHomeViewModel = hiltViewModel(),
    chatListViewModel: ChatListViewModel = hiltViewModel(),
    paymentRecoveryViewModel: PaymentRecoveryViewModel = hiltViewModel(),
    notificationViewModel: NotificationViewModel = hiltViewModel(),
) {
    NotificationPermissionEffect()
    NotificationSyncEffect(notificationViewModel::refresh)
    val touristNavController = rememberNavController()
    val context = LocalContext.current
    val navBackStackEntry by touristNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val filterExploreViewModel =
        if (currentDestination?.hasRoute<TouristDestination.Filter>() == true) {
            touristNavController.previousBackStackEntry?.let { backStackEntry ->
                hiltViewModel<TouristExploreViewModel>(backStackEntry)
            }
        } else {
            null
        }
    val navigationUiConfig = currentDestination.touristNavigationUiConfig()
    val userName by homeViewModel.userName.collectAsStateWithLifecycle()
    val chatListUiState by chatListViewModel.uiState.collectAsStateWithLifecycle()
    val notificationUiState by notificationViewModel.uiState.collectAsStateWithLifecycle()
    val pendingPaymentId by
        paymentRecoveryViewModel.pendingPaymentId.collectAsStateWithLifecycle()
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
            val destination = target.toTouristDestination()
            if (!destination.marksNotificationAfterSuccessfulLoad()) {
                target.notificationId?.let(notificationViewModel::markRead)
            }
            touristNavController.navigateTo(destination)
            onNotificationNavigationHandled(target)
        }
    }

    LaunchedEffect(notificationUiState.errorMessage) {
        notificationUiState.errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            notificationViewModel.onMessageShown()
        }
    }

    LaunchedEffect(pendingPaymentId, currentDestination?.route) {
        val paymentId = pendingPaymentId ?: return@LaunchedEffect
        if (!currentDestination.isPaymentFlowDestination()) {
            paymentRecoveryViewModel.onRecoveryNavigationHandled()
            touristNavController.navigateTo(
                TouristPaymentDestination.Status(paymentId = paymentId),
            )
        }
    }

    Box {
        Scaffold(
            topBar = {
                AppTopBar(
                config =
                    navigationUiConfig.topBar.copy(
                        chatTitle = activeChat?.name.orEmpty(),
                        chatAvatarResId =
                            activeChat?.avatarResId ?: R.drawable.ic_default_avatar,
                        chatAvatarUrl = activeChat?.avatarUrl,
                    ),
                userName = userName,
                    onBackClick = {
                        val backAction = customBackAction
                        if (backAction != null) {
                            backAction()
                        } else {
                            if (currentDestination?.hasRoute<TouristDestination.Filter>() == true) {
                                filterExploreViewModel?.cancelFilterEditing()
                            }
                            touristNavController.navigateUp()
                        }
                    },
                    onLogoutClick = onLogoutClick,
                    unreadNotificationCount = notificationUiState.unreadCount,
                    onNotificationClick = { showNotifications = true },
                    onChatProfileClick =
                        activeChat?.let { chat ->
                            {
                                touristNavController.navigateTo(
                                    TouristDestination.GuideProfile(chat.remoteUserId),
                                )
                            }
                        },
                )
            },
            bottomBar = {
                if (navigationUiConfig.showBottomBar) {
                    val selectedDestination = currentDestination.touristBottomBarDestination()
                    AppBottomBar(
                        selectedDestination = selectedDestination,
                        items = touristBottomBarItems,
                        badgeCounts =
                            mapOf(
                                TouristBottomBarDestination.CHAT to
                                    chatListUiState.totalUnreadCount,
                            ),
                        onDestinationClick = { destination ->
                            touristNavController.navigateBottomBar(
                                destination = destination.toRoute(),
                                startDestination = TouristDestination.Home,
                            )
                        },
                    )
                }
            },
        ) { innerPadding ->
            NavHost(
                navController = touristNavController,
                startDestination = TouristDestination.Home,
                modifier = Modifier.padding(innerPadding),
            ) {
                touristNavGraph(
                    touristNavController = touristNavController,
                    routeNavController = routeNavController,
                    homeViewModel = homeViewModel,
                    chatListViewModel = chatListViewModel,
                    onBackActionChanged = { customBackAction = it },
                )
            }
        }

        NotificationBottomSheet(
            isVisible = showNotifications,
            uiState = notificationUiState,
            onDismiss = { showNotifications = false },
            onNotificationClick = { target ->
                showNotifications = false
                val destination = target.toTouristDestination()
                if (!destination.marksNotificationAfterSuccessfulLoad()) {
                    target.notificationId?.let(notificationViewModel::markRead)
                }
                touristNavController.navigateTo(destination)
            },
            onMarkAllRead = notificationViewModel::markAllRead,
            onRefresh = notificationViewModel::refresh,
            onLoadMore = notificationViewModel::loadMore,
        )
    }
}

private fun NavDestination?.isPaymentFlowDestination(): Boolean =
    this != null &&
        (hasRoute<TouristPaymentDestination.Checkout>() ||
            hasRoute<TouristPaymentDestination.Hosted>() ||
            hasRoute<TouristPaymentDestination.Status>() ||
            hasRoute<TouristPaymentDestination.Success>())

private enum class TouristBottomBarDestination {
    HOME,
    EXPLORE,
    TRIPS,
    CHAT,
    PROFILE,
}

private val touristBottomBarItems =
    listOf(
        BottomBarItem(
            label = R.string.tourist_home,
            icon = TablerIcons.Home,
            destination = TouristBottomBarDestination.HOME,
        ),
        BottomBarItem(
            label = R.string.tourist_explore,
            icon = TablerIcons.Compass,
            destination = TouristBottomBarDestination.EXPLORE,
        ),
        BottomBarItem(
            label = R.string.tourist_trips,
            icon = TablerIcons.Route,
            destination = TouristBottomBarDestination.TRIPS,
        ),
        BottomBarItem(
            label = R.string.tourist_chat,
            icon = TablerIcons.MessageCircle2,
            destination = TouristBottomBarDestination.CHAT,
        ),
        BottomBarItem(
            label = R.string.tourist_profile,
            icon = TablerIcons.User,
            destination = TouristBottomBarDestination.PROFILE,
        ),
    )

private fun NavDestination?.touristBottomBarDestination(): TouristBottomBarDestination? =
    when {
        this == null || hasRoute<TouristDestination.Home>() -> TouristBottomBarDestination.HOME
        hasRoute<TouristDestination.Explore>() -> TouristBottomBarDestination.EXPLORE
        hasRoute<TouristDestination.Trips>() -> TouristBottomBarDestination.TRIPS
        hasRoute<TouristDestination.Chat>() -> TouristBottomBarDestination.CHAT
        hasRoute<TouristDestination.Profile>() -> TouristBottomBarDestination.PROFILE
        else -> null
    }

private fun TouristBottomBarDestination.toRoute(): Any =
    when (this) {
        TouristBottomBarDestination.HOME -> TouristDestination.Home
        TouristBottomBarDestination.EXPLORE -> TouristDestination.Explore
        TouristBottomBarDestination.TRIPS -> TouristDestination.Trips
        TouristBottomBarDestination.CHAT -> TouristDestination.Chat
        TouristBottomBarDestination.PROFILE -> TouristDestination.Profile
    }
