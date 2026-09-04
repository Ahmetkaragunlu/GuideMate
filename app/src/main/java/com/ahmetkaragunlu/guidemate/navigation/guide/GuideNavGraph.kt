package com.ahmetkaragunlu.guidemate.navigation.guide

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ahmetkaragunlu.guidemate.navigation.RootDestination
import com.ahmetkaragunlu.guidemate.navigation.chat.ChatDestination
import com.ahmetkaragunlu.guidemate.navigation.guide.account.GuideAccountStart
import com.ahmetkaragunlu.guidemate.navigation.guide.wallet.GuideWalletDestination
import com.ahmetkaragunlu.guidemate.navigation.guide.wallet.guideWalletNavGraph
import com.ahmetkaragunlu.guidemate.navigation.guide.tours.guideTourNavGraph
import com.ahmetkaragunlu.guidemate.navigation.navigateTo
import com.ahmetkaragunlu.guidemate.chat.presentation.ChatDetailScreen
import com.ahmetkaragunlu.guidemate.chat.presentation.ChatListScreen
import com.ahmetkaragunlu.guidemate.chat.presentation.viewmodel.ChatListViewModel
import com.ahmetkaragunlu.guidemate.wallet.presentation.guide.earnings.GuideEarningsViewModel
import com.ahmetkaragunlu.guidemate.home.presentation.guide.GuideHomeScreen
import com.ahmetkaragunlu.guidemate.home.presentation.guide.GuideHomeViewModel
import com.ahmetkaragunlu.guidemate.notification.presentation.model.NotificationUiState
import com.ahmetkaragunlu.guidemate.profile.presentation.guide.GuideProfileScreen
import com.ahmetkaragunlu.guidemate.profile.presentation.guide.model.GuideProfileMenuTarget
import com.ahmetkaragunlu.guidemate.profile.presentation.guide.preview.GuideProfilePreviewScreen
import kotlinx.coroutines.flow.StateFlow

internal fun NavGraphBuilder.guideNavGraph(
    guideNavController: NavController,
    routeNavController: NavController,
    homeViewModel: GuideHomeViewModel,
    earningsViewModel: GuideEarningsViewModel,
    notificationState: StateFlow<NotificationUiState>,
    chatListViewModel: ChatListViewModel,
    onBackActionChanged: ((() -> Unit)?) -> Unit,
) {
    composable<GuideDestination.Home> {
        val homeUiState by homeViewModel.uiState.collectAsStateWithLifecycle()
        val notificationUiState by notificationState.collectAsStateWithLifecycle()
        GuideHomeScreen(
            uiState = homeUiState,
            recentNotifications = notificationUiState.recentNotifications,
            onNavigateToEarnings = {
                guideNavController.navigateTo(GuideWalletDestination.Earnings)
            },
            onRetryPerformance = homeViewModel::refreshDashboard,
        )
    }
    composable<GuideDestination.Chat> {
        val chatListUiState by chatListViewModel.uiState.collectAsStateWithLifecycle()
        ChatListScreen(
            uiState = chatListUiState,
            onNavigateToDetail = { chatId ->
                guideNavController.navigateTo(ChatDestination.Detail(chatId))
            },
            onRetry = chatListViewModel::refresh,
        )
    }
    composable<ChatDestination.Detail> {
        ChatDetailScreen()
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
        onBackActionChanged = onBackActionChanged,
    )
    guideWalletNavGraph(
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
