package com.ahmetkaragunlu.guidemate.navigation.guide.tours

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ahmetkaragunlu.guidemate.navigation.navigateTo
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.GUIDE_MY_TOURS_SELECTED_TAB_RESULT
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.GuideMyToursScreen
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.detail.GuideTourDetailScreen
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.edit.GuideTourEditScreen

internal fun NavGraphBuilder.guideTourNavGraph(
    navController: NavController,
    onBackActionChanged: ((() -> Unit)?) -> Unit,
) {
    composable<GuideTourDestination.MyTours> {
        GuideMyToursScreen(
            onNavigateToTourPublish = {
                navController.navigateTo(GuideTourPublishGraph)
            },
            onNavigateToTourDetail = { sessionId ->
                navController.navigateTo(GuideTourDestination.Detail(sessionId))
            },
            onNavigateToTourEdit = { sessionId ->
                navController.navigateTo(GuideTourDestination.Edit(sessionId))
            },
        )
    }
    composable<GuideTourDestination.Detail> {
        GuideTourDetailScreen(
            onFinished = { targetTab ->
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    GUIDE_MY_TOURS_SELECTED_TAB_RESULT,
                    targetTab.name,
                )
                navController.navigateUp()
            },
        )
    }
    composable<GuideTourDestination.Edit> {
        GuideTourEditScreen(
            onSaved = { targetTab ->
                navController
                    .getBackStackEntry(GuideTourDestination.MyTours)
                    .savedStateHandle[GUIDE_MY_TOURS_SELECTED_TAB_RESULT] = targetTab.name
                navController.popBackStack(
                    route = GuideTourDestination.MyTours,
                    inclusive = false,
                )
            },
            onNavigateBack = navController::navigateUp,
            onBackActionChanged = onBackActionChanged,
        )
    }

    guideTourPublishNavGraph(navController = navController)
}
