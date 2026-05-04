package com.ahmetkaragunlu.guidemate.screens.tourist.trips

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.common.tab.GuideMateTabRow
import com.ahmetkaragunlu.guidemate.screens.tourist.trips.components.PastTripCard
import com.ahmetkaragunlu.guidemate.screens.tourist.trips.components.UpcomingTripCard

@Composable
fun TouristTripsScreen(
    viewModel: TouristTripsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val trips by viewModel.trips.collectAsStateWithLifecycle()

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
                            onCancelClick = { viewModel.onCancelTripClick(trip.id) },
                        )

                    TripTab.PAST ->
                        PastTripCard(
                            trip = trip,
                            onDetailsClick = { },
                        )
                }
            }
        }
    }
}
