package com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.location.model.LanguageOption
import com.ahmetkaragunlu.guidemate.common.location.model.LocationOption
import com.ahmetkaragunlu.guidemate.common.result.AppError
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
import com.ahmetkaragunlu.guidemate.tour.presentation.mapper.toTourLanguage
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
                    guide =
                        draft.guide.copy(
                            name = profile?.displayName.orEmpty(),
                            imageResId = R.drawable.ic_default_avatar,
                            imageUrl = profile?.avatar?.imageUrl,
                        ),
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue =
                    draftState.value.copy(
                        guide =
                            draftState.value.guide.copy(
                                name = profileRepository.cachedOwnProfile?.displayName.orEmpty(),
                                imageUrl = profileRepository.cachedOwnProfile?.avatar?.imageUrl,
                            ),
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
                    session =
                        session.copy(
                            tourDate = date,
                            startTime = startTime?.takeIf { date != today || it.isAfter(currentTime) },
                        ),
                )
            }
        }

        fun onStartTimeSelected(time: LocalTime) =
            updateDraft { copy(session = session.copy(startTime = time)) }

        fun onDurationSelected(durationMinutes: Int) =
            updateDraft { copy(session = session.copy(durationMinutes = durationMinutes)) }

        fun onLocationSelected(location: LocationOption) {
            updateDraft {
                copy(
                    location =
                        this.location.copy(
                            countryCode = location.country.code,
                            country = location.country.displayName,
                            cityPlaceId = location.city.placeId,
                            city = location.city.displayName,
                            timeZoneId = ZoneId.systemDefault().id,
                        ),
                )
            }
        }

        fun onLanguagesSelected(languages: List<LanguageOption>) {
            updateDraft {
                copy(
                    content =
                        content.copy(
                            spokenLanguages = languages.map(LanguageOption::toTourLanguage),
                        ),
                )
            }
        }

        fun onCategorySelected(category: TourCategory) =
            updateDraft { copy(content = content.copy(category = category)) }

        fun onRemoveLanguageClick(code: String) {
            updateDraft {
                if (spokenLanguages.size <= 1) {
                    this
                } else {
                    copy(
                        content =
                            content.copy(
                                spokenLanguages = spokenLanguages.filterNot { it.code == code },
                            ),
                    )
                }
            }
        }

        fun onPriceChange(input: String) {
            if (input.isValidCurrencyInput()) {
                updateDraft { copy(session = session.copy(price = input)) }
            }
        }

        fun onCapacityChange(input: String) {
            if (input.all(Char::isDigit)) {
                updateDraft { copy(session = session.copy(capacity = input)) }
            }
        }

        fun onTourNameChange(value: String) =
            updateDraft { copy(content = content.copy(tourName = value)) }

        fun onTourDescriptionChange(value: String) =
            updateDraft { copy(content = content.copy(tourDescription = value)) }

        fun onCoverImageSelected(uri: String) =
            updateDraft { copy(content = content.copy(selectedCoverImageUri = uri)) }

        fun onMeetingPointChange(value: String) =
            updateDraft { copy(session = session.copy(meetingPoint = value)) }

        fun validateStep1(): Boolean =
            validateStep(GuideTourPublishStep.LOCATION_AND_TIME)

        fun validateStep2(): Boolean =
            validateStep(GuideTourPublishStep.TECHNICAL_DETAILS)

        fun validateStep3(): Boolean =
            validateStep(GuideTourPublishStep.CONTENT_AND_MEDIA)

        fun onPublishClick() {
            val form = draftState.value
            if (form.isPublishing || form.publishSucceeded) return
            form.firstValidationError()?.let { error ->
                showValidationError(error.messageResId, error.step)
                return
            }
            val inputWithoutCover = form.toCreateInputOrNull(coverMediaId = null)
            val imageUri = form.selectedCoverImageUri
            if (inputWithoutCover == null || imageUri == null) {
                showValidationError(R.string.error_tour_schedule_invalid, GuideTourPublishStep.PREVIEW)
                return
            }

            draftState.update {
                it.copy(
                    submission =
                        it.submission.copy(
                            isPublishing = true,
                            errorMessage = null,
                            validationErrorStep = null,
                            validationErrorResId = null,
                        ),
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
                                    it.copy(
                                        submission =
                                            it.submission.copy(
                                                isPublishing = false,
                                                succeeded = true,
                                            ),
                                    )
                                }
                            }
                            is DataResult.Error -> {
                                mediaRepository.deleteUnreferenced(upload.data.mediaAssetId)
                                finishWithError(result.error)
                            }
                        }
                    }
                }
            }
        }

        fun onPublishSucceededHandled() {
            draftState.update {
                it.copy(submission = it.submission.copy(succeeded = false))
            }
        }

        private fun validateStep(step: GuideTourPublishStep): Boolean {
            draftState.value.findValidationError(step)?.let { error ->
                return showValidationError(error.messageResId, step)
            }
            draftState.update {
                it.copy(
                    submission =
                        it.submission.copy(
                            validationErrorStep = null,
                            validationErrorResId = null,
                        ),
                )
            }
            return true
        }

        private fun showValidationError(
            @StringRes messageResId: Int,
            step: GuideTourPublishStep,
        ): Boolean {
            draftState.update {
                it.copy(
                    submission =
                        it.submission.copy(
                            validationErrorStep = step,
                            validationErrorResId = messageResId,
                        ),
                )
            }
            return false
        }

        private fun finishWithError(message: String) {
            draftState.update {
                it.copy(
                    submission =
                        it.submission.copy(isPublishing = false, errorMessage = message),
                )
            }
        }

        private fun finishWithError(error: AppError) {
            error.toTourPublishValidationError()?.let { validationError ->
                draftState.update {
                    it.copy(
                        submission =
                            it.submission.copy(
                                isPublishing = false,
                                errorMessage = null,
                                validationErrorStep = validationError.step,
                                validationErrorResId = validationError.messageResId,
                            ),
                    )
                }
                return
            }
            finishWithError(error.toMessage(resourceProvider))
        }

        private fun updateDraft(transform: GuideTourPublishUiState.() -> GuideTourPublishUiState) {
            draftState.update { state ->
                state.transform().copy(
                    submission =
                        state.submission.copy(
                            validationErrorStep = null,
                            validationErrorResId = null,
                            errorMessage = null,
                        ),
                )
            }
        }
    }

private data class TourPublishValidationError(
    val step: GuideTourPublishStep,
    @param:StringRes val messageResId: Int,
)

private fun GuideTourPublishUiState.firstValidationError(): TourPublishValidationError? =
    PUBLISH_INPUT_STEPS.firstNotNullOfOrNull(::findValidationError)

private fun GuideTourPublishUiState.findValidationError(
    step: GuideTourPublishStep,
): TourPublishValidationError? =
    when (step) {
        GuideTourPublishStep.LOCATION_AND_TIME ->
            if (
                countryCode.isBlank() ||
                    country.isBlank() ||
                    cityPlaceId.isBlank() ||
                    city.isBlank() ||
                    timeZoneId.isBlank() ||
                    toStartInstant()?.isAfter(Instant.now()) != true ||
                    durationMinutes?.let { it > 0 } != true
            ) {
                TourPublishValidationError(step, R.string.error_tour_step1_invalid)
            } else {
                null
            }
        GuideTourPublishStep.TECHNICAL_DETAILS ->
            when {
                spokenLanguages.size > MAX_TOUR_LANGUAGE_COUNT ->
                    TourPublishValidationError(step, R.string.error_tour_languages_too_many)
                category == null ||
                    spokenLanguages.isEmpty() ||
                    price.toCurrencyMinorUnitsOrNull()?.let { it > 0 } != true ||
                    capacity.toIntOrNull()?.let { it > 0 } != true ->
                    TourPublishValidationError(step, R.string.error_tour_step2_invalid)
                else -> null
            }
        GuideTourPublishStep.CONTENT_AND_MEDIA -> contentValidationError()
        GuideTourPublishStep.PREVIEW -> firstValidationError()
    }

private fun GuideTourPublishUiState.contentValidationError(): TourPublishValidationError? {
    val step = GuideTourPublishStep.CONTENT_AND_MEDIA
    val trimmedTitle = tourName.trim()
    val trimmedDescription = tourDescription.trim()
    val trimmedMeetingPoint = meetingPoint.trim()
    return when {
        trimmedTitle.isEmpty() ->
            TourPublishValidationError(step, R.string.error_tour_title_required)
        trimmedTitle.length !in TOUR_TITLE_MIN_LENGTH..TOUR_TITLE_MAX_LENGTH ->
            TourPublishValidationError(step, R.string.error_tour_title_length)
        selectedCoverImageUri == null ->
            TourPublishValidationError(step, R.string.error_tour_cover_required)
        trimmedDescription.isEmpty() ->
            TourPublishValidationError(step, R.string.error_tour_description_required)
        trimmedDescription.length !in TOUR_DESCRIPTION_MIN_LENGTH..TOUR_DESCRIPTION_MAX_LENGTH ->
            TourPublishValidationError(step, R.string.error_tour_description_length)
        trimmedMeetingPoint.isEmpty() ->
            TourPublishValidationError(step, R.string.error_tour_meeting_point_required)
        trimmedMeetingPoint.length > TOUR_MEETING_POINT_MAX_LENGTH ->
            TourPublishValidationError(step, R.string.error_tour_meeting_point_length)
        else -> null
    }
}

private fun AppError.toTourPublishValidationError(): TourPublishValidationError? {
    val backendError = this as? AppError.Backend ?: return null
    return backendError.fieldErrors.firstNotNullOfOrNull { fieldError ->
        when (fieldError.field.substringAfterLast('.')) {
            "title" ->
                TourPublishValidationError(
                    GuideTourPublishStep.CONTENT_AND_MEDIA,
                    if (fieldError.code == FIELD_REQUIRED_CODE) {
                        R.string.error_tour_title_required
                    } else {
                        R.string.error_tour_title_length
                    },
                )
            "description" ->
                TourPublishValidationError(
                    GuideTourPublishStep.CONTENT_AND_MEDIA,
                    if (fieldError.code == FIELD_REQUIRED_CODE) {
                        R.string.error_tour_description_required
                    } else {
                        R.string.error_tour_description_length
                    },
                )
            "meetingPoint" ->
                TourPublishValidationError(
                    GuideTourPublishStep.CONTENT_AND_MEDIA,
                    if (fieldError.code == FIELD_REQUIRED_CODE) {
                        R.string.error_tour_meeting_point_required
                    } else {
                        R.string.error_tour_meeting_point_length
                    },
                )
            "languageCodes" ->
                TourPublishValidationError(
                    GuideTourPublishStep.TECHNICAL_DETAILS,
                    if (fieldError.code == INVALID_SIZE_CODE) {
                        R.string.error_tour_languages_too_many
                    } else {
                        R.string.error_tour_step2_invalid
                    },
                )
            "countryCode", "cityPlaceId", "cityName", "timeZoneId", "startsAt", "durationMinutes" ->
                TourPublishValidationError(
                    GuideTourPublishStep.LOCATION_AND_TIME,
                    R.string.error_tour_step1_invalid,
                )
            "categoryCode", "priceMinor", "capacity" ->
                TourPublishValidationError(
                    GuideTourPublishStep.TECHNICAL_DETAILS,
                    R.string.error_tour_step2_invalid,
                )
            "coverMediaId" ->
                TourPublishValidationError(
                    GuideTourPublishStep.CONTENT_AND_MEDIA,
                    R.string.error_tour_cover_required,
                )
            else -> null
        }
    }
}

private fun GuideTourPublishUiState.toCreateInputOrNull(coverMediaId: String?): CreateGuideTourInput? {
    if (firstValidationError() != null) return null
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

private const val TOUR_TITLE_MIN_LENGTH = 3
private const val TOUR_TITLE_MAX_LENGTH = 120
private const val TOUR_DESCRIPTION_MIN_LENGTH = 20
private const val TOUR_DESCRIPTION_MAX_LENGTH = 3_000
private const val TOUR_MEETING_POINT_MAX_LENGTH = 500
private const val MAX_TOUR_LANGUAGE_COUNT = 20
private const val FIELD_REQUIRED_CODE = "FIELD_REQUIRED"
private const val INVALID_SIZE_CODE = "INVALID_SIZE"

private val PUBLISH_INPUT_STEPS =
    listOf(
        GuideTourPublishStep.LOCATION_AND_TIME,
        GuideTourPublishStep.TECHNICAL_DETAILS,
        GuideTourPublishStep.CONTENT_AND_MEDIA,
    )
