package com.ahmetkaragunlu.guidemate.discovery.presentation.tourist

import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.discovery.presentation.tourist.model.ExploreUiState
import com.ahmetkaragunlu.guidemate.profile.presentation.model.GuideResultUiModel
import com.ahmetkaragunlu.guidemate.tour.presentation.model.TourSearchResultUiModel

internal fun ExploreUiState.startTourLoad(append: Boolean): ExploreUiState =
    copy(
        tours =
            if (append) {
                tours.copy(isLoadingMore = true, appendFailed = false)
            } else {
                tours.copy(
                    loadState = ContentLoadState.LOADING,
                    results = emptyList(),
                    resultCount = 0,
                    isLoadingMore = false,
                    appendFailed = false,
                    canLoadMore = false,
                )
            },
    )

internal fun ExploreUiState.completeTourLoad(
    results: List<TourSearchResultUiModel>,
    resultCount: Long,
    canLoadMore: Boolean,
    append: Boolean,
): ExploreUiState =
    copy(
        tours =
            tours.copy(
                results = if (append) tours.results + results else results,
                resultCount = resultCount,
                loadState = ContentLoadState.CONTENT,
                isLoadingMore = false,
                appendFailed = false,
                canLoadMore = canLoadMore,
            ),
    )

internal fun ExploreUiState.failTourLoad(append: Boolean): ExploreUiState =
    copy(
        tours =
            tours.copy(
                loadState =
                    if (append || tours.results.isNotEmpty()) {
                        ContentLoadState.CONTENT
                    } else {
                        ContentLoadState.ERROR
                    },
                isLoadingMore = false,
                appendFailed = append,
            ),
    )

internal fun ExploreUiState.startGuideLoad(append: Boolean): ExploreUiState =
    copy(
        guides =
            if (append) {
                guides.copy(isLoadingMore = true, appendFailed = false)
            } else {
                guides.copy(
                    loadState = ContentLoadState.LOADING,
                    results = emptyList(),
                    isLoadingMore = false,
                    appendFailed = false,
                    canLoadMore = false,
                )
            },
    )

internal fun ExploreUiState.completeGuideLoad(
    results: List<GuideResultUiModel>,
    canLoadMore: Boolean,
    append: Boolean,
): ExploreUiState =
    copy(
        guides =
            guides.copy(
                results = if (append) guides.results + results else results,
                loadState = ContentLoadState.CONTENT,
                isLoadingMore = false,
                appendFailed = false,
                canLoadMore = canLoadMore,
            ),
    )

internal fun ExploreUiState.failGuideLoad(append: Boolean): ExploreUiState =
    copy(
        guides =
            guides.copy(
                loadState =
                    if (append || guides.results.isNotEmpty()) {
                        ContentLoadState.CONTENT
                    } else {
                        ContentLoadState.ERROR
                    },
                isLoadingMore = false,
                appendFailed = append,
            ),
    )
