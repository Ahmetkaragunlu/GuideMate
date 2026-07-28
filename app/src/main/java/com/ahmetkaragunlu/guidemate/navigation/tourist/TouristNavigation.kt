package com.ahmetkaragunlu.guidemate.navigation.tourist

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
import com.ahmetkaragunlu.guidemate.components.AppBottomBar
import com.ahmetkaragunlu.guidemate.components.AppTopBar
import com.ahmetkaragunlu.guidemate.components.BottomBarItem
import com.ahmetkaragunlu.guidemate.domain.model.UserRole
import com.ahmetkaragunlu.guidemate.navigation.chat.ChatDestination
import com.ahmetkaragunlu.guidemate.navigation.navigateBottomBar
import com.ahmetkaragunlu.guidemate.screens.common.chat.viewmodel.ChatListViewModel
import com.ahmetkaragunlu.guidemate.screens.tourist.home.TouristHomeViewModel
import compose.icons.TablerIcons
import compose.icons.tablericons.Compass
import compose.icons.tablericons.Home
import compose.icons.tablericons.MessageCircle2
import compose.icons.tablericons.Route
import compose.icons.tablericons.User

@Composable
fun TouristNavigation(
    routeNavController: NavController,
    homeViewModel: TouristHomeViewModel = hiltViewModel(),
    chatListViewModel: ChatListViewModel = hiltViewModel(),
) {
    val touristNavController = rememberNavController()
    val navBackStackEntry by touristNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val navigationUiConfig = currentDestination.touristNavigationUiConfig()
    val userName by homeViewModel.userName.collectAsStateWithLifecycle()
    val chatListUiState by chatListViewModel.uiState.collectAsStateWithLifecycle()
    val activeChatId =
        navBackStackEntry
            ?.takeIf { it.destination.hasRoute<ChatDestination.Detail>() }
            ?.toRoute<ChatDestination.Detail>()
            ?.chatId
    val activeChat = chatListUiState.chats.firstOrNull { it.chatId == activeChatId }

    LaunchedEffect(chatListViewModel) {
        chatListViewModel.setViewerRole(UserRole.TOURIST)
    }

    Scaffold(
        topBar = {
            AppTopBar(
                config =
                    navigationUiConfig.topBar.copy(
                        chatTitle = activeChat?.name.orEmpty(),
                        chatAvatarResId = activeChat?.avatarResId ?: R.drawable.example,
                    ),
                userName = userName,
                onBackClick = touristNavController::navigateUp,
                onLogoutClick = {},
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
                chatListViewModel = chatListViewModel,
            )
        }
    }
}

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
