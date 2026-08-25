package com.ahmetkaragunlu.guidemate.discovery.presentation.tourist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.components.EditTextField
import com.ahmetkaragunlu.guidemate.common.ui.components.EditButton
import com.ahmetkaragunlu.guidemate.common.ui.components.GuideMateContentState
import com.ahmetkaragunlu.guidemate.discovery.presentation.tourist.model.ExploreTab
import com.ahmetkaragunlu.guidemate.common.ui.components.GuideMateTabRow
import com.ahmetkaragunlu.guidemate.profile.presentation.components.GuideResultCard
import com.ahmetkaragunlu.guidemate.profile.presentation.model.GuideResultUiModel
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.tour.presentation.components.TourSearchResultCard
import com.ahmetkaragunlu.guidemate.tour.presentation.model.TourSearchResultUiModel
import compose.icons.TablerIcons
import compose.icons.tablericons.AdjustmentsHorizontal

@Composable
fun TouristExploreScreen(
    modifier: Modifier = Modifier,
    viewModel: TouristExploreViewModel = hiltViewModel(),
    onNavigateToFilter: () -> Unit,
    onNavigateToTourDetail: (String) -> Unit = {},
    onNavigateToGuideProfile: (Long) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        GuideMateTabRow(
            tabs = ExploreTab.entries,
            selectedTab = uiState.selectedTab,
            onTabSelected = { newTab ->
                viewModel.updateSelectedTab(newTab)
            },
        )

        when (uiState.selectedTab) {
            ExploreTab.TOURS ->
                ToursContent(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.spacing_medium)),
                    searchQuery = uiState.toursSearchQuery,
                    onSearchQueryChange = viewModel::updateToursSearchQuery,
                    onNavigateToFilter = onNavigateToFilter,
                    tours = uiState.tourResults,
                    resultCount = uiState.tourResultCount,
                    loadState = uiState.tourResultsLoadState,
                    isLoadingMore = uiState.isLoadingMoreTours,
                    appendFailed = uiState.tourAppendFailed,
                    canLoadMore = uiState.canLoadMoreTours,
                    onRetry = viewModel::refreshTours,
                    onLoadMore = viewModel::loadMoreTours,
                    onClearFilters = viewModel::clearSearchAndFilters,
                    onTourClick = onNavigateToTourDetail,
                )

            ExploreTab.GUIDES ->
                GuidesContent(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.spacing_medium)),
                    searchQuery = uiState.guidesSearchQuery,
                    onSearchQueryChange = viewModel::updateGuidesSearchQuery,
                    guides = uiState.guideResults,
                    loadState = uiState.guideResultsLoadState,
                    isLoadingMore = uiState.isLoadingMoreGuides,
                    appendFailed = uiState.guideAppendFailed,
                    canLoadMore = uiState.canLoadMoreGuides,
                    onRetry = viewModel::refreshGuides,
                    onLoadMore = viewModel::loadMoreGuides,
                    onGuideClick = onNavigateToGuideProfile,
                )
        }
    }
}

@Composable
private fun ToursContent(
    modifier: Modifier = Modifier,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onNavigateToFilter: () -> Unit,
    tours: List<TourSearchResultUiModel>,
    resultCount: Long,
    loadState: ContentLoadState,
    isLoadingMore: Boolean,
    appendFailed: Boolean,
    canLoadMore: Boolean,
    onRetry: () -> Unit,
    onLoadMore: () -> Unit,
    onClearFilters: () -> Unit,
    onTourClick: (String) -> Unit,
) {
    Column(
        modifier = modifier.padding(dimensionResource(R.dimen.spacing_medium)),
    ) {
        EditTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            colors =
                OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorResource(R.color.brand_color),
                    unfocusedBorderColor = colorResource(R.color.brand_color),
                    cursorColor = colorResource(R.color.brand_color),
                ),
            placeholder = R.string.search_tours,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = Color.Gray,
                )
            },
            trailingIcon = {
                IconButton(onClick = { onNavigateToFilter() }) {
                    Icon(
                        imageVector = TablerIcons.AdjustmentsHorizontal,
                        contentDescription = null,
                        tint = colorResource(R.color.brand_color),
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))
        GuideMateContentState(
            state = loadState,
            onRetry = onRetry,
            modifier = Modifier.fillMaxWidth().weight(1f),
        ) {
            if (tours.isEmpty()) {
                TourSearchEmptyState(onClearFilters = onClearFilters)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement =
                        androidx.compose.foundation.layout.Arrangement.spacedBy(
                            dimensionResource(R.dimen.spacing_medium),
                        ),
                ) {
                    item {
                        Text(
                            text =
                                androidx.compose.ui.res.stringResource(
                                    R.string.tour_search_result_count,
                                    resultCount,
                                ),
                            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                            color = colorResource(R.color.text_color),
                        )
                    }
                    items(tours, key = { it.sessionId }) { tour ->
                        TourSearchResultCard(
                            tour = tour,
                            onClick = { onTourClick(tour.sessionId) },
                        )
                    }
                    when {
                        isLoadingMore ->
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
                        appendFailed ->
                            item {
                                GuideMateContentState(
                                    state = ContentLoadState.ERROR,
                                    onRetry = onLoadMore,
                                    modifier = Modifier.fillMaxWidth().height(112.dp),
                                ) {}
                            }
                        canLoadMore ->
                            item {
                                androidx.compose.runtime.LaunchedEffect(tours.size) {
                                    onLoadMore()
                                }
                            }
                    }
                }
            }
        }
    }
}

@Composable
private fun TourSearchEmptyState(onClearFilters: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement =
            androidx.compose.foundation.layout.Arrangement.spacedBy(
                dimensionResource(R.dimen.spacing_medium),
                alignment = Alignment.CenterVertically,
            ),
    ) {
        Text(
            text = androidx.compose.ui.res.stringResource(R.string.tour_search_empty_title),
            color = colorResource(R.color.text_color),
        )
        EditButton(
            text = R.string.tour_search_clear_filters,
            onClick = onClearFilters,
            modifier = Modifier.fillMaxWidth(0.7f),
        )
    }
}

@Composable
private fun GuidesContent(
    modifier: Modifier = Modifier,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    guides: List<GuideResultUiModel>,
    loadState: ContentLoadState,
    isLoadingMore: Boolean,
    appendFailed: Boolean,
    canLoadMore: Boolean,
    onRetry: () -> Unit,
    onLoadMore: () -> Unit,
    onGuideClick: (Long) -> Unit,
) {
    Column(
        modifier = modifier.padding(dimensionResource(R.dimen.spacing_medium)),
    ) {
        EditTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            colors =
                OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorResource(R.color.brand_color),
                    unfocusedBorderColor = colorResource(R.color.brand_color),
                    cursorColor = colorResource(R.color.brand_color),
                ),
            placeholder = R.string.search_guide,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = Color.Gray,
                )
            },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))
        GuideMateContentState(
            state = loadState,
            onRetry = onRetry,
            modifier = Modifier.fillMaxWidth().weight(1f),
        ) {
            if (guides.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = androidx.compose.ui.res.stringResource(R.string.guide_search_empty),
                        color = colorResource(R.color.text_color),
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(guides, key = { it.guideId }) { guide ->
                        GuideResultCard(
                            guide = guide,
                            onClick = { onGuideClick(guide.guideId) },
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_tiny)))
                    }
                    if (isLoadingMore) {
                        item {
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(dimensionResource(R.dimen.spacing_medium)),
                                contentAlignment = Alignment.Center,
                            ) {
                                CircularProgressIndicator(color = colorResource(R.color.brand_color))
                            }
                        }
                    } else if (appendFailed) {
                        item {
                            GuideMateContentState(
                                state = ContentLoadState.ERROR,
                                onRetry = onLoadMore,
                                modifier = Modifier.fillMaxWidth().height(112.dp),
                            ) {}
                        }
                    } else if (canLoadMore) {
                        item {
                            androidx.compose.runtime.LaunchedEffect(guides.size) {
                                onLoadMore()
                            }
                        }
                    }
                }
            }
        }
    }
}
