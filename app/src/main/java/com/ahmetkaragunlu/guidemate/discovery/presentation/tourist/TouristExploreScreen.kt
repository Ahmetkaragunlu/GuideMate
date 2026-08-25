package com.ahmetkaragunlu.guidemate.discovery.presentation.tourist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.ui.components.GuideMateTabRow
import com.ahmetkaragunlu.guidemate.discovery.presentation.tourist.model.ExploreTab

@Composable
fun TouristExploreScreen(
    modifier: Modifier = Modifier,
    viewModel: TouristExploreViewModel = hiltViewModel(),
    onNavigateToFilter: () -> Unit,
    onNavigateToTourDetail: (String) -> Unit = {},
    onNavigateToGuideProfile: (Long) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize()) {
        GuideMateTabRow(
            tabs = ExploreTab.entries,
            selectedTab = uiState.selectedTab,
            onTabSelected = viewModel::updateSelectedTab,
        )

        when (uiState.selectedTab) {
            ExploreTab.TOURS ->
                TourExploreResults(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.spacing_medium)),
                    searchQuery = uiState.tours.searchQuery,
                    onSearchQueryChange = viewModel::updateToursSearchQuery,
                    onNavigateToFilter = onNavigateToFilter,
                    tours = uiState.tours.results,
                    resultCount = uiState.tours.resultCount,
                    loadState = uiState.tours.loadState,
                    isLoadingMore = uiState.tours.isLoadingMore,
                    appendFailed = uiState.tours.appendFailed,
                    canLoadMore = uiState.tours.canLoadMore,
                    onRetry = viewModel::refreshTours,
                    onLoadMore = viewModel::loadMoreTours,
                    onClearFilters = viewModel::clearSearchAndFilters,
                    onTourClick = onNavigateToTourDetail,
                )

            ExploreTab.GUIDES ->
                GuideExploreResults(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.spacing_medium)),
                    searchQuery = uiState.guides.searchQuery,
                    onSearchQueryChange = viewModel::updateGuidesSearchQuery,
                    guides = uiState.guides.results,
                    loadState = uiState.guides.loadState,
                    isLoadingMore = uiState.guides.isLoadingMore,
                    appendFailed = uiState.guides.appendFailed,
                    canLoadMore = uiState.guides.canLoadMore,
                    onRetry = viewModel::refreshGuides,
                    onLoadMore = viewModel::loadMoreGuides,
                    onGuideClick = onNavigateToGuideProfile,
                )
        }
    }
}
