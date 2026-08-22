package com.ahmetkaragunlu.guidemate.reservation.presentation.trips

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.components.EditAlertDialog
import com.ahmetkaragunlu.guidemate.common.ui.components.GuideMateContentState
import com.ahmetkaragunlu.guidemate.common.ui.components.GuideMateTabRow
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.reservation.presentation.trips.components.PastTripCard
import com.ahmetkaragunlu.guidemate.reservation.presentation.trips.components.UpcomingTripCard
import com.ahmetkaragunlu.guidemate.reservation.presentation.trips.model.TripTab

@Composable
fun TouristTripsScreen(
    onNavigateToReservationDetail: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TouristTripsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var reservationIdPendingCancellation by rememberSaveable { mutableStateOf<String?>(null) }

    Column(modifier = modifier.fillMaxSize()) {
        GuideMateTabRow(
            tabs = TripTab.entries,
            selectedTab = uiState.selectedTab,
            onTabSelected = viewModel::changeTab,
        )

        GuideMateContentState(
            state = uiState.loadState,
            onRetry = viewModel::refresh,
            modifier = Modifier.fillMaxWidth().weight(1f),
        ) {
            if (uiState.trips.isEmpty()) {
                TripsEmptyState(selectedTab = uiState.selectedTab)
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(dimensionResource(R.dimen.spacing_medium)),
                    verticalArrangement =
                        Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(uiState.trips, key = { it.reservationId }) { trip ->
                        when (uiState.selectedTab) {
                            TripTab.UPCOMING ->
                                UpcomingTripCard(
                                    trip = trip,
                                    onDetailsClick = {
                                        onNavigateToReservationDetail(trip.reservationId)
                                    },
                                    onCancelClick = {
                                        reservationIdPendingCancellation = trip.reservationId
                                    },
                                )

                            TripTab.PAST ->
                                PastTripCard(
                                    trip = trip,
                                    onDetailsClick = {
                                        onNavigateToReservationDetail(trip.reservationId)
                                    },
                                )
                        }
                    }

                    when {
                        uiState.isLoadingMore ->
                            item {
                                Box(
                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    CircularProgressIndicator(
                                        color = colorResource(R.color.brand_color),
                                    )
                                }
                            }
                        uiState.appendFailed ->
                            item {
                                GuideMateContentState(
                                    state = ContentLoadState.ERROR,
                                    onRetry = viewModel::loadMore,
                                    modifier = Modifier.fillMaxWidth().height(112.dp),
                                ) {}
                            }
                        uiState.canLoadMore ->
                            item {
                                LaunchedEffect(uiState.trips.size) {
                                    viewModel.loadMore()
                                }
                            }
                    }
                }
            }
        }
    }

    reservationIdPendingCancellation?.let { reservationId ->
        EditAlertDialog(
            title = R.string.cancel_reservation_title,
            text = R.string.cancel_reservation_message,
            onDismissRequest = { reservationIdPendingCancellation = null },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.cancelReservation(reservationId)
                        reservationIdPendingCancellation = null
                    },
                    enabled = uiState.cancellingReservationId == null,
                    colors =
                        ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error,
                        ),
                ) {
                    Text(text = stringResource(R.string.yes))
                }
            },
            dismissButton = {
                TextButton(onClick = { reservationIdPendingCancellation = null }) {
                    Text(text = stringResource(R.string.no))
                }
            },
        )
    }

    uiState.cancellationFeedback?.let { feedback ->
        EditAlertDialog(
            title =
                if (feedback.isSuccess) {
                    R.string.reservation_cancellation_success_title
                } else {
                    R.string.reservation_cancellation_failed_title
                },
            text = R.string.reservation_cancellation_failed_message,
            textValue = feedback.message,
            onDismissRequest = viewModel::dismissCancellationFeedback,
            confirmButton = {
                TextButton(onClick = viewModel::dismissCancellationFeedback) {
                    Text(text = stringResource(R.string.ok))
                }
            },
        )
    }
}

@Composable
private fun TripsEmptyState(selectedTab: TripTab) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text =
                stringResource(
                    when (selectedTab) {
                        TripTab.UPCOMING -> R.string.upcoming_trips_empty
                        TripTab.PAST -> R.string.past_trips_empty
                    },
                ),
            color = colorResource(R.color.text_color),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
