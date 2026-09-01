package com.ahmetkaragunlu.guidemate.tour.presentation.tourist.guide

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.tour.domain.repository.TourDiscoveryRepository
import com.ahmetkaragunlu.guidemate.tour.presentation.mapper.toSearchResultUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class TouristGuideToursViewModel
@Inject
constructor(
    private val repository: TourDiscoveryRepository,
) : ViewModel() {
    private val mutableUiState = MutableStateFlow(TouristGuideToursUiState())
    val uiState: StateFlow<TouristGuideToursUiState> = mutableUiState.asStateFlow()

    private var guideId: Long? = null
    private var currentPage = 0
    private var loadJob: Job? = null

    fun loadGuideTours(guideId: Long) {
        if (this.guideId == guideId &&
            (uiState.value.loadState == ContentLoadState.CONTENT || loadJob?.isActive == true)
        ) {
            return
        }
        this.guideId = guideId
        currentPage = 0
        loadPage(page = 0, append = false)
    }

    fun retry() {
        val currentGuideId = guideId ?: return
        if (uiState.value.appendFailed) {
            loadPage(page = currentPage + 1, append = true)
        } else {
            loadPage(page = 0, append = false, targetGuideId = currentGuideId)
        }
    }

    fun loadMore() {
        val state = uiState.value
        if (!state.canLoadMore || state.isLoadingMore || loadJob?.isActive == true) return
        loadPage(page = currentPage + 1, append = true)
    }

    private fun loadPage(
        page: Int,
        append: Boolean,
        targetGuideId: Long? = guideId,
    ) {
        val currentGuideId = targetGuideId ?: return
        loadJob?.cancel()
        loadJob =
            viewModelScope.launch {
                mutableUiState.update { state ->
                    if (append) {
                        state.copy(isLoadingMore = true, appendFailed = false)
                    } else {
                        TouristGuideToursUiState(loadState = ContentLoadState.LOADING)
                    }
                }
                when (
                    val result =
                        repository.getPopularToursForGuide(
                            guideId = currentGuideId,
                            page = page,
                            size = PAGE_SIZE,
                        )
                ) {
                    is DataResult.Success -> {
                        val mappedTours = result.data.items.map { it.toSearchResultUiModel() }
                        currentPage = result.data.page
                        mutableUiState.update { state ->
                            state.copy(
                                tours = if (append) state.tours + mappedTours else mappedTours,
                                loadState = ContentLoadState.CONTENT,
                                isLoadingMore = false,
                                appendFailed = false,
                                canLoadMore = !result.data.isLast,
                            )
                        }
                    }
                    is DataResult.Error -> {
                        mutableUiState.update { state ->
                            state.copy(
                                loadState =
                                    if (append || state.tours.isNotEmpty()) {
                                        ContentLoadState.CONTENT
                                    } else {
                                        ContentLoadState.ERROR
                                    },
                                isLoadingMore = false,
                                appendFailed = append,
                            )
                        }
                    }
                }
            }
    }

    private companion object {
        const val PAGE_SIZE = 20
    }
}
