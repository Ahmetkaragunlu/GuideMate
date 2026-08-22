package com.ahmetkaragunlu.guidemate.navigation.tourist

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.ahmetkaragunlu.guidemate.auth.domain.model.UserRole
import com.ahmetkaragunlu.guidemate.navigation.RootDestination
import com.ahmetkaragunlu.guidemate.navigation.chat.ChatDestination
import com.ahmetkaragunlu.guidemate.navigation.navigateTo
import com.ahmetkaragunlu.guidemate.navigation.tourist.account.TouristAccountStart
import com.ahmetkaragunlu.guidemate.navigation.tourist.payment.TouristPaymentDestination
import com.ahmetkaragunlu.guidemate.navigation.tourist.payment.touristPaymentNavGraph
import com.ahmetkaragunlu.guidemate.chat.presentation.ChatDetailScreen
import com.ahmetkaragunlu.guidemate.chat.presentation.ChatListScreen
import com.ahmetkaragunlu.guidemate.chat.presentation.viewmodel.ChatListViewModel
import com.ahmetkaragunlu.guidemate.discovery.presentation.tourist.TouristExploreScreen
import com.ahmetkaragunlu.guidemate.discovery.presentation.tourist.TouristFilterScreen
import com.ahmetkaragunlu.guidemate.home.presentation.tourist.TouristHomeScreen
import com.ahmetkaragunlu.guidemate.home.presentation.tourist.TouristHomeViewModel
import com.ahmetkaragunlu.guidemate.profile.presentation.tourist.TouristProfileScreen
import com.ahmetkaragunlu.guidemate.profile.presentation.publicprofile.GuidePublicProfileScreen
import com.ahmetkaragunlu.guidemate.profile.presentation.tourist.model.TouristProfileMenuTarget
import com.ahmetkaragunlu.guidemate.reservation.presentation.trips.TouristTripsScreen
import com.ahmetkaragunlu.guidemate.tour.presentation.tourist.detail.TouristTourDetailScreen

internal fun NavGraphBuilder.touristNavGraph(
    touristNavController: NavController,
    routeNavController: NavController,
    homeViewModel: TouristHomeViewModel,
    chatListViewModel: ChatListViewModel,
) {
    composable<TouristDestination.Home> {
        TouristHomeScreen(
            viewModel = homeViewModel,
            onNavigateToTourDetail = { sessionId ->
                touristNavController.navigateTo(TouristDestination.TourDetail(sessionId))
            },
            onNavigateToGuideProfile = { guideId ->
                touristNavController.navigateTo(TouristDestination.GuideProfile(guideId))
            },
        )
    }
    composable<TouristDestination.Explore> {
        TouristExploreScreen(
            onNavigateToFilter = {
                touristNavController.navigateTo(TouristDestination.Filter)
            },
            onNavigateToGuideProfile = { guideId ->
                touristNavController.navigateTo(TouristDestination.GuideProfile(guideId))
            },
        )
    }
    composable<TouristDestination.Trips> {
        TouristTripsScreen(
            onNavigateToTourDetail = { sessionId ->
                touristNavController.navigateTo(TouristDestination.TourDetail(sessionId))
            },
        )
    }
    composable<TouristDestination.Chat> {
        val chatListUiState = chatListViewModel.uiState.collectAsStateWithLifecycle()
        ChatListScreen(
            uiState = chatListUiState.value,
            onNavigateToDetail = { chatId ->
                touristNavController.navigateTo(ChatDestination.Detail(chatId))
            },
        )
    }
    composable<TouristDestination.Profile> {
        TouristProfileScreen(
            onNavigateToAccount = { target ->
                routeNavController.navigateTo(
                    RootDestination.TouristAccount(target.toTouristAccountStart()),
                )
            },
            onNavigateToWallet = {
                touristNavController.navigateTo(TouristPaymentDestination.Wallet)
            },
        )
    }
    composable<TouristDestination.Filter> {
        TouristFilterScreen()
    }
    composable<ChatDestination.Detail> {
        ChatDetailScreen(viewerRole = UserRole.TOURIST)
    }
    composable<TouristDestination.TourDetail> {
        TouristTourDetailScreen(
            onBookTour = { sessionId ->
                touristNavController.navigateTo(TouristPaymentDestination.Checkout(sessionId))
            },
        )
    }
    composable<TouristDestination.GuideProfile> { backStackEntry ->
        val destination = backStackEntry.toRoute<TouristDestination.GuideProfile>()
        GuidePublicProfileScreen(guideId = destination.guideId)
    }

    touristPaymentNavGraph(
        touristNavController = touristNavController,
        routeNavController = routeNavController,
    )
}

private fun TouristProfileMenuTarget.toTouristAccountStart(): TouristAccountStart =
    when (this) {
        TouristProfileMenuTarget.SAVED_CARDS -> TouristAccountStart.SAVED_CARDS
        TouristProfileMenuTarget.CHANGE_PASSWORD -> TouristAccountStart.CHANGE_PASSWORD
        TouristProfileMenuTarget.NOTIFICATION_SETTINGS ->
            TouristAccountStart.NOTIFICATION_SETTINGS
        TouristProfileMenuTarget.LEGAL_AGREEMENTS -> TouristAccountStart.LEGAL_AGREEMENTS
        TouristProfileMenuTarget.HELP_SUPPORT -> TouristAccountStart.HELP_SUPPORT
    }
