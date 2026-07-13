package com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.guide.profile.shared.GuideProfileStateProvider
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.model.GuideTourPublishStep
import com.ahmetkaragunlu.guidemate.screens.guide.tourpublish.model.GuideTourPublishUiState
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.Tour
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.TourApprovalStatus
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.TourLanguage
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.TourSession
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.TourSessionStatus
import com.ahmetkaragunlu.guidemate.screens.guide.tours.shared.GuideTourStore
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel
class GuideTourPublishViewModel
    @Inject
    constructor(
        private val tourStore: GuideTourStore,
        guideProfileStateProvider: GuideProfileStateProvider,
    ) : ViewModel() {
        private val draftState = MutableStateFlow(initialUiState())
        private var hasSubmittedCurrentDraft = false
        val uiState: StateFlow<GuideTourPublishUiState> =
            combine(draftState, guideProfileStateProvider.profileState()) { draft, profile ->
                draft.copy(
                    guideName = profile.displayName,
                    guideImageResId = profile.profileImageResId ?: R.drawable.unnamed,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = initialUiState(),
            )

        fun resetDraft() {
            draftState.value = initialUiState()
            hasSubmittedCurrentDraft = false
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

        fun onStartTimeSelected(time: LocalTime) {
            updateDraft { copy(startTime = time) }
        }

        fun onDurationSelected(durationMinutes: Int) {
            updateDraft { copy(durationMinutes = durationMinutes) }
        }

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
            if (input.all(Char::isDigit)) {
                updateDraft { copy(price = input) }
            }
        }

        fun onCapacityChange(input: String) {
            if (input.all(Char::isDigit)) {
                updateDraft { copy(capacity = input) }
            }
        }

        fun onTourNameChange(value: String) {
            updateDraft { copy(tourName = value) }
        }

        fun onTourDescriptionChange(value: String) {
            updateDraft { copy(tourDescription = value) }
        }

        fun onCoverImageSelected(uri: String) {
            updateDraft { copy(selectedCoverImageUri = uri) }
        }

        fun onMeetingPointChange(value: String) {
            updateDraft { copy(meetingPoint = value) }
        }

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

        fun onPublishClick(): Boolean {
            if (hasSubmittedCurrentDraft) return false
            val form = draftState.value
            if (!form.isStep1Valid()) {
                return showValidationError(
                    R.string.error_tour_step1_invalid,
                    GuideTourPublishStep.PREVIEW,
                )
            }
            if (!form.isStep2Valid()) {
                return showValidationError(
                    R.string.error_tour_step2_invalid,
                    GuideTourPublishStep.PREVIEW,
                )
            }
            if (!form.isStep3Valid()) {
                return showValidationError(
                    R.string.error_tour_step3_invalid,
                    GuideTourPublishStep.PREVIEW,
                )
            }

            val startsAt = form.toStartInstant()
            val duration = form.durationMinutes
            val price = form.price.toDoubleOrNull()
            val capacity = form.capacity.toIntOrNull()
            if (
                startsAt == null ||
                    !startsAt.isAfter(Instant.now()) ||
                    duration == null ||
                    duration <= 0 ||
                    price == null ||
                    price <= 0 ||
                    capacity == null ||
                    capacity <= 0
            ) {
                return showValidationError(
                    R.string.error_tour_schedule_invalid,
                    GuideTourPublishStep.PREVIEW,
                )
            }
            val tourId = UUID.randomUUID().toString()
            val tour =
                Tour(
                    id = tourId,
                    guideId = "guide-current",
                    title = form.tourName.trim(),
                    description = form.tourDescription.trim(),
                    country = form.country.trim(),
                    city = form.city.trim(),
                    timeZoneId = form.timeZoneId,
                    category = form.category.trim(),
                    languages = form.spokenLanguages,
                    coverImageResId = form.previewImageResId,
                    coverImageUrl = form.selectedCoverImageUri,
                    approvalStatus = TourApprovalStatus.PENDING_REVIEW,
                )
            val session =
                TourSession(
                    id = UUID.randomUUID().toString(),
                    tourId = tourId,
                    meetingPoint = form.meetingPoint.trim(),
                    startsAt = startsAt,
                    durationMinutes = duration,
                    price = price,
                    capacity = capacity,
                    bookedCount = 0,
                    status = TourSessionStatus.CLOSED,
                )
            return if (tourStore.submitForReview(tour = tour, session = session)) {
                hasSubmittedCurrentDraft = true
                true
            } else {
                showValidationError(
                    R.string.error_session_creation,
                    GuideTourPublishStep.PREVIEW,
                )
            }
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
                it.copy(
                    validationErrorStep = step,
                    validationErrorResId = messageResId,
                )
            }
            return false
        }

        private fun updateDraft(transform: GuideTourPublishUiState.() -> GuideTourPublishUiState) {
            draftState.update { state ->
                state.transform().copy(
                    validationErrorStep = null,
                    validationErrorResId = null,
                )
            }
        }

        private fun GuideTourPublishUiState.isStep1Valid(): Boolean =
            country.isNotBlank() &&
                city.isNotBlank() &&
                toStartInstant()?.isAfter(Instant.now()) == true &&
                durationMinutes?.let { it > 0 } == true

        private fun GuideTourPublishUiState.isStep2Valid(): Boolean =
            spokenLanguages.isNotEmpty() &&
                price.toDoubleOrNull()?.let { it > 0 } == true &&
                capacity.toIntOrNull()?.let { it > 0 } == true

        private fun GuideTourPublishUiState.isStep3Valid(): Boolean =
            tourName.isNotBlank() &&
                selectedCoverImageUri != null &&
                tourDescription.isNotBlank() &&
                meetingPoint.isNotBlank()

        private fun GuideTourPublishUiState.toStartInstant(): Instant? {
            val date = tourDate ?: return null
            val time = startTime ?: return null
            return runCatching { date.atTime(time).atZone(timeZoneId.toZoneId()).toInstant() }.getOrNull()
        }

        private fun String.toZoneId(): ZoneId =
            runCatching { ZoneId.of(this) }.getOrDefault(ZoneId.systemDefault())

        private companion object {
            // Mock data (MVP)
            fun initialUiState(): GuideTourPublishUiState =
                GuideTourPublishUiState(
                    country = "Türkiye",
                    city = "İstanbul",
                    timeZoneId = "Europe/Istanbul",
                    tourDate = LocalDate.of(2027, 5, 24),
                    startTime = LocalTime.of(9, 0),
                    durationMinutes = 180,
                    category = "Tarih ve Kültür",
                    spokenLanguages =
                        listOf(
                            TourLanguage(
                                code = "tr",
                                flagEmoji = "🇹🇷",
                                displayName = "Türkçe",
                                shortCode = "TR",
                            ),
                            TourLanguage(
                                code = "en",
                                flagEmoji = "🇬🇧",
                                displayName = "İngilizce",
                                shortCode = "ENG",
                            ),
                        ),
                    capacity = "12",
                    previewImageResId = R.drawable.example,
                )
        }
    }
