package com.ahmetkaragunlu.guidemate.profile.presentation.guide.preview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.profile.domain.repository.GuideProfileRepository
import com.ahmetkaragunlu.guidemate.profile.presentation.mapper.toProfileContentUiState
import com.ahmetkaragunlu.guidemate.profile.presentation.model.GuideProfileContentUiState
import com.ahmetkaragunlu.guidemate.tour.domain.model.discovery.TourSearchItem
import com.ahmetkaragunlu.guidemate.tour.domain.repository.TourDiscoveryRepository
import com.ahmetkaragunlu.guidemate.tour.presentation.mapper.toPopularTourCardUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
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
        private val tourRepository: TourDiscoveryRepository,
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
        private val popularTours = MutableStateFlow<List<TourSearchItem>>(emptyList())

        val uiState: StateFlow<GuideProfileContentUiState> =
            combine(
                profileRepository.ownProfile,
                popularTours,
                loadState,
            ) { profile, tours, state ->
                profile.toProfileContentUiState(
                    loadState = state,
                    popularTours = tours.map { it.toPopularTourCardUiModel() },
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
            profileRepository.cachedOwnProfile?.guideId?.let(::refreshPopularTours)
            refreshProfile()
        }

        fun refreshProfile() {
            if (refreshJob?.isActive == true) return
            refreshJob =
                viewModelScope.launch {
                    val hasCachedProfile = profileRepository.cachedOwnProfile != null
                    if (!hasCachedProfile) loadState.value = ContentLoadState.LOADING
                    loadState.value =
                        when (val result = profileRepository.refreshOwnProfile()) {
                            is DataResult.Success -> {
                                refreshPopularTours(result.data.guideId)
                                ContentLoadState.CONTENT
                            }
                            is DataResult.Error ->
                                if (hasCachedProfile) {
                                    ContentLoadState.CONTENT
                                } else {
                                    ContentLoadState.ERROR
                                }
                        }
                }
        }

        private fun refreshPopularTours(guideId: Long) {
            viewModelScope.launch {
                when (
                    val result =
                        tourRepository.getPopularToursForGuide(
                            guideId = guideId,
                            page = 0,
                            size = 20,
                        )
                ) {
                    is DataResult.Success -> {
                        popularTours.value = result.data.items
                    }
                    is DataResult.Error -> Unit
                }
            }
        }
    }
