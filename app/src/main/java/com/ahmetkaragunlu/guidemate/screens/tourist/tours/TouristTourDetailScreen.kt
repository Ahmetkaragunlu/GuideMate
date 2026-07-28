package com.ahmetkaragunlu.guidemate.screens.tourist.tours

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.components.EditAlertDialog
import com.ahmetkaragunlu.guidemate.screens.common.tours.detail.TourDetailContent
import com.ahmetkaragunlu.guidemate.screens.common.tours.detail.model.TourDetailMode
import com.ahmetkaragunlu.guidemate.screens.common.tours.detail.model.TourDetailStatus
import com.ahmetkaragunlu.guidemate.screens.common.tours.model.catalog.TourBookingAvailability
import com.ahmetkaragunlu.guidemate.screens.tourist.booking.model.detailMessageResId
import com.ahmetkaragunlu.guidemate.screens.tourist.reviews.TourReviewBottomSheet
import com.ahmetkaragunlu.guidemate.screens.tourist.reviews.model.TourReviewAvailability

@Composable
fun TouristTourDetailScreen(
    onBookTour: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TouristTourDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    uiState?.let { screenState ->
        val detail = screenState.detail
        val bookingAvailability = screenState.bookingAvailability
        val reviewAvailability = screenState.reviewAvailability
        TourDetailContent(
            uiState = detail,
            mode =
                when {
                    detail.sessionStatus == TourDetailStatus.COMPLETED &&
                        reviewAvailability == TourReviewAvailability.AVAILABLE ->
                        TourDetailMode.TOURIST_REVIEWABLE
                    detail.sessionStatus == TourDetailStatus.COMPLETED ||
                        detail.sessionStatus == TourDetailStatus.CANCELLED ->
                        TourDetailMode.TOURIST_PAST
                    bookingAvailability == TourBookingAvailability.ALREADY_RESERVED ->
                        TourDetailMode.TOURIST_BOOKED
                    bookingAvailability.isBookable -> TourDetailMode.TOURIST_BOOKABLE
                    else -> TourDetailMode.TOURIST_UNAVAILABLE
                },
            onPrimaryAction = {
                when {
                    reviewAvailability == TourReviewAvailability.AVAILABLE ->
                        viewModel.openReviewSheet()
                    bookingAvailability.isBookable -> onBookTour(detail.sessionId)
                }
            },
            modifier = modifier,
            topContent =
                resolveDetailNoticeResId(
                    detailStatus = detail.sessionStatus,
                    reviewAvailability = reviewAvailability,
                    bookingMessageResId = bookingAvailability.detailMessageResId,
                )?.let { messageResId ->
                    {
                        TouristTourDetailNotice(messageResId = messageResId)
                    }
                },
        )

        TourReviewBottomSheet(
            uiState = screenState.reviewForm,
            onDismissRequest = viewModel::dismissReviewSheet,
            onRatingChanged = viewModel::updateReviewRating,
            onCommentChanged = viewModel::updateReviewComment,
            onSubmit = viewModel::submitReview,
        )

        if (screenState.reviewForm.showSuccessDialog) {
            EditAlertDialog(
                title = R.string.tour_review_success_title,
                text = R.string.tour_review_success_message,
                confirmButton = {
                    TextButton(onClick = viewModel::dismissReviewSuccess) {
                        Text(text = stringResource(R.string.ok))
                    }
                },
                onDismissRequest = viewModel::dismissReviewSuccess,
            )
        }
    }
}

@Composable
private fun TouristTourDetailNotice(
    @StringRes messageResId: Int,
) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(R.dimen.spacing_medium))
                .background(
                    color = colorResource(R.color.brand_color).copy(alpha = 0.08f),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.radius_medium)),
                )
                .padding(dimensionResource(R.dimen.spacing_medium)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(messageResId),
            style = MaterialTheme.typography.bodyMedium,
            color = colorResource(R.color.text_color),
        )
    }
}

@StringRes
private fun resolveDetailNoticeResId(
    detailStatus: TourDetailStatus?,
    reviewAvailability: TourReviewAvailability,
    @StringRes bookingMessageResId: Int?,
): Int? =
    when {
        reviewAvailability == TourReviewAvailability.SUBMITTED ->
            R.string.tour_review_submitted_notice
        detailStatus != null -> null
        else -> bookingMessageResId
    }
