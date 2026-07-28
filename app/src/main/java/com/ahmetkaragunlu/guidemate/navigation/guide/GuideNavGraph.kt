package com.ahmetkaragunlu.guidemate.navigation.guide

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ahmetkaragunlu.guidemate.domain.model.UserRole
import com.ahmetkaragunlu.guidemate.navigation.RootDestination
import com.ahmetkaragunlu.guidemate.navigation.chat.ChatDestination
import com.ahmetkaragunlu.guidemate.navigation.guide.account.GuideAccountStart
import com.ahmetkaragunlu.guidemate.navigation.guide.finance.GuideFinanceDestination
import com.ahmetkaragunlu.guidemate.navigation.guide.finance.guideFinanceNavGraph
import com.ahmetkaragunlu.guidemate.navigation.guide.tours.guideTourNavGraph
import com.ahmetkaragunlu.guidemate.navigation.navigateTo
import com.ahmetkaragunlu.guidemate.screens.common.chat.ChatDetailScreen
import com.ahmetkaragunlu.guidemate.screens.common.chat.ChatListScreen
import com.ahmetkaragunlu.guidemate.screens.common.chat.viewmodel.ChatListViewModel
import com.ahmetkaragunlu.guidemate.screens.guide.earnings.viewmodel.GuideEarningsViewModel
import com.ahmetkaragunlu.guidemate.screens.guide.home.GuideHomeScreen
import com.ahmetkaragunlu.guidemate.screens.guide.home.GuideHomeViewModel
import com.ahmetkaragunlu.guidemate.screens.guide.notifications.viewmodel.GuideNotificationsViewModel
import com.ahmetkaragunlu.guidemate.screens.guide.profile.GuideProfileScreen
import com.ahmetkaragunlu.guidemate.screens.guide.profile.model.GuideProfileMenuTarget
import com.ahmetkaragunlu.guidemate.screens.guide.profile.preview.GuideProfilePreviewScreen
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.viewmodel.GuideTourPublishViewModel

internal fun NavGraphBuilder.guideNavGraph(
    guideNavController: NavController,
    routeNavController: NavController,
    homeViewModel: GuideHomeViewModel,
    earningsViewModel: GuideEarningsViewModel,
    notificationsViewModel: GuideNotificationsViewModel,
    tourPublishViewModel: GuideTourPublishViewModel,
    chatListViewModel: ChatListViewModel,
    onBackActionChanged: ((() -> Unit)?) -> Unit,
) {
    composable<GuideDestination.Home> {
        val homeUiState by homeViewModel.uiState.collectAsStateWithLifecycle()
        val earningsUiState by earningsViewModel.uiState.collectAsStateWithLifecycle()
        val notificationsUiState by notificationsViewModel.uiState.collectAsStateWithLifecycle()
        GuideHomeScreen(
            uiState = homeUiState,
            currentMonthEarning = earningsUiState.currentMonth,
            recentNotifications = notificationsUiState.recentNotifications,
            onNavigateToEarnings = {
                guideNavController.navigateTo(GuideFinanceDestination.Earnings)
            },
        )
    }
    composable<GuideDestination.Chat> {
        val chatListUiState by chatListViewModel.uiState.collectAsStateWithLifecycle()
        ChatListScreen(
            uiState = chatListUiState,
            onNavigateToDetail = { chatId ->
                guideNavController.navigateTo(ChatDestination.Detail(chatId))
            },
        )
    }
    composable<ChatDestination.Detail> {
        ChatDetailScreen(viewerRole = UserRole.GUIDE)
    }
    composable<GuideDestination.Profile> {
        GuideProfileScreen(
            onNavigateToAccount = { target ->
                routeNavController.navigateTo(
                    RootDestination.GuideAccount(target.toGuideAccountStart()),
                )
            },
            onNavigateToProfilePreview = {
                guideNavController.navigateTo(GuideDestination.ProfilePreview)
            },
        )
    }
    composable<GuideDestination.ProfilePreview> {
        GuideProfilePreviewScreen()
    }

    guideTourNavGraph(
        navController = guideNavController,
        tourPublishViewModel = tourPublishViewModel,
        onBackActionChanged = onBackActionChanged,
    )
    guideFinanceNavGraph(
        navController = guideNavController,
        earningsViewModel = earningsViewModel,
    )
}

private fun GuideProfileMenuTarget.toGuideAccountStart(): GuideAccountStart =
    when (this) {
        GuideProfileMenuTarget.BANK_ACCOUNTS -> GuideAccountStart.BANK_ACCOUNTS
        GuideProfileMenuTarget.ABOUT -> GuideAccountStart.ABOUT
        GuideProfileMenuTarget.CHANGE_PASSWORD -> GuideAccountStart.CHANGE_PASSWORD
        GuideProfileMenuTarget.NOTIFICATION_SETTINGS -> GuideAccountStart.NOTIFICATION_SETTINGS
        GuideProfileMenuTarget.LEGAL_AGREEMENTS -> GuideAccountStart.LEGAL_AGREEMENTS
        GuideProfileMenuTarget.HELP_SUPPORT -> GuideAccountStart.HELP_SUPPORT
    }
