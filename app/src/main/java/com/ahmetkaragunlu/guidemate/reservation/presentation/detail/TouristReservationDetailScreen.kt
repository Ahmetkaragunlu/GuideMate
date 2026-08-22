package com.ahmetkaragunlu.guidemate.reservation.presentation.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.components.GuideMateContentState
import com.ahmetkaragunlu.guidemate.common.ui.components.EditAlertDialog
import com.ahmetkaragunlu.guidemate.reservation.domain.model.TouristReservationStatus
import com.ahmetkaragunlu.guidemate.review.presentation.TourReviewBottomSheet
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.TourDetailContent
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.TourDetailNotice
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailMode

@Composable
fun TouristReservationDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: TouristReservationDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    GuideMateContentState(
        state = uiState.loadState,
        onRetry = viewModel::refresh,
        modifier = modifier,
    ) {
        uiState.detail?.let { detail ->
            TourDetailContent(
                uiState = detail,
                mode =
                    when (uiState.reservationStatus) {
                        TouristReservationStatus.CONFIRMED -> TourDetailMode.TOURIST_BOOKED
                        TouristReservationStatus.COMPLETED ->
                            if (uiState.canSubmitReview) {
                                TourDetailMode.TOURIST_REVIEWABLE
                            } else {
                                TourDetailMode.TOURIST_PAST
                            }
                        TouristReservationStatus.CANCELLED,
                        -> TourDetailMode.TOURIST_PAST
                        TouristReservationStatus.PENDING_PAYMENT,
                        TouristReservationStatus.EXPIRED,
                        null,
                        -> TourDetailMode.TOURIST_UNAVAILABLE
                    },
                onPrimaryAction = viewModel::showReviewForm,
                modifier = modifier,
                topContent =
                    uiState.noticeResId?.let { messageResId ->
                        { TourDetailNotice(messageResId = messageResId) }
                    },
            )
        }
    }

    TourReviewBottomSheet(
        uiState = uiState.reviewForm,
        onDismissRequest = viewModel::dismissReviewForm,
        onRatingChanged = viewModel::updateReviewRating,
        onCommentChanged = viewModel::updateReviewComment,
        onSubmit = viewModel::submitReview,
    )

    if (uiState.reviewForm.showSuccessDialog) {
        EditAlertDialog(
            title = R.string.tour_review_success_title,
            text = R.string.tour_review_success_message,
            onDismissRequest = {},
            confirmButton = {
                TextButton(onClick = viewModel::dismissReviewSuccessDialog) {
                    Text(stringResource(R.string.ok))
                }
            },
        )
    }
}
