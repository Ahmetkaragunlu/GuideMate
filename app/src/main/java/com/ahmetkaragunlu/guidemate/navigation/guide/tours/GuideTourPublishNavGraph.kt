package com.ahmetkaragunlu.guidemate.navigation.guide.tours

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ahmetkaragunlu.guidemate.navigation.navigateTo
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.step1.GuideTourPublishStep1LocationDateScreen
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.step2.GuideTourPublishStep2CategoryPriceScreen
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.step3.GuideTourPublishStep3DetailsMediaScreen
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.step4.GuideTourPublishStep4PreviewPublishScreen
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.GuideTourPublishViewModel
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.GUIDE_MY_TOURS_SELECTED_TAB_RESULT
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.model.GuideTourTab
import kotlinx.serialization.Serializable

@Serializable
internal data object GuideTourPublishGraph

internal fun NavGraphBuilder.guideTourPublishNavGraph(
    navController: NavController,
) {
    navigation<GuideTourPublishGraph>(
        startDestination = GuideTourDestination.PublishStep1,
    ) {
        composable<GuideTourDestination.PublishStep1> { backStackEntry ->
            val viewModel = backStackEntry.guideTourPublishViewModel(navController)
            val uiState = viewModel.uiState.collectAsStateWithLifecycle()
            GuideTourPublishStep1LocationDateScreen(
                uiState = uiState.value,
                onLocationSelected = viewModel::onLocationSelected,
                onDateSelected = viewModel::onTourDateSelected,
                onStartTimeSelected = viewModel::onStartTimeSelected,
                onDurationSelected = viewModel::onDurationSelected,
                onNext = {
                    if (viewModel.validateStep1()) {
                        navController.navigateTo(GuideTourDestination.PublishStep2)
                    }
                },
            )
        }
        composable<GuideTourDestination.PublishStep2> { backStackEntry ->
            val viewModel = backStackEntry.guideTourPublishViewModel(navController)
            val uiState = viewModel.uiState.collectAsStateWithLifecycle()
            GuideTourPublishStep2CategoryPriceScreen(
                uiState = uiState.value,
                onCategorySelected = viewModel::onCategorySelected,
                onLanguagesSelected = viewModel::onLanguagesSelected,
                onRemoveLanguageClick = viewModel::onRemoveLanguageClick,
                onPriceChange = viewModel::onPriceChange,
                onCapacityChange = viewModel::onCapacityChange,
                onNext = {
                    if (viewModel.validateStep2()) {
                        navController.navigateTo(GuideTourDestination.PublishStep3)
                    }
                },
            )
        }
        composable<GuideTourDestination.PublishStep3> { backStackEntry ->
            val viewModel = backStackEntry.guideTourPublishViewModel(navController)
            val uiState = viewModel.uiState.collectAsStateWithLifecycle()
            GuideTourPublishStep3DetailsMediaScreen(
                uiState = uiState.value,
                onTourNameChange = viewModel::onTourNameChange,
                onCoverImageSelected = viewModel::onCoverImageSelected,
                onDescriptionChange = viewModel::onTourDescriptionChange,
                onMeetingPointChange = viewModel::onMeetingPointChange,
                onNext = {
                    if (viewModel.validateStep3()) {
                        navController.navigateTo(GuideTourDestination.PublishStep4)
                    }
                },
            )
        }
        composable<GuideTourDestination.PublishStep4> { backStackEntry ->
            val viewModel = backStackEntry.guideTourPublishViewModel(navController)
            val uiState = viewModel.uiState.collectAsStateWithLifecycle()
            LaunchedEffect(uiState.value.publishSucceeded) {
                if (uiState.value.publishSucceeded) {
                    viewModel.onPublishSucceededHandled()
                    navController
                        .getBackStackEntry(GuideTourDestination.MyTours)
                        .savedStateHandle[GUIDE_MY_TOURS_SELECTED_TAB_RESULT] =
                        GuideTourTab.REVIEW.name
                    navController.popBackStack(
                        route = GuideTourDestination.MyTours,
                        inclusive = false,
                    )
                }
            }
            GuideTourPublishStep4PreviewPublishScreen(
                uiState = uiState.value,
                onPublish = viewModel::onPublishClick,
            )
        }
    }
}

@Composable
private fun NavBackStackEntry.guideTourPublishViewModel(
    navController: NavController,
): GuideTourPublishViewModel {
    val graphBackStackEntry =
        remember(this) {
            navController.getBackStackEntry<GuideTourPublishGraph>()
        }
    return hiltViewModel(viewModelStoreOwner = graphBackStackEntry)
}
