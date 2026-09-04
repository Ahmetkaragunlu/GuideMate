package com.ahmetkaragunlu.guidemate.navigation.guide.tours

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ahmetkaragunlu.guidemate.navigation.navigateTo
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.GuideMyToursScreen
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.detail.GuideTourDetailScreen
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.edit.GuideTourEditScreen
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.model.GuideTourTab

internal fun NavGraphBuilder.guideTourNavGraph(
    navController: NavController,
    onBackActionChanged: ((() -> Unit)?) -> Unit,
) {
    composable<GuideTourDestination.MyTours> { backStackEntry ->
        val requestedTabName by
            backStackEntry.savedStateHandle
                .getStateFlow(GUIDE_MY_TOURS_SELECTED_TAB_RESULT, "")
                .collectAsStateWithLifecycle()
        GuideMyToursScreen(
            onNavigateToTourPublish = {
                navController.navigateTo(GuideTourPublishGraph)
            },
            onNavigateToTourDetail = { tourId, sessionId ->
                navController.navigateTo(GuideTourDestination.Detail(tourId, sessionId))
            },
            onNavigateToTourEdit = { tourId, sessionId ->
                navController.navigateTo(GuideTourDestination.Edit(tourId, sessionId))
            },
            requestedTab =
                GuideTourTab.entries.firstOrNull { it.name == requestedTabName },
            onRequestedTabConsumed = {
                backStackEntry.savedStateHandle[GUIDE_MY_TOURS_SELECTED_TAB_RESULT] = ""
            },
        )
    }
    composable<GuideTourDestination.Detail> {
        GuideTourDetailScreen(
            onFinished = { targetTab ->
                navController.returnToMyTours(targetTab)
            },
        )
    }
    composable<GuideTourDestination.Edit> {
        GuideTourEditScreen(
            onSaved = { targetTab ->
                navController.returnToMyTours(targetTab)
            },
            onNavigateBack = navController::navigateUp,
            onBackActionChanged = onBackActionChanged,
        )
    }

    guideTourPublishNavGraph(navController = navController)
}

internal const val GUIDE_MY_TOURS_SELECTED_TAB_RESULT = "guideMyToursSelectedTab"

internal fun NavController.returnToMyTours(targetTab: GuideTourTab) {
    val existingEntry =
        runCatching { getBackStackEntry(GuideTourDestination.MyTours) }.getOrNull()
    if (existingEntry != null) {
        existingEntry.savedStateHandle[GUIDE_MY_TOURS_SELECTED_TAB_RESULT] = targetTab.name
        popBackStack(route = GuideTourDestination.MyTours, inclusive = false)
        return
    }

    navigateTo(GuideTourDestination.MyTours)
    getBackStackEntry(GuideTourDestination.MyTours)
        .savedStateHandle[GUIDE_MY_TOURS_SELECTED_TAB_RESULT] = targetTab.name
}
