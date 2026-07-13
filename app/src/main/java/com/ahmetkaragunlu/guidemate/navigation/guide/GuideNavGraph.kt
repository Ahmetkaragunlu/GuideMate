package com.ahmetkaragunlu.guidemate.navigation.guide

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
import com.ahmetkaragunlu.guidemate.components.GuideTopBar
import com.ahmetkaragunlu.guidemate.domain.model.UserRole
import com.ahmetkaragunlu.guidemate.navigation.chat.CHAT_ID_ARGUMENT
import com.ahmetkaragunlu.guidemate.navigation.graph.Graph
import com.ahmetkaragunlu.guidemate.navigation.guideNavItems
import com.ahmetkaragunlu.guidemate.navigation.navigateTo
import com.ahmetkaragunlu.guidemate.screens.common.chat.ChatDetailScreen
import com.ahmetkaragunlu.guidemate.screens.common.chat.ChatListScreen
import com.ahmetkaragunlu.guidemate.screens.common.chat.viewmodel.ChatListViewModel
import com.ahmetkaragunlu.guidemate.screens.guide.earnings.GuideEarningsScreen
import com.ahmetkaragunlu.guidemate.screens.guide.earnings.viewmodel.GuideEarningsViewModel
import com.ahmetkaragunlu.guidemate.screens.guide.home.GuideHomeScreen
import com.ahmetkaragunlu.guidemate.screens.guide.home.GuideHomeViewModel
import com.ahmetkaragunlu.guidemate.screens.guide.notifications.GuideNotificationsBottomSheet
import com.ahmetkaragunlu.guidemate.screens.guide.notifications.viewmodel.GuideNotificationsViewModel
import com.ahmetkaragunlu.guidemate.screens.guide.profile.GuideProfileScreen
import com.ahmetkaragunlu.guidemate.screens.guide.profile.preview.GuideProfilePreviewScreen
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step1.GuideTourPublishStep1LocationDateScreen
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step2.GuideTourPublishStep2CategoryPriceScreen
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step3.GuideTourPublishStep3DetailsMediaScreen
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step4.GuideTourPublishStep4PreviewPublishScreen
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.viewmodel.GuideTourPublishViewModel
import com.ahmetkaragunlu.guidemate.screens.guide.tours.GuideMyToursScreen
import com.ahmetkaragunlu.guidemate.screens.guide.tours.GUIDE_MY_TOURS_SELECTED_TAB_RESULT
import com.ahmetkaragunlu.guidemate.screens.guide.tours.detail.GuideTourDetailScreen
import com.ahmetkaragunlu.guidemate.screens.guide.tours.edit.GuideTourEditScreen
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.GuideTourTab
import com.ahmetkaragunlu.guidemate.screens.guide.wallet.GuideMyWalletScreen

fun NavGraphBuilder.guideNavGraph(
    guideNavController: NavController,
    routeNavController: NavController,
    homeViewModel: GuideHomeViewModel,
    earningsViewModel: GuideEarningsViewModel,
    notificationsViewModel: GuideNotificationsViewModel,
    tourPublishViewModel: GuideTourPublishViewModel,
    chatListViewModel: ChatListViewModel,
    onBackActionChanged: ((() -> Unit)?) -> Unit,
) {
    composable(route = GuideRoute.GuideHomeScreen.route) {
        val homeUiState by homeViewModel.uiState.collectAsStateWithLifecycle()
        val earningsUiState by earningsViewModel.uiState.collectAsStateWithLifecycle()
        val notificationsUiState by notificationsViewModel.uiState.collectAsStateWithLifecycle()
        GuideHomeScreen(
            uiState = homeUiState,
            currentMonthEarning = earningsUiState.currentMonth,
            recentNotifications = notificationsUiState.recentNotifications,
            onNavigateToEarnings = {
                guideNavController.navigateTo(GuideRoute.GuideEarningsScreen.route)
            },
        )
    }
    composable(route = GuideRoute.GuideEarningsScreen.route) {
        val earningsUiState by earningsViewModel.uiState.collectAsStateWithLifecycle()
        GuideEarningsScreen(uiState = earningsUiState)
    }
    composable(route = GuideRoute.GuideMyToursScreen.route) {
        GuideMyToursScreen(
            onNavigateToTourPublish = {
                tourPublishViewModel.resetDraft()
                guideNavController.navigateTo(GuideRoute.GuideTourPublishStep1Screen.route)
            },
            onNavigateToTourDetail = { sessionId ->
                guideNavController.navigateTo(guideTourDetailRoute(sessionId))
            },
            onNavigateToTourEdit = { sessionId ->
                guideNavController.navigateTo(guideTourEditRoute(sessionId))
            },
        )
    }
    composable(route = GUIDE_TOUR_DETAIL_ROUTE_PATTERN) {
        GuideTourDetailScreen(
            onFinished = { targetTab ->
                guideNavController.previousBackStackEntry?.savedStateHandle?.set(
                    GUIDE_MY_TOURS_SELECTED_TAB_RESULT,
                    targetTab.name,
                )
                guideNavController.navigateUp()
            },
        )
    }
    composable(route = GUIDE_TOUR_EDIT_ROUTE_PATTERN) {
        GuideTourEditScreen(
            onSaved = { targetTab ->
                guideNavController
                    .getBackStackEntry(GuideRoute.GuideMyToursScreen.route)
                    .savedStateHandle[GUIDE_MY_TOURS_SELECTED_TAB_RESULT] = targetTab.name
                guideNavController.popBackStack(
                    route = GuideRoute.GuideMyToursScreen.route,
                    inclusive = false,
                )
            },
            onNavigateBack = guideNavController::navigateUp,
            onBackActionChanged = onBackActionChanged,
        )
    }
    composable(route = GuideRoute.GuideTourPublishStep1Screen.route) {
        val uiState by tourPublishViewModel.uiState.collectAsStateWithLifecycle()
        GuideTourPublishStep1LocationDateScreen(
            uiState = uiState,
            onLocationClick = { },
            onDateSelected = tourPublishViewModel::onTourDateSelected,
            onStartTimeSelected = tourPublishViewModel::onStartTimeSelected,
            onDurationSelected = tourPublishViewModel::onDurationSelected,
            onNext = {
                if (tourPublishViewModel.validateStep1()) {
                    guideNavController.navigateTo(GuideRoute.GuideTourPublishStep2Screen.route)
                }
            },
        )
    }
    composable(route = GuideRoute.GuideTourPublishStep2Screen.route) {
        val uiState by tourPublishViewModel.uiState.collectAsStateWithLifecycle()
        GuideTourPublishStep2CategoryPriceScreen(
            uiState = uiState,
            onCategoryClick = { },
            onAddLanguageClick = { },
            onRemoveLanguageClick = tourPublishViewModel::onRemoveLanguageClick,
            onPriceChange = tourPublishViewModel::onPriceChange,
            onCapacityChange = tourPublishViewModel::onCapacityChange,
            onNext = {
                if (tourPublishViewModel.validateStep2()) {
                    guideNavController.navigateTo(GuideRoute.GuideTourPublishStep3Screen.route)
                }
            },
        )
    }
    composable(route = GuideRoute.GuideTourPublishStep3Screen.route) {
        val uiState by tourPublishViewModel.uiState.collectAsStateWithLifecycle()
        GuideTourPublishStep3DetailsMediaScreen(
            uiState = uiState,
            onTourNameChange = tourPublishViewModel::onTourNameChange,
            onCoverImageSelected = tourPublishViewModel::onCoverImageSelected,
            onDescriptionChange = tourPublishViewModel::onTourDescriptionChange,
            onMeetingPointChange = tourPublishViewModel::onMeetingPointChange,
            onNext = {
                if (tourPublishViewModel.validateStep3()) {
                    guideNavController.navigateTo(GuideRoute.GuideTourPublishStep4Screen.route)
                }
            },
        )
    }
    composable(route = GuideRoute.GuideTourPublishStep4Screen.route) {
        val uiState by tourPublishViewModel.uiState.collectAsStateWithLifecycle()
        GuideTourPublishStep4PreviewPublishScreen(
            uiState = uiState,
            onPublish = {
                if (tourPublishViewModel.onPublishClick()) {
                    guideNavController
                        .getBackStackEntry(GuideRoute.GuideMyToursScreen.route)
                        .savedStateHandle[GUIDE_MY_TOURS_SELECTED_TAB_RESULT] =
                        GuideTourTab.REVIEW.name
                    guideNavController.popBackStack(
                        route = GuideRoute.GuideMyToursScreen.route,
                        inclusive = false,
                    )
                }
            },
        )
    }
    composable(route = GuideRoute.GuideMyWalletScreen.route) {
        val earningsUiState by earningsViewModel.uiState.collectAsStateWithLifecycle()
        GuideMyWalletScreen(
            earnings = earningsUiState.allEarnings,
            onNavigateToEarnings = {
                guideNavController.navigateTo(GuideRoute.GuideEarningsScreen.route)
            },
        )
    }
    composable(route = GuideRoute.GuideChatScreen.route) {
        val chatListUiState by chatListViewModel.uiState.collectAsStateWithLifecycle()
        ChatListScreen(
            uiState = chatListUiState,
            onNavigateToDetail = { chatId ->
                guideNavController.navigateTo(guideChatDetailRoute(chatId))
            },
        )
    }

    composable(
        route = GUIDE_CHAT_DETAIL_ROUTE_PATTERN,
        arguments = listOf(navArgument(CHAT_ID_ARGUMENT) { type = NavType.StringType }),
    ) {
        ChatDetailScreen(viewerRole = UserRole.GUIDE)
    }
    composable(route = GuideRoute.GuideProfileScreen.route) {
        GuideProfileScreen(
            onNavigateToAccount = { targetRoute ->
                routeNavController.navigateTo("${Graph.GuideAccountGraph.route}/${targetRoute.route}")
            },
            onNavigateToProfilePreview = {
                guideNavController.navigateTo(GuideRoute.GuideProfilePreviewScreen.route)
            },
        )
    }
    composable(route = GuideRoute.GuideProfilePreviewScreen.route) {
        GuideProfilePreviewScreen()
    }
}

@Composable
fun GuideNavGraphScaffold(
    routeNavController: NavController,
    homeViewModel: GuideHomeViewModel = hiltViewModel(),
    earningsViewModel: GuideEarningsViewModel = hiltViewModel(),
    notificationsViewModel: GuideNotificationsViewModel = hiltViewModel(),
    tourPublishViewModel: GuideTourPublishViewModel = hiltViewModel(),
    chatListViewModel: ChatListViewModel = hiltViewModel(),
) {
    val guideNavController = rememberNavController()
    val navBackStackEntry by guideNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: GuideRoute.GuideHomeScreen.route
    val getUserName by homeViewModel.userName.collectAsStateWithLifecycle()
    val notificationsUiState by notificationsViewModel.uiState.collectAsStateWithLifecycle()
    val chatListUiState by chatListViewModel.uiState.collectAsStateWithLifecycle()
    val activeChatId = navBackStackEntry?.arguments?.getString(CHAT_ID_ARGUMENT)
    val activeChat = chatListUiState.chats.firstOrNull { it.chatId == activeChatId }
    var showNotifications by rememberSaveable { mutableStateOf(false) }
    var customBackAction by remember { mutableStateOf<(() -> Unit)?>(null) }

    LaunchedEffect(chatListViewModel) {
        chatListViewModel.setViewerRole(UserRole.GUIDE)
    }

    Box {
        Scaffold(
            topBar = {
                GuideTopBar(
                    currentRoute = currentRoute,
                    navController = guideNavController,
                    userName = getUserName,
                    unreadNotificationCount = notificationsUiState.unreadCount,
                    onNotificationClick = { showNotifications = true },
                    chatTitle = activeChat?.name.orEmpty(),
                    chatAvatarResId = activeChat?.avatarResId ?: R.drawable.example,
                    onBackClick = { customBackAction?.invoke() ?: guideNavController.navigateUp() },
                )
            },
            bottomBar = {
                if (currentRoute.shouldShowGuideBottomBar()) {
                    AppBottomBar(
                        navController = guideNavController,
                        currentRoute = currentRoute,
                        items = guideNavItems,
                        badgeCounts =
                            mapOf(
                                GuideRoute.GuideChatScreen.route to
                                    chatListUiState.totalUnreadCount,
                            ),
                    )
                }
            },
        ) { innerPadding ->
            NavHost(
                navController = guideNavController,
                startDestination = GuideRoute.GuideHomeScreen.route,
                modifier = Modifier.padding(innerPadding),
            ) {
                guideNavGraph(
                    guideNavController = guideNavController,
                    routeNavController = routeNavController,
                    homeViewModel = homeViewModel,
                    earningsViewModel = earningsViewModel,
                    notificationsViewModel = notificationsViewModel,
                    tourPublishViewModel = tourPublishViewModel,
                    chatListViewModel = chatListViewModel,
                    onBackActionChanged = { customBackAction = it },
                )
            }
        }

        GuideNotificationsBottomSheet(
            isVisible = showNotifications,
            uiState = notificationsUiState,
            onDismiss = { showNotifications = false },
        )
    }
}
