package com.ahmetkaragunlu.guidemate.profile.presentation.guide

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.location.data.LocaleSelectionCatalog
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.error.toMessage
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaPurpose
import com.ahmetkaragunlu.guidemate.media.domain.repository.MediaRepository
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuideProfile
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuideProfileUpdate
import com.ahmetkaragunlu.guidemate.profile.domain.model.level.GuideLevelTier
import com.ahmetkaragunlu.guidemate.profile.domain.repository.GuideProfileRepository
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
import kotlinx.coroutines.launch

@HiltViewModel
class GuideProfileViewModel
    @Inject
    constructor(
        private val profileRepository: GuideProfileRepository,
        private val mediaRepository: MediaRepository,
        private val resourceProvider: ResourceProvider,
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
        private val selectedProfileImageUri = MutableStateFlow<String?>(null)
        private val isAvatarUpdating = MutableStateFlow(false)
        private val userMessage = MutableStateFlow<String?>(null)
        private val popularTours = MutableStateFlow<List<TourSearchItem>>(emptyList())
        private var refreshJob: Job? = null

        val profileState: StateFlow<GuideProfileUiState> =
            combine(
                profileRepository.ownProfile,
                popularTours,
                selectedProfileImageUri,
                isAvatarUpdating,
                combine(loadState, userMessage, ::Pair),
            ) { profile, tours, selectedImageUri, avatarUpdating, request ->
                profile.toUiState(
                    loadState = request.first,
                    selectedProfileImageUri = selectedImageUri,
                    isAvatarUpdating = avatarUpdating,
                    userMessage = request.second,
                    popularTours = tours.map { it.toPopularTourCardUiModel() },
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue =
                    profileRepository.cachedOwnProfile.toUiState(
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
                    when (val result = profileRepository.refreshOwnProfile()) {
                        is DataResult.Success -> {
                            loadState.value = ContentLoadState.CONTENT
                            refreshPopularTours(result.data.guideId)
                        }
                        is DataResult.Error -> {
                            if (hasCachedProfile) {
                                userMessage.value = result.error.toMessage(resourceProvider)
                            } else {
                                loadState.value = ContentLoadState.ERROR
                            }
                        }
                    }
                }
        }

        private fun refreshPopularTours(guideId: Long) {
            viewModelScope.launch {
                when (val result = tourRepository.getPopularTours(page = 0, size = 20)) {
                    is DataResult.Success -> {
                        popularTours.value =
                            result.data.items.filter { it.guide.id == guideId.toString() }
                    }
                    is DataResult.Error -> Unit
                }
            }
        }

        fun onProfileImageSelected(uri: String) {
            if (isAvatarUpdating.value) return
            val currentProfile = profileRepository.cachedOwnProfile
            if (currentProfile == null ||
                currentProfile.specialtyTitle.length !in
                    GuideProfileUpdate.MIN_SPECIALTY_TITLE_LENGTH..
                        GuideProfileUpdate.MAX_SPECIALTY_TITLE_LENGTH ||
                currentProfile.biography.length !in
                    GuideProfileUpdate.MIN_BIOGRAPHY_LENGTH..
                        GuideProfileUpdate.MAX_BIOGRAPHY_LENGTH
            ) {
                userMessage.value =
                    resourceProvider.getString(R.string.guide_profile_complete_about_first)
                return
            }
            selectedProfileImageUri.value = uri
            isAvatarUpdating.value = true
            viewModelScope.launch {
                when (val uploadResult = mediaRepository.uploadImage(uri, MediaPurpose.GUIDE_AVATAR)) {
                    is DataResult.Error -> {
                        userMessage.value = uploadResult.error.toMessage(resourceProvider)
                    }
                    is DataResult.Success -> {
                        val profile = profileRepository.cachedOwnProfile
                        if (profile == null) {
                            mediaRepository.deleteUnreferenced(uploadResult.data.mediaAssetId)
                            userMessage.value = resourceProvider.getString(R.string.error_generic_failure)
                        } else {
                            val updateResult =
                                profileRepository.updateOwnProfile(
                                    profile.toUpdate(uploadResult.data.mediaAssetId),
                                )
                            if (updateResult is DataResult.Error) {
                                mediaRepository.deleteUnreferenced(uploadResult.data.mediaAssetId)
                                userMessage.value = updateResult.error.toMessage(resourceProvider)
                            } else {
                                userMessage.value =
                                    resourceProvider.getString(R.string.guide_profile_update_success)
                            }
                        }
                    }
                }
                selectedProfileImageUri.value = null
                isAvatarUpdating.value = false
            }
        }

        fun onUserMessageShown() {
            userMessage.value = null
        }
    }

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

private fun GuideProfile.toUpdate(avatarMediaId: String): GuideProfileUpdate =
    GuideProfileUpdate(
        specialtyTitle = specialtyTitle,
        biography = biography,
        languageCodes = languageCodes,
        avatarMediaId = avatarMediaId,
    )
