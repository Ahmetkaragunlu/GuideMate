package com.ahmetkaragunlu.guidemate.screens.tourist.trips

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.components.EditAlertDialog
import com.ahmetkaragunlu.guidemate.screens.common.tab.GuideMateTabRow
import com.ahmetkaragunlu.guidemate.screens.tourist.trips.model.TripTab
import com.ahmetkaragunlu.guidemate.screens.tourist.trips.components.PastTripCard
import com.ahmetkaragunlu.guidemate.screens.tourist.trips.components.UpcomingTripCard

@Composable
fun TouristTripsScreen(
    onNavigateToTourDetail: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TouristTripsViewModel = hiltViewModel(),
) {
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val trips by viewModel.trips.collectAsStateWithLifecycle()
    var reservationIdPendingCancellation by rememberSaveable { mutableStateOf<String?>(null) }
    var showCancellationError by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize()) {
        GuideMateTabRow(
            tabs = TripTab.entries,
            selectedTab = selectedTab,
            onTabSelected = viewModel::changeTab,
        )

        LazyColumn(
            contentPadding = PaddingValues(dimensionResource(R.dimen.spacing_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(),
        ) {
            items(trips, key = { it.id }) { trip ->
                when (selectedTab) {
                    TripTab.UPCOMING ->
                        UpcomingTripCard(
                            trip = trip,
                            onDetailsClick = {
                                onNavigateToTourDetail(trip.tourSessionId)
                            },
                            onCancelClick = {
                                reservationIdPendingCancellation = trip.id
                            },
                        )

                    TripTab.PAST ->
                        PastTripCard(
                            trip = trip,
                            onDetailsClick = {
                                onNavigateToTourDetail(trip.tourSessionId)
                            },
                        )
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
                        showCancellationError = !viewModel.cancelReservation(reservationId)
                        reservationIdPendingCancellation = null
                    },
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

    if (showCancellationError) {
        EditAlertDialog(
            title = R.string.reservation_cancellation_failed_title,
            text = R.string.reservation_cancellation_failed_message,
            onDismissRequest = { showCancellationError = false },
            confirmButton = {
                TextButton(onClick = { showCancellationError = false }) {
                    Text(text = stringResource(R.string.ok))
                }
            },
        )
    }
}
