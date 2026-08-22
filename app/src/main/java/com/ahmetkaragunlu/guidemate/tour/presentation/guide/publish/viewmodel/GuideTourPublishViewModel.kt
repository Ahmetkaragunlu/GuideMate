package com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.location.model.LanguageOption
import com.ahmetkaragunlu.guidemate.common.location.model.LocationOption
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.error.toMessage
import com.ahmetkaragunlu.guidemate.common.ui.formatting.isValidCurrencyInput
import com.ahmetkaragunlu.guidemate.common.ui.formatting.toCurrencyMinorUnitsOrNull
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaPurpose
import com.ahmetkaragunlu.guidemate.media.domain.repository.MediaRepository
import com.ahmetkaragunlu.guidemate.profile.domain.repository.GuideProfileRepository
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.CreateGuideTourInput
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.TourContentInput
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.TourSessionInput
import com.ahmetkaragunlu.guidemate.tour.domain.repository.GuideTourRepository
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.model.toTourLanguage
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.model.GuideTourPublishStep
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.model.GuideTourPublishUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class GuideTourPublishViewModel
    @Inject
    constructor(
        private val repository: GuideTourRepository,
        private val mediaRepository: MediaRepository,
        private val profileRepository: GuideProfileRepository,
        private val resourceProvider: ResourceProvider,
    ) : ViewModel() {
        private val draftState = MutableStateFlow(GuideTourPublishUiState())
        val uiState: StateFlow<GuideTourPublishUiState> =
            combine(draftState, profileRepository.ownProfile) { draft, profile ->
                draft.copy(
                    guideName = profile?.displayName.orEmpty(),
                    guideImageResId = R.drawable.unnamed,
                    guideImageUrl = profile?.avatar?.imageUrl,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue =
                    draftState.value.copy(
                        guideName = profileRepository.cachedOwnProfile?.displayName.orEmpty(),
                        guideImageUrl = profileRepository.cachedOwnProfile?.avatar?.imageUrl,
                    ),
            )

        init {
            if (profileRepository.cachedOwnProfile == null) {
                viewModelScope.launch { profileRepository.refreshOwnProfile() }
            }
        }

        fun onTourDateSelected(date: LocalDate) {
            updateDraft {
                val zoneId = timeZoneId.toZoneId()
                val today = LocalDate.now(zoneId)
                val currentTime = LocalTime.now(zoneId)
                copy(
                    tourDate = date,
                    startTime = startTime?.takeIf { date != today || it.isAfter(currentTime) },
                )
            }
        }

        fun onStartTimeSelected(time: LocalTime) = updateDraft { copy(startTime = time) }

        fun onDurationSelected(durationMinutes: Int) =
            updateDraft { copy(durationMinutes = durationMinutes) }

        fun onLocationSelected(location: LocationOption) {
            updateDraft {
                copy(
                    countryCode = location.country.code,
                    country = location.country.displayName,
                    cityPlaceId = location.city.placeId,
                    city = location.city.displayName,
                    timeZoneId = ZoneId.systemDefault().id,
                )
            }
        }

        fun onLanguagesSelected(languages: List<LanguageOption>) {
            updateDraft { copy(spokenLanguages = languages.map(LanguageOption::toTourLanguage)) }
        }

        fun onCategorySelected(category: TourCategory) = updateDraft { copy(category = category) }

        fun onRemoveLanguageClick(code: String) {
            updateDraft {
                if (spokenLanguages.size <= 1) {
                    this
                } else {
                    copy(spokenLanguages = spokenLanguages.filterNot { it.code == code })
                }
            }
        }

        fun onPriceChange(input: String) {
            if (input.isValidCurrencyInput()) updateDraft { copy(price = input) }
        }

        fun onCapacityChange(input: String) {
            if (input.all(Char::isDigit)) updateDraft { copy(capacity = input) }
        }

        fun onTourNameChange(value: String) = updateDraft { copy(tourName = value) }

        fun onTourDescriptionChange(value: String) =
            updateDraft { copy(tourDescription = value) }

        fun onCoverImageSelected(uri: String) =
            updateDraft { copy(selectedCoverImageUri = uri) }

        fun onMeetingPointChange(value: String) = updateDraft { copy(meetingPoint = value) }

        fun validateStep1(): Boolean =
            validateStep(
                isValid = draftState.value.isStep1Valid(),
                step = GuideTourPublishStep.LOCATION_AND_TIME,
                errorResId = R.string.error_tour_step1_invalid,
            )

        fun validateStep2(): Boolean =
            validateStep(
                isValid = draftState.value.isStep2Valid(),
                step = GuideTourPublishStep.TECHNICAL_DETAILS,
                errorResId = R.string.error_tour_step2_invalid,
            )

        fun validateStep3(): Boolean =
            validateStep(
                isValid = draftState.value.isStep3Valid(),
                step = GuideTourPublishStep.CONTENT_AND_MEDIA,
                errorResId = R.string.error_tour_step3_invalid,
            )

        fun onPublishClick() {
            val form = draftState.value
            if (form.isPublishing || form.publishSucceeded) return
            val inputWithoutCover = form.toCreateInputOrNull(coverMediaId = null)
            val imageUri = form.selectedCoverImageUri
            if (inputWithoutCover == null || imageUri == null) {
                showValidationError(R.string.error_tour_schedule_invalid, GuideTourPublishStep.PREVIEW)
                return
            }

            draftState.update {
                it.copy(
                    isPublishing = true,
                    submissionErrorMessage = null,
                    validationErrorStep = null,
                    validationErrorResId = null,
                )
            }
            viewModelScope.launch {
                when (val upload = mediaRepository.uploadImage(imageUri, MediaPurpose.TOUR_COVER)) {
                    is DataResult.Error -> finishWithError(upload.error.toMessage(resourceProvider))
                    is DataResult.Success -> {
                        val input =
                            draftState.value.toCreateInputOrNull(upload.data.mediaAssetId)
                        if (input == null) {
                            mediaRepository.deleteUnreferenced(upload.data.mediaAssetId)
                            finishWithError(resourceProvider.getString(R.string.error_tour_schedule_invalid))
                            return@launch
                        }
                        when (val result = repository.createTour(input)) {
                            is DataResult.Success -> {
                                draftState.update {
                                    it.copy(isPublishing = false, publishSucceeded = true)
                                }
                            }
                            is DataResult.Error -> {
                                mediaRepository.deleteUnreferenced(upload.data.mediaAssetId)
                                finishWithError(result.error.toMessage(resourceProvider))
                            }
                        }
                    }
                }
            }
        }

        fun onPublishSucceededHandled() {
            draftState.update { it.copy(publishSucceeded = false) }
        }

        private fun validateStep(
            isValid: Boolean,
            step: GuideTourPublishStep,
            @StringRes errorResId: Int,
        ): Boolean {
            if (!isValid) return showValidationError(errorResId, step)
            draftState.update { it.copy(validationErrorStep = null, validationErrorResId = null) }
            return true
        }

        private fun showValidationError(
            @StringRes messageResId: Int,
            step: GuideTourPublishStep,
        ): Boolean {
            draftState.update {
                it.copy(validationErrorStep = step, validationErrorResId = messageResId)
            }
            return false
        }

        private fun finishWithError(message: String) {
            draftState.update { it.copy(isPublishing = false, submissionErrorMessage = message) }
        }

        private fun updateDraft(transform: GuideTourPublishUiState.() -> GuideTourPublishUiState) {
            draftState.update { state ->
                state.transform().copy(
                    validationErrorStep = null,
                    validationErrorResId = null,
                    submissionErrorMessage = null,
                )
            }
        }
    }

private fun GuideTourPublishUiState.isStep1Valid(): Boolean =
    countryCode.isNotBlank() &&
        country.isNotBlank() &&
        cityPlaceId.isNotBlank() &&
        city.isNotBlank() &&
        timeZoneId.isNotBlank() &&
        toStartInstant()?.isAfter(Instant.now()) == true &&
        durationMinutes?.let { it > 0 } == true

private fun GuideTourPublishUiState.isStep2Valid(): Boolean =
    category != null &&
        spokenLanguages.isNotEmpty() &&
        price.toCurrencyMinorUnitsOrNull()?.let { it > 0 } == true &&
        capacity.toIntOrNull()?.let { it > 0 } == true

private fun GuideTourPublishUiState.isStep3Valid(): Boolean =
    tourName.isNotBlank() &&
        selectedCoverImageUri != null &&
        tourDescription.isNotBlank() &&
        meetingPoint.isNotBlank()

private fun GuideTourPublishUiState.toCreateInputOrNull(coverMediaId: String?): CreateGuideTourInput? {
    if (!isStep1Valid() || !isStep2Valid() || !isStep3Valid()) return null
    val selectedCategory = category ?: return null
    val startsAt = toStartInstant() ?: return null
    val duration = durationMinutes ?: return null
    val amount = price.toCurrencyMinorUnitsOrNull() ?: return null
    val participantCapacity = capacity.toIntOrNull() ?: return null
    return CreateGuideTourInput(
        content =
            TourContentInput(
                title = tourName.trim(),
                description = tourDescription.trim(),
                countryCode = countryCode,
                cityPlaceId = cityPlaceId,
                cityName = city,
                timeZoneId = timeZoneId,
                category = selectedCategory,
                languageCodes = spokenLanguages.map { it.code },
                coverMediaId = coverMediaId.orEmpty(),
            ),
        session =
            TourSessionInput(
                meetingPoint = meetingPoint.trim(),
                startsAt = startsAt,
                durationMinutes = duration,
                priceMinor = amount,
                capacity = participantCapacity,
            ),
    )
}

private fun GuideTourPublishUiState.toStartInstant(): Instant? {
    val date = tourDate ?: return null
    val time = startTime ?: return null
    return runCatching { date.atTime(time).atZone(timeZoneId.toZoneId()).toInstant() }.getOrNull()
}

private fun String.toZoneId(): ZoneId =
    runCatching { ZoneId.of(this) }.getOrDefault(ZoneId.systemDefault())
