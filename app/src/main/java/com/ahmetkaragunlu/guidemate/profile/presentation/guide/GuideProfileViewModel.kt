package com.ahmetkaragunlu.guidemate.profile.presentation.guide

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.location.locale.LocaleSelectionCatalog
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.error.toMessage
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationType
import com.ahmetkaragunlu.guidemate.notification.domain.repository.NotificationRepository
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuideProfile
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuideProfileUpdate
import com.ahmetkaragunlu.guidemate.profile.domain.model.level.GuideLevelTier
import com.ahmetkaragunlu.guidemate.profile.domain.repository.GuideProfileRepository
import com.ahmetkaragunlu.guidemate.profile.domain.repository.UserAvatarRepository
import com.ahmetkaragunlu.guidemate.profile.presentation.guide.model.GuideProfileUiState
import com.ahmetkaragunlu.guidemate.profile.presentation.model.GuideSpokenLanguageUi
import com.ahmetkaragunlu.guidemate.tour.domain.model.discovery.TourSearchItem
import com.ahmetkaragunlu.guidemate.tour.domain.repository.TourDiscoveryRepository
import com.ahmetkaragunlu.guidemate.tour.presentation.mapper.toPopularTourCardUiModel
import com.ahmetkaragunlu.guidemate.tour.presentation.model.PopularTourCardUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class GuideProfileViewModel
    @Inject
    constructor(
        private val profileRepository: GuideProfileRepository,
        private val userAvatarRepository: UserAvatarRepository,
        private val resourceProvider: ResourceProvider,
        private val tourRepository: TourDiscoveryRepository,
        notificationRepository: NotificationRepository,
    ) : ViewModel() {
        private val operationState =
            MutableStateFlow(
                GuideProfileOperationState(
                    loadState =
                        if (profileRepository.cachedOwnProfile == null) {
                            ContentLoadState.LOADING
                        } else {
                            ContentLoadState.CONTENT
                        },
                ),
            )
        private val popularTours = MutableStateFlow<List<TourSearchItem>>(emptyList())
        private var refreshJob: Job? = null

        val profileState: StateFlow<GuideProfileUiState> =
            combine(
                profileRepository.ownProfile,
                popularTours,
                operationState,
            ) { profile, tours, operation ->
                profile.toUiState(
                    loadState = operation.loadState,
                    selectedProfileImageUri = operation.selectedProfileImageUri,
                    isAvatarUpdating = operation.isAvatarUpdating,
                    userMessage = operation.userMessage,
                    popularTours = tours.map { it.toPopularTourCardUiModel() },
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue =
                    profileRepository.cachedOwnProfile.toUiState(
                        loadState = operationState.value.loadState,
                    ),
            )

        init {
            profileRepository.cachedOwnProfile?.guideId?.let(::refreshPopularTours)
            refreshProfile()
            viewModelScope.launch {
                notificationRepository.pushEvents.collect { target ->
                    if (
                        target.type == NotificationType.RATING_RECEIVED ||
                            target.type == NotificationType.COMMENT_RECEIVED ||
                            target.type == NotificationType.TOUR_COMPLETED
                    ) {
                        refreshProfile()
                    }
                }
            }
        }

        fun refreshProfile() {
            if (refreshJob?.isActive == true) return
            refreshJob =
                viewModelScope.launch {
                    val hasCachedProfile = profileRepository.cachedOwnProfile != null
                    if (!hasCachedProfile) {
                        operationState.update { it.copy(loadState = ContentLoadState.LOADING) }
                    }
                    when (val result = profileRepository.refreshOwnProfile()) {
                        is DataResult.Success -> {
                            operationState.update { it.copy(loadState = ContentLoadState.CONTENT) }
                            refreshPopularTours(result.data.guideId)
                        }
                        is DataResult.Error -> {
                            if (hasCachedProfile) {
                                operationState.update {
                                    it.copy(userMessage = result.error.toMessage(resourceProvider))
                                }
                            } else {
                                operationState.update { it.copy(loadState = ContentLoadState.ERROR) }
                            }
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

        fun onProfileImageSelected(uri: String) {
            if (operationState.value.isAvatarUpdating) return
            operationState.update {
                it.copy(selectedProfileImageUri = uri, isAvatarUpdating = true)
            }
            viewModelScope.launch {
                when (val result = userAvatarRepository.updateAvatar(uri)) {
                    is DataResult.Error -> {
                        operationState.update {
                            it.copy(userMessage = result.error.toMessage(resourceProvider))
                        }
                    }
                    is DataResult.Success -> {
                        operationState.update {
                            it.copy(
                                userMessage =
                                    resourceProvider.getString(R.string.profile_photo_update_success),
                            )
                        }
                    }
                }
                operationState.update {
                    it.copy(selectedProfileImageUri = null, isAvatarUpdating = false)
                }
            }
        }

        fun onUserMessageShown() {
            operationState.update { it.copy(userMessage = null) }
        }
    }

private data class GuideProfileOperationState(
    val loadState: ContentLoadState,
    val selectedProfileImageUri: String? = null,
    val isAvatarUpdating: Boolean = false,
    val userMessage: String? = null,
)

private fun GuideProfile?.toUiState(
    loadState: ContentLoadState,
    selectedProfileImageUri: String? = null,
    isAvatarUpdating: Boolean = false,
    userMessage: String? = null,
    popularTours: List<PopularTourCardUiModel> = emptyList(),
): GuideProfileUiState {
    val locale = Locale.getDefault()
    return GuideProfileUiState(
        guideId = this?.guideId,
        displayName = this?.displayName.orEmpty(),
        profileImageUrl = this?.avatar?.imageUrl,
        selectedProfileImageUri = selectedProfileImageUri,
        title = this?.specialtyTitle.orEmpty(),
        guideLevel = this?.performance?.level ?: GuideLevelTier.APPROVED,
        rating = this?.performance?.averageRating ?: 0.0,
        tourCount = this?.performance?.completedSessionCount ?: 0L,
        biography = this?.biography.orEmpty(),
        spokenLanguages =
            this?.languageCodes.orEmpty().map { code ->
                val language = LocaleSelectionCatalog.language(code, locale)
                GuideSpokenLanguageUi(
                    code = code,
                    displayText = language?.chipLabel ?: code.uppercase(locale),
                )
            },
        popularTours = popularTours,
        loadState = loadState,
        isAvatarUpdating = isAvatarUpdating,
        userMessage = userMessage,
    )
}
