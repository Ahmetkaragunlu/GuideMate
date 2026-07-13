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
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.components.AppBottomBar
import com.ahmetkaragunlu.guidemate.components.TouristAppBar
import com.ahmetkaragunlu.guidemate.domain.model.UserRole
import com.ahmetkaragunlu.guidemate.navigation.chat.CHAT_ID_ARGUMENT
import com.ahmetkaragunlu.guidemate.navigation.graph.Graph
import com.ahmetkaragunlu.guidemate.navigation.navigateTo
import com.ahmetkaragunlu.guidemate.navigation.touristNavItems
import com.ahmetkaragunlu.guidemate.screens.common.chat.ChatDetailScreen
import com.ahmetkaragunlu.guidemate.screens.common.chat.ChatListScreen
import com.ahmetkaragunlu.guidemate.screens.common.chat.viewmodel.ChatListViewModel
import com.ahmetkaragunlu.guidemate.screens.tourist.explore.TouristExploreScreen
import com.ahmetkaragunlu.guidemate.screens.tourist.explore.TouristFilterScreen
import com.ahmetkaragunlu.guidemate.screens.tourist.home.TouristHomeScreen
import com.ahmetkaragunlu.guidemate.screens.tourist.home.TouristHomeViewModel
import com.ahmetkaragunlu.guidemate.screens.tourist.profile.TouristProfileScreen
import com.ahmetkaragunlu.guidemate.screens.tourist.trips.TouristTripsScreen

fun NavGraphBuilder.touristNavGraph(
    touristNavController: NavController,
    routeNavController: NavController,
    chatListViewModel: ChatListViewModel,
) {
    composable(route = TouristRoute.TouristHomeScreen.route) {
        TouristHomeScreen()
    }
    composable(route = TouristRoute.TouristExploreScreen.route) {
        TouristExploreScreen(
            onNavigateToFilter = {
                touristNavController.navigateTo(TouristRoute.TouristFilterScreen.route)
            },
        )
    }
    composable(route = TouristRoute.TouristMyTripsScreen.route) {
        TouristTripsScreen()
    }
    composable(route = TouristRoute.TouristChatScreen.route) {
        val chatListUiState by chatListViewModel.uiState.collectAsStateWithLifecycle()
        ChatListScreen(
            uiState = chatListUiState,
            onNavigateToDetail = { chatId ->
                touristNavController.navigateTo(touristChatDetailRoute(chatId))
            },
        )
    }

    composable(route = TouristRoute.TouristProfileScreen.route) {
        TouristProfileScreen(
            onNavigateToAccount = { targetRoute ->
                routeNavController.navigateTo("${Graph.AccountGraph.route}/${targetRoute.route}")
            },
        )
    }
    composable(route = TouristRoute.TouristFilterScreen.route) {
        TouristFilterScreen()
    }
    composable(
        route = TOURIST_CHAT_DETAIL_ROUTE_PATTERN,
        arguments = listOf(navArgument(CHAT_ID_ARGUMENT) { type = NavType.StringType }),
    ) {
        ChatDetailScreen(viewerRole = UserRole.TOURIST)
    }
}

@Composable
fun TouristNavGraphScaffold(
    viewModel: TouristHomeViewModel = hiltViewModel(),
    chatListViewModel: ChatListViewModel = hiltViewModel(),
    routeNavController: NavController,
) {
    val touristNavController = rememberNavController()
    val navBackStackEntry by touristNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: TouristRoute.TouristHomeScreen.route
    val getUserName by viewModel.userName.collectAsStateWithLifecycle()
    val chatListUiState by chatListViewModel.uiState.collectAsStateWithLifecycle()
    val activeChatId = navBackStackEntry?.arguments?.getString(CHAT_ID_ARGUMENT)
    val activeChat = chatListUiState.chats.firstOrNull { it.chatId == activeChatId }

    LaunchedEffect(chatListViewModel) {
        chatListViewModel.setViewerRole(UserRole.TOURIST)
    }

    Scaffold(
        topBar = {
            TouristAppBar(
                currentRoute = currentRoute,
                navController = touristNavController,
                userName = getUserName,
                chatTitle = activeChat?.name.orEmpty(),
                chatAvatarResId = activeChat?.avatarResId ?: R.drawable.example,
            )
        },
        bottomBar = {
            AppBottomBar(
                navController = touristNavController,
                currentRoute = currentRoute,
                items = touristNavItems,
                badgeCounts =
                    mapOf(
                        TouristRoute.TouristChatScreen.route to chatListUiState.totalUnreadCount,
                    ),
            )
        },
    ) { innerPadding ->
        NavHost(
            navController = touristNavController,
            startDestination = TouristRoute.TouristHomeScreen.route,
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
