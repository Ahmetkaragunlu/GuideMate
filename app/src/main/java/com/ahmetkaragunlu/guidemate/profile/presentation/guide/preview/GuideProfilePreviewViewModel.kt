package com.ahmetkaragunlu.guidemate.profile.presentation.guide.preview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.profile.domain.repository.GuideProfileRepository
import com.ahmetkaragunlu.guidemate.profile.presentation.mapper.toProfileContentUiState
import com.ahmetkaragunlu.guidemate.profile.presentation.model.GuideProfileContentUiState
import com.ahmetkaragunlu.guidemate.tour.data.mock.MOCK_CURRENT_GUIDE_ID
import com.ahmetkaragunlu.guidemate.tour.data.mock.TourCatalogStore
import com.ahmetkaragunlu.guidemate.tour.data.mock.refreshAtSessionTransitions
import com.ahmetkaragunlu.guidemate.tour.presentation.mapper.toPopularTourCardUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class GuideProfilePreviewViewModel
    @Inject
    constructor(
        private val profileRepository: GuideProfileRepository,
        tourCatalogStore: TourCatalogStore,
    ) : ViewModel() {
        private val loadState =
            MutableStateFlow(
                if (profileRepository.cachedOwnProfile == null) {
                    ContentLoadState.LOADING
                } else {
                    ContentLoadState.CONTENT
                },
            )
        private var refreshJob: Job? = null

        val uiState: StateFlow<GuideProfileContentUiState> =
            combine(
                profileRepository.ownProfile,
                tourCatalogStore.state.refreshAtSessionTransitions(),
                loadState,
            ) { profile, catalog, state ->
                profile.toProfileContentUiState(
                    loadState = state,
                    popularTours =
                        catalog
                            .bookableTourItemsForGuideAt(
                                guideId = MOCK_CURRENT_GUIDE_ID,
                                now = Instant.now(),
                            ).map { it.toPopularTourCardUiModel() },
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue =
                    profileRepository.cachedOwnProfile.toProfileContentUiState(
                        loadState = loadState.value,
                    ),
            )

        init {
            refreshProfile()
        }

        fun refreshProfile() {
            if (refreshJob?.isActive == true) return
            refreshJob =
                viewModelScope.launch {
                    val hasCachedProfile = profileRepository.cachedOwnProfile != null
                    if (!hasCachedProfile) loadState.value = ContentLoadState.LOADING
                    loadState.value =
                        when (profileRepository.refreshOwnProfile()) {
                            is DataResult.Success -> ContentLoadState.CONTENT
                            is DataResult.Error ->
                                if (hasCachedProfile) {
                                    ContentLoadState.CONTENT
                                } else {
                                    ContentLoadState.ERROR
                                }
                        }
                }
        }
    }
