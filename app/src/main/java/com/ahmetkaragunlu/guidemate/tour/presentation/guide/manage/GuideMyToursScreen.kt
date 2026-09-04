package com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.components.ActiveTourCard
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.components.PastTourCard
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.components.ReviewTourCard
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.model.GuideTourTab
import compose.icons.TablerIcons
import compose.icons.tablericons.Plus

@Composable
fun GuideMyToursScreen(
    onNavigateToTourPublish: () -> Unit,
    onNavigateToTourDetail: (tourId: String, sessionId: String) -> Unit,
    onNavigateToTourEdit: (tourId: String, sessionId: String) -> Unit,
    requestedTab: GuideTourTab? = null,
    onRequestedTabConsumed: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: GuideMyToursViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var tourIdPendingArchive by rememberSaveable { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(requestedTab) {
        requestedTab?.let { tab ->
            viewModel.applyNavigationResult(tab)
            onRequestedTabConsumed()
        }
    }
    LaunchedEffect(uiState.userMessage) {
        uiState.userMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.onUserMessageShown()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            GuideMateTabRow(
                tabs = GuideTourTab.entries,
                selectedTab = uiState.selectedTab,
                onTabSelected = viewModel::changeTab,
            )

            GuideMateContentState(
                state = uiState.loadState,
                onRetry = viewModel::refresh,
                modifier = Modifier.fillMaxSize(),
            ) {
                if (uiState.tours.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = stringResource(uiState.selectedTab.emptyMessageResId()),
                            color = colorResource(R.color.text_color),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(dimensionResource(R.dimen.spacing_medium)),
                        verticalArrangement =
                            Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        itemsIndexed(uiState.tours, key = { _, tour -> tour.id }) { index, tour ->
                            if (index == uiState.tours.lastIndex && uiState.canLoadMore) {
                                LaunchedEffect(tour.id) { viewModel.loadMore() }
                            }
                            when (uiState.selectedTab) {
                                GuideTourTab.ACTIVE ->
                                    ActiveTourCard(
                                        tour = tour,
                                        onToggleLive = { isLive ->
                                            viewModel.toggleBookingAvailability(tour.id, isLive)
                                        },
                                        onEdit = {
                                            onNavigateToTourEdit(tour.tourId, tour.id)
                                        },
                                        onClick = {
                                            onNavigateToTourDetail(tour.tourId, tour.id)
                                        },
                                        isToggleEnabled =
                                            tour.id !in uiState.pendingSessionIds,
                                    )
                                GuideTourTab.REVIEW ->
                                    ReviewTourCard(
                                        tour = tour,
                                        onEdit = {
                                            onNavigateToTourEdit(tour.tourId, tour.id)
                                        },
                                        onArchive = { tourIdPendingArchive = tour.tourId },
                                        onClick = {
                                            onNavigateToTourDetail(tour.tourId, tour.id)
                                        },
                                    )
                                GuideTourTab.PAST ->
                                    PastTourCard(
                                        tour = tour,
                                        onClick = {
                                            onNavigateToTourDetail(tour.tourId, tour.id)
                                        },
                                    )
                            }
                        }
                        if (uiState.isLoadingMore) {
                            item {
                                CircularProgressIndicator(
                                    color = colorResource(R.color.brand_color),
                                )
                            }
                        } else if (uiState.appendFailed) {
                            item {
                                TextButton(onClick = viewModel::loadMore) {
                                    Text(text = stringResource(R.string.common_retry))
                                }
                            }
                        }
                    }
                }
            }
        }

        if (uiState.selectedTab == GuideTourTab.ACTIVE) {
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

        SnackbarHost(
            hostState = snackbarHostState,
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(
                        bottom =
                            if (uiState.selectedTab == GuideTourTab.ACTIVE) {
                                24.dp
                            } else {
                                dimensionResource(R.dimen.spacing_large)
                            },
                    ),
        )

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

private fun GuideTourTab.emptyMessageResId(): Int =
    when (this) {
        GuideTourTab.ACTIVE -> R.string.guide_tours_empty_active
        GuideTourTab.REVIEW -> R.string.guide_tours_empty_review
        GuideTourTab.PAST -> R.string.guide_tours_empty_past
    }
