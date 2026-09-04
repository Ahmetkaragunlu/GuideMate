package com.ahmetkaragunlu.guidemate.tour.presentation.tourist.detail

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.common.ui.components.GuideMateContentState
import com.ahmetkaragunlu.guidemate.reservation.presentation.checkout.model.detailMessageResId
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.TourDetailContent
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.TourDetailNotice
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailMode
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailStatus

@Composable
fun TouristTourDetailScreen(
    onBookTour: (String) -> Unit,
    onNavigateToGuideProfile: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TouristTourDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    GuideMateContentState(
        state = uiState.loadState,
        onRetry = viewModel::refresh,
        modifier = modifier,
    ) {
        uiState.detail?.let { detail ->
            val bookingAvailability = uiState.bookingAvailability
            TourDetailContent(
                uiState = detail,
                mode =
                    when {
                        detail.sessionStatus != null ->
                            TourDetailMode.TOURIST_PAST
                        bookingAvailability.isBookable -> TourDetailMode.TOURIST_BOOKABLE
                        else -> TourDetailMode.TOURIST_UNAVAILABLE
                    },
                onPrimaryAction = {
                    if (bookingAvailability.isBookable) onBookTour(detail.sessionId)
                },
                onGuideProfileClick = { onNavigateToGuideProfile(detail.guideId) },
                modifier = modifier,
                topContent =
                    resolveDetailNoticeResId(
                        detailStatus = detail.sessionStatus,
                        bookingMessageResId = bookingAvailability.detailMessageResId,
                    )?.let { messageResId ->
                        { TourDetailNotice(messageResId = messageResId) }
                    },
            )
        }
    }
}

@StringRes
private fun resolveDetailNoticeResId(
    detailStatus: TourDetailStatus?,
    @StringRes bookingMessageResId: Int?,
): Int? =
    when {
        detailStatus != null -> null
        else -> bookingMessageResId
    }
