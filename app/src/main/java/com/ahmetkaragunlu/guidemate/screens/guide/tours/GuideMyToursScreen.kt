package com.ahmetkaragunlu.guidemate.screens.guide.tours

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.ahmetkaragunlu.guidemate.components.EditAlertDialog
import com.ahmetkaragunlu.guidemate.screens.common.tab.GuideMateTabRow
import com.ahmetkaragunlu.guidemate.screens.guide.tours.components.ActiveTourCard
import com.ahmetkaragunlu.guidemate.screens.guide.tours.components.PastTourCard
import com.ahmetkaragunlu.guidemate.screens.guide.tours.components.ReviewTourCard
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.GuideTourTab
import compose.icons.TablerIcons
import compose.icons.tablericons.Plus

@Composable
fun GuideMyToursScreen(
    onNavigateToTourPublish: () -> Unit,
    onNavigateToTourDetail: (String) -> Unit,
    onNavigateToTourEdit: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GuideMyToursViewModel = hiltViewModel(),
) {
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val tours by viewModel.tours.collectAsStateWithLifecycle()
    var tourIdPendingArchive by rememberSaveable { mutableStateOf<String?>(null) }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            GuideMateTabRow(
                tabs = GuideTourTab.entries,
                selectedTab = selectedTab,
                onTabSelected = viewModel::changeTab,
            )

            LazyColumn(
                contentPadding = PaddingValues(dimensionResource(R.dimen.spacing_medium)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize(),
            ) {
                items(tours, key = { it.id }) { tour ->
                    when (selectedTab) {
                        GuideTourTab.ACTIVE ->
                            ActiveTourCard(
                                tour = tour,
                                onToggleLive = { isLive ->
                                    viewModel.toggleBookingAvailability(tour.id, isLive)
                                },
                                onEdit = { onNavigateToTourEdit(tour.id) },
                                onClick = { onNavigateToTourDetail(tour.id) },
                            )
                        GuideTourTab.REVIEW ->
                            ReviewTourCard(
                                tour = tour,
                                onEdit = { onNavigateToTourEdit(tour.id) },
                                onArchive = { tourIdPendingArchive = tour.tourId },
                                onClick = { onNavigateToTourDetail(tour.id) },
                            )
                        GuideTourTab.PAST ->
                            PastTourCard(
                                tour = tour,
                                onClick = { onNavigateToTourDetail(tour.id) },
                            )
                    }
                }
            }
        }

        if (selectedTab == GuideTourTab.ACTIVE) {
            FloatingActionButton(
                onClick = onNavigateToTourPublish,
                modifier =
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(dimensionResource(R.dimen.spacing_large)),
                containerColor = colorResource(R.color.brand_color),
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp),
            ) {
                Icon(
                    imageVector = TablerIcons.Plus,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }

        if (tourIdPendingArchive != null) {
            EditAlertDialog(
                title = R.string.archive_tour_draft_title,
                text = R.string.archive_tour_draft_message,
                onDismissRequest = { tourIdPendingArchive = null },
                confirmButton = {
                    TextButton(
                        onClick = {
                            tourIdPendingArchive?.let(viewModel::archiveRejectedTour)
                            tourIdPendingArchive = null
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
                    TextButton(onClick = { tourIdPendingArchive = null }) {
                        Text(text = stringResource(R.string.no))
                    }
                },
            )
        }
    }
}
