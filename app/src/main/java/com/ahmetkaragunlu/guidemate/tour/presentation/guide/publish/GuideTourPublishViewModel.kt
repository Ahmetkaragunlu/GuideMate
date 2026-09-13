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
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider
import com.ahmetkaragunlu.guidemate.profile.domain.repository.GuideProfileRepository
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import com.ahmetkaragunlu.guidemate.tour.domain.usecase.PublishGuideTourUseCase
import com.ahmetkaragunlu.guidemate.tour.presentation.mapper.toTourLanguage
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.model.GuideTourPublishStep
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.model.GuideTourPublishUiState
import dagger.hilt.android.lifecycle.HiltViewModel
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
        private val publishGuideTour: PublishGuideTourUseCase,
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
                when (val result = publishGuideTour(imageUri, inputWithoutCover)) {
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
                    is DataResult.Error -> finishWithError(result.error)
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
