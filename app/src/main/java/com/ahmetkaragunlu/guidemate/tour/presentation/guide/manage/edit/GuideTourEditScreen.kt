package com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.edit

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.components.EditAlertDialog
import com.ahmetkaragunlu.guidemate.common.ui.components.GuideMateContentState
import com.ahmetkaragunlu.guidemate.common.ui.image.ImageSourcePicker
import com.ahmetkaragunlu.guidemate.common.location.presentation.components.LanguageSelectionBottomSheet
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.category.GuideTourCategorySelectionBottomSheet
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.model.GuideTourTab
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourApprovalStatus

private enum class GuideTourEditOverlay {
    DISCARD_CONFIRMATION,
    REVIEW_CONFIRMATION,
    PHOTO_SOURCE,
    CATEGORY_PICKER,
    LANGUAGE_PICKER,
}

@Composable
fun GuideTourEditScreen(
    onSaved: (GuideTourTab) -> Unit,
    onNavigateBack: () -> Unit,
    onBackActionChanged: ((() -> Unit)?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GuideTourEditViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val content = uiState.content
    val operation = uiState.operation
    var activeOverlay by rememberSaveable { mutableStateOf<GuideTourEditOverlay?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val requestExit = {
        if (operation.isSaving) {
            Unit
        } else if (operation.hasUnsavedChanges) {
            activeOverlay = GuideTourEditOverlay.DISCARD_CONFIRMATION
        } else {
            onNavigateBack()
        }
    }

    LaunchedEffect(operation.userMessage) {
        operation.userMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.onUserMessageShown()
        }
    }
    LaunchedEffect(operation.savedTargetTab) {
        operation.savedTargetTab?.let { tab ->
            viewModel.onSavedHandled()
            onSaved(tab)
        }
    }

    BackHandler(onBack = requestExit)
    DisposableEffect(operation.hasUnsavedChanges) {
        onBackActionChanged(requestExit)
        onDispose { onBackActionChanged(null) }
    }

    Box(modifier = modifier.fillMaxSize()) {
        GuideMateContentState(
            state = operation.loadState,
            onRetry = viewModel::refresh,
            modifier = Modifier.fillMaxSize(),
        ) {
            GuideTourEditContent(
                uiState = uiState,
                onTitleChange = viewModel::onTitleChange,
                onDescriptionChange = viewModel::onDescriptionChange,
                onCategoryClick = { activeOverlay = GuideTourEditOverlay.CATEGORY_PICKER },
                onDateSelected = viewModel::onTourDateSelected,
                onStartTimeSelected = viewModel::onStartTimeSelected,
                onMeetingPointChange = viewModel::onMeetingPointChange,
                onDurationChange = viewModel::onDurationChange,
                onPriceChange = viewModel::onPriceChange,
                onCapacityChange = viewModel::onCapacityChange,
                onRemoveLanguage = viewModel::removeLanguage,
                onAddLanguage = { activeOverlay = GuideTourEditOverlay.LANGUAGE_PICKER },
                onChangePhotos = { activeOverlay = GuideTourEditOverlay.PHOTO_SOURCE },
                onSave = {
                    if (operation.requiresReviewConfirmation) {
                        activeOverlay = GuideTourEditOverlay.REVIEW_CONFIRMATION
                    } else {
                        viewModel.saveChanges()
                    }
                },
                modifier = Modifier.fillMaxSize(),
            )
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }

    ImageSourcePicker(
        isVisible = activeOverlay == GuideTourEditOverlay.PHOTO_SOURCE,
        titleResId = R.string.tour_cover_photo_source_title,
        onDismissRequest = { activeOverlay = null },
        onImageSelected = viewModel::onCoverImageSelected,
        onError = viewModel::onCoverImageSelectionError,
    )

    GuideTourCategorySelectionBottomSheet(
        isVisible = activeOverlay == GuideTourEditOverlay.CATEGORY_PICKER,
        selectedCategory = content.category,
        onDismissRequest = { activeOverlay = null },
        onCategorySelected = viewModel::onCategorySelected,
    )

    LanguageSelectionBottomSheet(
        isVisible = activeOverlay == GuideTourEditOverlay.LANGUAGE_PICKER,
        selectedLanguageCodes = content.languages.mapTo(mutableSetOf()) { it.code },
        onDismissRequest = { activeOverlay = null },
        onLanguagesSelected = { languages ->
            viewModel.onLanguagesSelected(languages)
            activeOverlay = null
        },
    )

    if (activeOverlay == GuideTourEditOverlay.DISCARD_CONFIRMATION) {
        EditAlertDialog(
            onDismissRequest = { activeOverlay = null },
            title = R.string.unsaved_tour_changes_title,
            text = R.string.unsaved_tour_changes_message,
            confirmButton = {
                TextButton(
                    onClick = onNavigateBack,
                    colors =
                        ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error,
                        ),
                ) {
                    Text(text = stringResource(R.string.yes))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { activeOverlay = null },
                    colors =
                        ButtonDefaults.textButtonColors(
                            contentColor = colorResource(R.color.brand_color),
                        ),
                ) {
                    Text(text = stringResource(R.string.no))
                }
            },
        )
    }

    if (activeOverlay == GuideTourEditOverlay.REVIEW_CONFIRMATION) {
        EditAlertDialog(
            title =
                if (operation.approvalStatus == TourApprovalStatus.REJECTED) {
                    R.string.resubmit_for_review_confirmation_title
                } else {
                    R.string.tour_edit_review_confirmation_title
                },
            text =
                if (operation.approvalStatus == TourApprovalStatus.REJECTED) {
                    R.string.resubmit_for_review_confirmation_message
                } else {
                    R.string.tour_edit_review_confirmation_message
                },
            onDismissRequest = { activeOverlay = null },
            confirmButton = {
                TextButton(
                    onClick = {
                        activeOverlay = null
                        viewModel.saveChanges()
                    },
                    colors =
                        ButtonDefaults.textButtonColors(
                            contentColor = colorResource(R.color.brand_color),
                        ),
                ) {
                    Text(text = stringResource(R.string.yes))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { activeOverlay = null },
                    colors =
                        ButtonDefaults.textButtonColors(
                            contentColor = colorResource(R.color.text_color),
                        ),
                ) {
                    Text(text = stringResource(R.string.no))
                }
            },
        )
    }
}
