package com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.common.ui.components.GuideMateContentState
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.TourDetailContent
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailMode
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.model.GuideTourTab

@Composable
fun GuideTourDetailScreen(
    onFinished: (GuideTourTab) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GuideTourDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.userMessage) {
        uiState.userMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.onUserMessageShown()
        }
    }
    LaunchedEffect(uiState.finishedTab) {
        uiState.finishedTab?.let { tab ->
            viewModel.onFinishedHandled()
            onFinished(tab)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        GuideMateContentState(
            state = uiState.loadState,
            onRetry = viewModel::refresh,
            modifier = Modifier.fillMaxSize(),
        ) {
            val detail = uiState.detail
            val mode = uiState.mode
            if (detail != null && mode != null) {
                TourDetailContent(
                    uiState = detail,
                    mode = mode,
                    onPrimaryAction = {
                        when (mode) {
                            TourDetailMode.GUIDE_ACTIVE -> viewModel.showCancelDialog()
                            TourDetailMode.GUIDE_PAST -> viewModel.showNewSessionSheet()
                            else -> Unit
                        }
                    },
                    isPrimaryActionEnabled = !uiState.action.isSubmitting,
                    modifier = Modifier.fillMaxSize(),
                )

                if (uiState.action.isCancelDialogVisible) {
                    CancelTourSessionDialog(
                        reason = uiState.action.cancellationReason,
                        hasBookings = detail.bookedCount > 0,
                        onReasonChange = viewModel::onCancellationReasonChange,
                        onDismiss = viewModel::dismissCancelDialog,
                        onConfirm = viewModel::cancelSession,
                        isSubmitting = uiState.action.isSubmitting,
                    )
                }

                if (uiState.action.isNewSessionSheetVisible) {
                    AddTourSessionBottomSheet(
                        formState = uiState.action.newSessionForm,
                        onDateSelected = viewModel::onNewSessionDateSelected,
                        onTimeSelected = viewModel::onNewSessionTimeSelected,
                        onDurationSelected = viewModel::onNewSessionDurationSelected,
                        onMeetingPointChange = viewModel::onNewSessionMeetingPointChange,
                        onPriceChange = viewModel::onNewSessionPriceChange,
                        onCapacityChange = viewModel::onNewSessionCapacityChange,
                        onDismiss = viewModel::dismissNewSessionSheet,
                        onConfirm = viewModel::addNewSession,
                        isSubmitting = uiState.action.isSubmitting,
                    )
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}
