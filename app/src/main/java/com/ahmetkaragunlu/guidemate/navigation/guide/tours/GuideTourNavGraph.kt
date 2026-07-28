package com.ahmetkaragunlu.guidemate.navigation.guide.tours

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ahmetkaragunlu.guidemate.navigation.navigateTo
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step1.GuideTourPublishStep1LocationDateScreen
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step2.GuideTourPublishStep2CategoryPriceScreen
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step3.GuideTourPublishStep3DetailsMediaScreen
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.step4.GuideTourPublishStep4PreviewPublishScreen
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.viewmodel.GuideTourPublishViewModel
import com.ahmetkaragunlu.guidemate.screens.guide.tours.GUIDE_MY_TOURS_SELECTED_TAB_RESULT
import com.ahmetkaragunlu.guidemate.screens.guide.tours.GuideMyToursScreen
import com.ahmetkaragunlu.guidemate.screens.guide.tours.detail.GuideTourDetailScreen
import com.ahmetkaragunlu.guidemate.screens.guide.tours.edit.GuideTourEditScreen
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.GuideTourTab

internal fun NavGraphBuilder.guideTourNavGraph(
    navController: NavController,
    tourPublishViewModel: GuideTourPublishViewModel,
    onBackActionChanged: ((() -> Unit)?) -> Unit,
) {
    composable<GuideTourDestination.MyTours> {
        GuideMyToursScreen(
            onNavigateToTourPublish = {
                tourPublishViewModel.resetDraft()
                navController.navigateTo(GuideTourDestination.PublishStep1)
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
    composable<GuideTourDestination.PublishStep1> {
        val uiState = tourPublishViewModel.uiState.collectAsStateWithLifecycle()
        GuideTourPublishStep1LocationDateScreen(
            uiState = uiState.value,
            onLocationSelected = tourPublishViewModel::onLocationSelected,
            onDateSelected = tourPublishViewModel::onTourDateSelected,
            onStartTimeSelected = tourPublishViewModel::onStartTimeSelected,
            onDurationSelected = tourPublishViewModel::onDurationSelected,
            onNext = {
                if (tourPublishViewModel.validateStep1()) {
                    navController.navigateTo(GuideTourDestination.PublishStep2)
                }
            },
        )
    }
    composable<GuideTourDestination.PublishStep2> {
        val uiState = tourPublishViewModel.uiState.collectAsStateWithLifecycle()
        GuideTourPublishStep2CategoryPriceScreen(
            uiState = uiState.value,
            onCategorySelected = tourPublishViewModel::onCategorySelected,
            onLanguagesSelected = tourPublishViewModel::onLanguagesSelected,
            onRemoveLanguageClick = tourPublishViewModel::onRemoveLanguageClick,
            onPriceChange = tourPublishViewModel::onPriceChange,
            onCapacityChange = tourPublishViewModel::onCapacityChange,
            onNext = {
                if (tourPublishViewModel.validateStep2()) {
                    navController.navigateTo(GuideTourDestination.PublishStep3)
                }
            },
        )
    }
    composable<GuideTourDestination.PublishStep3> {
        val uiState = tourPublishViewModel.uiState.collectAsStateWithLifecycle()
        GuideTourPublishStep3DetailsMediaScreen(
            uiState = uiState.value,
            onTourNameChange = tourPublishViewModel::onTourNameChange,
            onCoverImageSelected = tourPublishViewModel::onCoverImageSelected,
            onDescriptionChange = tourPublishViewModel::onTourDescriptionChange,
            onMeetingPointChange = tourPublishViewModel::onMeetingPointChange,
            onNext = {
                if (tourPublishViewModel.validateStep3()) {
                    navController.navigateTo(GuideTourDestination.PublishStep4)
                }
            },
        )
    }
    composable<GuideTourDestination.PublishStep4> {
        val uiState = tourPublishViewModel.uiState.collectAsStateWithLifecycle()
        GuideTourPublishStep4PreviewPublishScreen(
            uiState = uiState.value,
            onPublish = {
                if (tourPublishViewModel.onPublishClick()) {
                    navController
                        .getBackStackEntry(GuideTourDestination.MyTours)
                        .savedStateHandle[GUIDE_MY_TOURS_SELECTED_TAB_RESULT] =
                        GuideTourTab.REVIEW.name
                    navController.popBackStack(
                        route = GuideTourDestination.MyTours,
                        inclusive = false,
                    )
                }
            },
        )
    }
}
