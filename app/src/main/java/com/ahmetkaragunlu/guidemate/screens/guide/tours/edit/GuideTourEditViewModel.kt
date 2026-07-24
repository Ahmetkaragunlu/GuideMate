package com.ahmetkaragunlu.guidemate.screens.guide.tours.edit

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.navigation.guide.GUIDE_TOUR_SESSION_ID_ARGUMENT
import com.ahmetkaragunlu.guidemate.screens.common.selection.model.LanguageOption
import com.ahmetkaragunlu.guidemate.screens.common.tours.category.TourCategory
import com.ahmetkaragunlu.guidemate.screens.guide.tours.edit.model.GuideTourEditUiState
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.GuideTourTab
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.TourApprovalStatus
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.TourEditRequest
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.toTourLanguage
import com.ahmetkaragunlu.guidemate.screens.guide.tours.shared.GuideTourStore
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class GuideTourEditViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val tourStore: GuideTourStore,
    ) : ViewModel() {
        private val sessionId: String =
            checkNotNull(savedStateHandle[GUIDE_TOUR_SESSION_ID_ARGUMENT])

        private val originalApprovalStatus =
            tourStore.state.value.findBySessionId(sessionId)?.tour?.approvalStatus
        private val originalState =
            createInitialState().copy(
                requiresReviewConfirmation =
                    originalApprovalStatus == TourApprovalStatus.REJECTED,
            )
        private val _uiState = MutableStateFlow(originalState)
        val uiState = _uiState.asStateFlow()

        fun onTitleChange(value: String) = updateForm { copy(title = value) }

        fun onDescriptionChange(value: String) = updateForm { copy(description = value) }

        fun onCategorySelected(category: TourCategory) = updateForm { copy(category = category) }

        fun onTourDateSelected(date: LocalDate) {
            updateForm {
                val today = LocalDate.now()
                val currentTime = LocalTime.now()
                copy(
                    tourDate = date,
                    startTime = startTime?.takeIf { date != today || it.isAfter(currentTime) },
                )
            }
        }

        fun onStartTimeSelected(time: LocalTime) = updateForm { copy(startTime = time) }

        fun onMeetingPointChange(value: String) = updateForm { copy(meetingPoint = value) }

        fun onDurationChange(value: String) {
            if (value.all(Char::isDigit)) updateForm { copy(durationMinutes = value) }
        }

        fun onPriceChange(value: String) {
            if (value.all(Char::isDigit)) updateForm { copy(price = value) }
        }

        fun onCapacityChange(value: String) {
            if (value.all(Char::isDigit)) updateForm { copy(capacity = value) }
        }

        fun removeLanguage(code: String) {
            if (_uiState.value.languages.size > 1) {
                updateForm { copy(languages = languages.filterNot { it.code == code }) }
            }
        }

        fun onLanguagesSelected(languages: List<LanguageOption>) {
            updateForm {
                copy(
                    languages =
                        languages.map(LanguageOption::toTourLanguage),
                )
            }
        }

        fun onCoverImageSelected(uri: String) = updateForm { copy(selectedCoverImageUri = uri) }

        fun onCoverImageSelectionError(@StringRes errorResId: Int) {
            _uiState.update { it.copy(errorResId = errorResId) }
        }

        fun saveChanges(): GuideTourTab? {
            val form = _uiState.value
            val category = form.category ?: return showError()
            val tourDate = form.tourDate ?: return showError()
            val startTime = form.startTime ?: return showError()
            val startsAt =
                LocalDateTime.of(tourDate, startTime)
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
            val result =
                tourStore.updateTour(
                    tourId = form.tourId,
                    sessionId = form.sessionId,
                    request =
                        TourEditRequest(
                            title = form.title,
                            description = form.description,
                            country = form.country,
                            city = form.location,
                            category = category,
                            languages = form.languages,
                            selectedCoverImageUri = form.selectedCoverImageUri,
                            meetingPoint = form.meetingPoint,
                            startsAt = startsAt,
                            durationMinutes = form.durationMinutes.toIntOrNull() ?: return showError(),
                            price = form.price.toDoubleOrNull() ?: return showError(),
                            capacity = form.capacity.toIntOrNull() ?: return showError(),
                        ),
                )
            if (!result) return showError()

            _uiState.update { it.copy(hasUnsavedChanges = false, errorResId = null) }
            val approvalStatus =
                tourStore.state.value.findBySessionId(sessionId)?.tour?.approvalStatus
            return if (approvalStatus == TourApprovalStatus.APPROVED) {
                GuideTourTab.ACTIVE
            } else {
                GuideTourTab.REVIEW
            }
        }

        private fun createInitialState(): GuideTourEditUiState {
            val current = tourStore.state.value.findBySessionId(sessionId) ?: return GuideTourEditUiState()
            return GuideTourEditUiState(
                tourId = current.tour.id,
                sessionId = current.session.id,
                title = current.tour.title,
                description = current.tour.description,
                country = current.tour.country,
                location = current.tour.city,
                category = current.tour.category,
                meetingPoint = current.session.meetingPoint,
                tourDate = current.session.startsAt.atZone(ZoneId.systemDefault()).toLocalDate(),
                startTime = current.session.startsAt.atZone(ZoneId.systemDefault()).toLocalTime(),
                durationMinutes = current.session.durationMinutes.toString(),
                price = current.session.price.toInt().toString(),
                capacity = current.session.capacity.toString(),
                languages = current.tour.languages,
                coverImageResId = current.tour.coverImageResId,
                coverImageUrl = current.tour.coverImageUrl,
                hasBookings = current.session.bookedCount > 0,
                isTourIdentityLocked = current.tour.publishedAt != null,
                approvalStatus = current.tour.approvalStatus,
            )
        }

        private fun showError(): GuideTourTab? {
            _uiState.update { it.copy(errorResId = R.string.error_tour_edit_invalid) }
            return null
        }

        private fun updateForm(transform: GuideTourEditUiState.() -> GuideTourEditUiState) {
            _uiState.update { state ->
                val updated = state.transform()
                updated.copy(
                    hasUnsavedChanges = updated.hasChangesFrom(originalState),
                    requiresReviewConfirmation = updated.requiresReviewConfirmation(),
                    errorResId = null,
                )
            }
        }

        private fun GuideTourEditUiState.requiresReviewConfirmation(): Boolean {
            val hasCriticalChange =
                title != originalState.title ||
                    description != originalState.description ||
                    category != originalState.category ||
                    languages != originalState.languages ||
                    selectedCoverImageUri != originalState.selectedCoverImageUri
            return (originalApprovalStatus == TourApprovalStatus.APPROVED && hasCriticalChange) ||
                originalApprovalStatus == TourApprovalStatus.REJECTED
        }

        private fun GuideTourEditUiState.hasChangesFrom(original: GuideTourEditUiState): Boolean =
            title != original.title ||
                description != original.description ||
                country != original.country ||
                location != original.location ||
                category != original.category ||
                meetingPoint != original.meetingPoint ||
                tourDate != original.tourDate ||
                startTime != original.startTime ||
                durationMinutes != original.durationMinutes ||
                price != original.price ||
                capacity != original.capacity ||
                languages != original.languages ||
                selectedCoverImageUri != original.selectedCoverImageUri
    }
