package com.ahmetkaragunlu.guidemate.navigation.guide

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ahmetkaragunlu.guidemate.components.AppBottomBar
import com.ahmetkaragunlu.guidemate.components.GuideTopBar
import com.ahmetkaragunlu.guidemate.navigation.graph.Graph
import com.ahmetkaragunlu.guidemate.navigation.guideNavItems
import com.ahmetkaragunlu.guidemate.navigation.navigateTo
import com.ahmetkaragunlu.guidemate.screens.guide.chat.GuideChatDetailScreen
import com.ahmetkaragunlu.guidemate.screens.guide.chat.GuideChatScreen
import com.ahmetkaragunlu.guidemate.screens.guide.earnings.GuideEarningsScreen
import com.ahmetkaragunlu.guidemate.screens.guide.earnings.model.GuideEarningsUiState
import com.ahmetkaragunlu.guidemate.screens.guide.earnings.viewmodel.GuideEarningsViewModel
import com.ahmetkaragunlu.guidemate.screens.guide.home.GuideHomeScreen
import com.ahmetkaragunlu.guidemate.screens.guide.home.GuideHomeViewModel
import com.ahmetkaragunlu.guidemate.screens.guide.home.model.GuideHomeUiState
import com.ahmetkaragunlu.guidemate.screens.guide.profile.GuideProfileScreen
import com.ahmetkaragunlu.guidemate.screens.guide.profile.preview.GuideProfilePreviewScreen
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step1.GuideTourPublishStep1LocationDateScreen
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step2.GuideTourPublishStep2CategoryPriceScreen
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step3.GuideTourPublishStep3DetailsMediaScreen
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step4.GuideTourPublishStep4PreviewPublishScreen
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.viewmodel.GuideTourPublishViewModel
import com.ahmetkaragunlu.guidemate.screens.guide.tours.GuideMyToursScreen
import com.ahmetkaragunlu.guidemate.screens.guide.wallet.GuideMyWalletScreen

fun NavGraphBuilder.guideNavGraph(
    guideNavController: NavController,
    routeNavController: NavController,
    guideHomeUiState: GuideHomeUiState,
    guideEarningsUiState: GuideEarningsUiState,
    tourPublishViewModel: GuideTourPublishViewModel,
) {
    composable(route = GuideRoute.GuideHomeScreen.route) {
        GuideHomeScreen(
            uiState = guideHomeUiState,
            currentMonthEarning = guideEarningsUiState.currentMonth,
            onNavigateToEarnings = {
                guideNavController.navigateTo(GuideRoute.GuideEarningsScreen.route)
            },
        )
    }
    composable(route = GuideRoute.GuideEarningsScreen.route) {
        GuideEarningsScreen(uiState = guideEarningsUiState)
    }
    composable(route = GuideRoute.GuideMyToursScreen.route) {
        GuideMyToursScreen(
            onNavigateToTourPublish = {
                tourPublishViewModel.resetDraft()
                guideNavController.navigateTo(GuideRoute.GuideTourPublishStep1Screen.route)
            },
        )
    }
    composable(route = GuideRoute.GuideTourPublishStep1Screen.route) {
        val uiState by tourPublishViewModel.uiState.collectAsStateWithLifecycle()
        GuideTourPublishStep1LocationDateScreen(
            uiState = uiState,
            onLocationClick = { },
            onDateClick = { },
            onNext = { guideNavController.navigateTo(GuideRoute.GuideTourPublishStep2Screen.route) },
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
            onNext = { guideNavController.navigateTo(GuideRoute.GuideTourPublishStep3Screen.route) },
        )
    }
    composable(route = GuideRoute.GuideTourPublishStep3Screen.route) {
        val uiState by tourPublishViewModel.uiState.collectAsStateWithLifecycle()
        GuideTourPublishStep3DetailsMediaScreen(
            uiState = uiState,
            onTourNameChange = tourPublishViewModel::onTourNameChange,
            onUploadPhotosClick = { },
            onDescriptionChange = tourPublishViewModel::onTourDescriptionChange,
            onMeetingPointChange = tourPublishViewModel::onMeetingPointChange,
            onNext = { guideNavController.navigateTo(GuideRoute.GuideTourPublishStep4Screen.route) },
        )
    }
    composable(route = GuideRoute.GuideTourPublishStep4Screen.route) {
        val uiState by tourPublishViewModel.uiState.collectAsStateWithLifecycle()
        GuideTourPublishStep4PreviewPublishScreen(
            uiState = uiState,
            onPublish = tourPublishViewModel::onPublishClick,
        )
    }
    composable(route = GuideRoute.GuideMyWalletScreen.route) {
        GuideMyWalletScreen(
            earnings = guideEarningsUiState.allEarnings,
            onNavigateToEarnings = {
                guideNavController.navigateTo(GuideRoute.GuideEarningsScreen.route)
            },
        )
    }
    composable(route = GuideRoute.GuideChatScreen.route) {
        GuideChatScreen(
            onNavigateToDetail = { chatId ->
                guideNavController.navigateTo(GuideRoute.GuideChatDetailScreen.route)
            },
        )
    }

    composable(route = GuideRoute.GuideChatDetailScreen.route) {
        GuideChatDetailScreen()
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
    tourPublishViewModel: GuideTourPublishViewModel = hiltViewModel(),
) {
    val guideNavController = rememberNavController()
    val navBackStackEntry by guideNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: GuideRoute.GuideHomeScreen.route
    val getUserName by homeViewModel.userName.collectAsStateWithLifecycle()
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val earningsUiState by earningsViewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            GuideTopBar(
                currentRoute = currentRoute,
                navController = guideNavController,
                userName = getUserName,
            )
        },
        bottomBar = {
            if (currentRoute.shouldShowGuideBottomBar()) {
                AppBottomBar(
                    navController = guideNavController,
                    currentRoute = currentRoute,
                    items = guideNavItems,
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
                guideHomeUiState = uiState,
                guideEarningsUiState = earningsUiState,
                tourPublishViewModel = tourPublishViewModel,
            )
        }
    }
}
