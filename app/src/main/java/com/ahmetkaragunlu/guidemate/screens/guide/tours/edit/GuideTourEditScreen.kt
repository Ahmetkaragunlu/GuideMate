package com.ahmetkaragunlu.guidemate.screens.guide.tours.edit

import androidx.activity.compose.BackHandler
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.components.EditAlertDialog
import com.ahmetkaragunlu.guidemate.components.ImageSourcePicker
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.GuideTourTab
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.TourApprovalStatus

@Composable
fun GuideTourEditScreen(
    onSaved: (GuideTourTab) -> Unit,
    onNavigateBack: () -> Unit,
    onBackActionChanged: ((() -> Unit)?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GuideTourEditViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDiscardDialog by rememberSaveable { mutableStateOf(false) }
    var showReviewConfirmationDialog by rememberSaveable { mutableStateOf(false) }
    var showPhotoSourceSheet by rememberSaveable { mutableStateOf(false) }
    val requestExit = {
        if (uiState.hasUnsavedChanges) {
            showDiscardDialog = true
        } else {
            onNavigateBack()
        }
    }

    BackHandler(onBack = requestExit)
    DisposableEffect(uiState.hasUnsavedChanges) {
        onBackActionChanged(requestExit)
        onDispose { onBackActionChanged(null) }
    }

    GuideTourEditContent(
        uiState = uiState,
        onTitleChange = viewModel::onTitleChange,
        onDescriptionChange = viewModel::onDescriptionChange,
        onDateSelected = viewModel::onTourDateSelected,
        onStartTimeSelected = viewModel::onStartTimeSelected,
        onMeetingPointChange = viewModel::onMeetingPointChange,
        onDurationChange = viewModel::onDurationChange,
        onPriceChange = viewModel::onPriceChange,
        onCapacityChange = viewModel::onCapacityChange,
        onRemoveLanguage = viewModel::removeLanguage,
        onAddLanguage = { },
        onChangePhotos = { showPhotoSourceSheet = true },
        onSave = {
            if (uiState.requiresReviewConfirmation) {
                showReviewConfirmationDialog = true
            } else {
                viewModel.saveChanges()?.let(onSaved)
            }
        },
        modifier = modifier,
    )

    ImageSourcePicker(
        isVisible = showPhotoSourceSheet,
        titleResId = R.string.tour_cover_photo_source_title,
        onDismissRequest = { showPhotoSourceSheet = false },
        onImageSelected = viewModel::onCoverImageSelected,
        onError = viewModel::onCoverImageSelectionError,
    )

    if (showDiscardDialog) {
        EditAlertDialog(
            onDismissRequest = { showDiscardDialog = false },
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
                    onClick = { showDiscardDialog = false },
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

    if (showReviewConfirmationDialog) {
        EditAlertDialog(
            title =
                if (uiState.approvalStatus == TourApprovalStatus.REJECTED) {
                    R.string.resubmit_for_review_confirmation_title
                } else {
                    R.string.tour_edit_review_confirmation_title
                },
            text =
                if (uiState.approvalStatus == TourApprovalStatus.REJECTED) {
                    R.string.resubmit_for_review_confirmation_message
                } else {
                    R.string.tour_edit_review_confirmation_message
                },
            onDismissRequest = { showReviewConfirmationDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showReviewConfirmationDialog = false
                        viewModel.saveChanges()?.let(onSaved)
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
                    onClick = { showReviewConfirmationDialog = false },
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
