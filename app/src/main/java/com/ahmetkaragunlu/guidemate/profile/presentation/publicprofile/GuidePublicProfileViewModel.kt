package com.ahmetkaragunlu.guidemate.profile.presentation.publicprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.error.toMessage
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.chat.domain.repository.ChatRepository
import com.ahmetkaragunlu.guidemate.profile.domain.repository.GuideProfileRepository
import com.ahmetkaragunlu.guidemate.profile.presentation.mapper.toProfileContentUiState
import com.ahmetkaragunlu.guidemate.profile.presentation.model.GuideProfileContentUiState
import com.ahmetkaragunlu.guidemate.review.domain.repository.ReviewRepository
import com.ahmetkaragunlu.guidemate.tour.domain.repository.TourDiscoveryRepository
import com.ahmetkaragunlu.guidemate.tour.presentation.mapper.toPopularTourCardUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class GuidePublicProfileViewModel
    @Inject
    constructor(
        private val profileRepository: GuideProfileRepository,
        private val tourRepository: TourDiscoveryRepository,
        private val reviewRepository: ReviewRepository,
        private val chatRepository: ChatRepository,
        private val resourceProvider: ResourceProvider,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(GuideProfileContentUiState())
        val uiState: StateFlow<GuideProfileContentUiState> = _uiState.asStateFlow()

        private var requestedGuideId: Long? = null
        private var loadJob: Job? = null
        private var startChatJob: Job? = null
        private val mutableChatDestinations = MutableSharedFlow<String>()
        val chatDestinations: SharedFlow<String> = mutableChatDestinations.asSharedFlow()
        private val mutableChatErrors = MutableSharedFlow<String>()
        val chatErrors: SharedFlow<String> = mutableChatErrors.asSharedFlow()

        init {
            observeReviewChanges()
        }

        private fun observeReviewChanges() {
            viewModelScope.launch {
                reviewRepository.reviewChanges.collect { retry() }
            }
        }

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
                            is DataResult.Success -> {
                                val popularTours = loadPopularTours(guideId)
                                result.data.toProfileContentUiState(
                                    loadState = ContentLoadState.CONTENT,
                                    popularTours = popularTours,
                                )
                            }
                            is DataResult.Error ->
                                GuideProfileContentUiState(loadState = ContentLoadState.ERROR)
                        }
                }
        }

        private suspend fun loadPopularTours(guideId: Long) =
            when (val result = tourRepository.getPopularTours(page = 0, size = 20)) {
                is DataResult.Success ->
                    result.data.items
                        .filter { it.guide.id == guideId.toString() }
                        .map { it.toPopularTourCardUiModel() }
                is DataResult.Error -> emptyList()
            }

        fun retry() {
            requestedGuideId?.let { guideId ->
                requestedGuideId = null
                loadGuide(guideId)
            }
        }

        fun startChat(guideId: Long) {
            if (startChatJob?.isActive == true) return
            startChatJob =
                viewModelScope.launch {
                    when (val result = chatRepository.findOrCreate(guideId)) {
                        is DataResult.Success -> mutableChatDestinations.emit(result.data.chatId)
                        is DataResult.Error ->
                            mutableChatErrors.emit(result.error.toMessage(resourceProvider))
                    }
                }
        }
    }
