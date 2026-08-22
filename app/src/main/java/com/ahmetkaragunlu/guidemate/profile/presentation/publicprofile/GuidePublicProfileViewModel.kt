package com.ahmetkaragunlu.guidemate.profile.presentation.publicprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.profile.domain.repository.GuideProfileRepository
import com.ahmetkaragunlu.guidemate.profile.presentation.mapper.toProfileContentUiState
import com.ahmetkaragunlu.guidemate.profile.presentation.model.GuideProfileContentUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class GuidePublicProfileViewModel
    @Inject
    constructor(
        private val profileRepository: GuideProfileRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(GuideProfileContentUiState())
        val uiState: StateFlow<GuideProfileContentUiState> = _uiState.asStateFlow()

        private var requestedGuideId: Long? = null
        private var loadJob: Job? = null

        fun loadGuide(guideId: Long) {
            if (requestedGuideId == guideId &&
                (_uiState.value.loadState == ContentLoadState.CONTENT || loadJob?.isActive == true)
            ) {
                return
            }
            requestedGuideId = guideId
            loadJob?.cancel()
            loadJob =
                viewModelScope.launch {
                    _uiState.value = GuideProfileContentUiState(loadState = ContentLoadState.LOADING)
                    _uiState.value =
                        when (val result = profileRepository.getPublicProfile(guideId)) {
                            is DataResult.Success ->
                                result.data.toProfileContentUiState(loadState = ContentLoadState.CONTENT)
                            is DataResult.Error ->
                                GuideProfileContentUiState(loadState = ContentLoadState.ERROR)
                        }
                }
        }

        fun retry() {
            requestedGuideId?.let { guideId ->
                requestedGuideId = null
                loadGuide(guideId)
            }
        }
    }
