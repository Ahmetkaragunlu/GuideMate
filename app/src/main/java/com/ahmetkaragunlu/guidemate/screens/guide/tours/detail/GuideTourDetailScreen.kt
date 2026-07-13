package com.ahmetkaragunlu.guidemate.screens.guide.tours.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.screens.common.tours.detail.TourDetailContent
import com.ahmetkaragunlu.guidemate.screens.common.tours.detail.model.TourDetailMode
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.GuideTourTab

@Composable
fun GuideTourDetailScreen(
    onFinished: (GuideTourTab) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GuideTourDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    uiState?.let { state ->
        TourDetailContent(
            uiState = state.detail,
            mode = state.mode,
            onPrimaryAction = {
                when (state.mode) {
                    TourDetailMode.GUIDE_ACTIVE -> viewModel.showCancelDialog()
                    TourDetailMode.GUIDE_PAST -> viewModel.showNewSessionSheet()
                    else -> Unit
                }
            },
            modifier = modifier,
        )

        if (state.action.isCancelDialogVisible) {
            CancelTourSessionDialog(
                reason = state.action.cancellationReason,
                hasBookings = state.detail.bookedCount > 0,
                onReasonChange = viewModel::onCancellationReasonChange,
                onDismiss = viewModel::dismissCancelDialog,
                onConfirm = {
                    if (viewModel.cancelSession()) {
                        onFinished(GuideTourTab.PAST)
                    }
                },
            )
        }

        if (state.action.isNewSessionSheetVisible) {
            AddTourSessionBottomSheet(
                formState = state.action.newSessionForm,
                onDateSelected = viewModel::onNewSessionDateSelected,
                onTimeSelected = viewModel::onNewSessionTimeSelected,
                onDurationSelected = viewModel::onNewSessionDurationSelected,
                onMeetingPointChange = viewModel::onNewSessionMeetingPointChange,
                onPriceChange = viewModel::onNewSessionPriceChange,
                onCapacityChange = viewModel::onNewSessionCapacityChange,
                onDismiss = viewModel::dismissNewSessionSheet,
                onConfirm = {
                    if (viewModel.addNewSession()) {
                        onFinished(GuideTourTab.ACTIVE)
                    }
                },
            )
        }
    }
}
