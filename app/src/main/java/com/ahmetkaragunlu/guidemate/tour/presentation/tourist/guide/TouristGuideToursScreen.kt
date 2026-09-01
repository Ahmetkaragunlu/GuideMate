package com.ahmetkaragunlu.guidemate.tour.presentation.tourist.guide

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.components.GuideMateContentState
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.tour.presentation.components.TourSearchResultCard

@Composable
fun TouristGuideToursScreen(
    guideId: Long,
    onTourClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TouristGuideToursViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(guideId) {
        viewModel.loadGuideTours(guideId)
    }

    GuideMateContentState(
        state = uiState.loadState,
        onRetry = viewModel::retry,
        modifier = modifier.fillMaxSize(),
    ) {
        if (uiState.tours.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.public_guide_tours_empty),
                    color = colorResource(R.color.text_color),
                )
            }
        } else {
            LazyColumn(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.spacing_medium)),
                verticalArrangement =
                    Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
            ) {
                items(uiState.tours, key = { it.sessionId }) { tour ->
                    TourSearchResultCard(
                        tour = tour,
                        onClick = { onTourClick(tour.sessionId) },
                    )
                }
                when {
                    uiState.isLoadingMore ->
                        item {
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(dimensionResource(R.dimen.spacing_medium)),
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
                                onRetry = viewModel::retry,
                                modifier = Modifier.fillMaxWidth().height(APPEND_RETRY_HEIGHT),
                            ) {}
                        }
                    uiState.canLoadMore ->
                        item {
                            LaunchedEffect(uiState.tours.size) {
                                viewModel.loadMore()
                            }
                        }
                }
            }
        }
    }
}

private val APPEND_RETRY_HEIGHT = 112.dp
