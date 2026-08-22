package com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.edit

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.location.model.LanguageOption
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.error.toMessage
import com.ahmetkaragunlu.guidemate.common.ui.formatting.isValidCurrencyInput
import com.ahmetkaragunlu.guidemate.common.ui.formatting.toCurrencyInput
import com.ahmetkaragunlu.guidemate.common.ui.formatting.toCurrencyMinorUnitsOrNull
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaPurpose
import com.ahmetkaragunlu.guidemate.media.domain.repository.MediaRepository
import com.ahmetkaragunlu.guidemate.navigation.guide.tours.GuideTourDestination
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourApprovalStatus
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import com.ahmetkaragunlu.guidemate.tour.domain.model.guide.GuideTourDetails
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.SubmitTourChangeInput
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.TourContentInput
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.TourSessionInput
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.UpdateTourSessionInput
import com.ahmetkaragunlu.guidemate.tour.domain.repository.GuideTourRepository
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.edit.model.GuideTourEditUiState
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.model.GuideTourTab
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.model.toTourLanguage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class GuideTourEditViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val repository: GuideTourRepository,
        private val mediaRepository: MediaRepository,
        private val resourceProvider: ResourceProvider,
    ) : ViewModel() {
        private val route = savedStateHandle.toRoute<GuideTourDestination.Edit>()
        private val _uiState = MutableStateFlow(GuideTourEditUiState())
        val uiState = _uiState.asStateFlow()

        private var originalState: GuideTourEditUiState? = null
        private var originalApprovalStatus: TourApprovalStatus? = null

        init {
            refresh()
        }

        fun refresh() {
            viewModelScope.launch {
                _uiState.update { it.copy(loadState = ContentLoadState.LOADING) }
                when (val result = repository.getTour(route.tourId)) {
                    is DataResult.Success -> setInitialState(result.data)
                    is DataResult.Error -> {
                        _uiState.update {
                            it.copy(
                                loadState = ContentLoadState.ERROR,
                                userMessage = result.error.toMessage(resourceProvider),
                            )
                        }
                    }
                }
            }
        }

        fun onTitleChange(value: String) = updateForm { copy(title = value) }

        fun onDescriptionChange(value: String) = updateForm { copy(description = value) }

        fun onCategorySelected(category: TourCategory) = updateForm { copy(category = category) }

        fun onTourDateSelected(date: LocalDate) {
            updateForm {
                val zone = timeZoneId.toZoneId()
                val today = LocalDate.now(zone)
                val currentTime = LocalTime.now(zone)
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
            if (value.isValidCurrencyInput()) updateForm { copy(price = value) }
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
            updateForm { copy(languages = languages.map(LanguageOption::toTourLanguage)) }
        }

        fun onCoverImageSelected(uri: String) = updateForm { copy(selectedCoverImageUri = uri) }

        fun onCoverImageSelectionError(@StringRes errorResId: Int) {
            _uiState.update { it.copy(errorResId = errorResId) }
        }

        fun saveChanges() {
            val state = _uiState.value
            if (state.isSaving) return
            val contentChanged = state.hasContentChangesFrom(originalState)
            val sessionChanged = state.hasSessionChangesFrom(originalState)
            val mustResubmit = originalApprovalStatus == TourApprovalStatus.REJECTED
            if (!contentChanged && !sessionChanged && !mustResubmit) {
                showError(R.string.error_tour_edit_no_changes)
                return
            }

            val sessionInput = state.toSessionInputOrNull() ?: return showError()
            _uiState.update { it.copy(isSaving = true, errorResId = null) }
            viewModelScope.launch {
                var uploadedMediaId: String? = null
                var current = _uiState.value
                val shouldSubmitContent =
                    !current.contentReviewSubmitted && (contentChanged || mustResubmit)

                if (shouldSubmitContent) {
                    current.selectedCoverImageUri?.let { uri ->
                        when (val upload = mediaRepository.uploadImage(uri, MediaPurpose.TOUR_COVER)) {
                            is DataResult.Success -> uploadedMediaId = upload.data.mediaAssetId
                            is DataResult.Error -> {
                                finishWithError(upload.error.toMessage(resourceProvider))
                                return@launch
                            }
                        }
                    }
                    val coverMediaId = uploadedMediaId ?: current.coverMediaId
                    val content = current.toContentInputOrNull(coverMediaId)
                    if (content == null) {
                        uploadedMediaId?.let { mediaRepository.deleteUnreferenced(it) }
                        showError()
                        _uiState.update { it.copy(isSaving = false) }
                        return@launch
                    }
                    when (
                        val change =
                            repository.submitChange(
                                tourId = route.tourId,
                                input =
                                    SubmitTourChangeInput(
                                        baseVersion = current.tourVersion,
                                        content = content,
                                    ),
                            )
                    ) {
                        is DataResult.Error -> {
                            uploadedMediaId?.let { mediaRepository.deleteUnreferenced(it) }
                            finishWithError(change.error.toMessage(resourceProvider))
                            return@launch
                        }
                        is DataResult.Success -> {
                            current =
                                current.copy(
                                    tourVersion = change.data.details.tour.version,
                                    coverMediaId = coverMediaId,
                                    coverImageUrl = change.data.details.tour.coverImageUrl,
                                    selectedCoverImageUri = null,
                                    contentReviewSubmitted = true,
                                    requiresReviewConfirmation = false,
                                )
                            originalState = originalState?.withContentFrom(current)
                            _uiState.value = current.copy(isSaving = true)
                        }
                    }
                }

                val shouldUpdateSession = current.hasSessionChangesFrom(originalState)
                if (shouldUpdateSession) {
                    when (
                        val sessionResult =
                            repository.updateSession(
                                sessionId = route.sessionId,
                                input =
                                    UpdateTourSessionInput(
                                        version = current.sessionVersion,
                                        session = sessionInput,
                                    ),
                            )
                    ) {
                        is DataResult.Error -> {
                            val message = sessionResult.error.toMessage(resourceProvider)
                            finishWithError(
                                if (current.contentReviewSubmitted) {
                                    resourceProvider.getString(
                                        R.string.tour_edit_partial_success,
                                        message,
                                    )
                                } else {
                                    message
                                },
                            )
                            return@launch
                        }
                        is DataResult.Success -> {
                            originalState = originalState?.withSessionFrom(current)
                            current = current.copy(sessionVersion = sessionResult.data.version)
                        }
                    }
                }

                _uiState.value =
                    current.copy(
                        hasUnsavedChanges = false,
                        isSaving = false,
                        savedTargetTab =
                            if (current.contentReviewSubmitted) {
                                GuideTourTab.REVIEW
                            } else {
                                GuideTourTab.ACTIVE
                            },
                    )
            }
        }

        fun onSavedHandled() {
            _uiState.update { it.copy(savedTargetTab = null) }
        }

        fun onUserMessageShown() {
            _uiState.update { it.copy(userMessage = null) }
        }

        private fun setInitialState(details: GuideTourDetails) {
            val session = details.session(route.sessionId)
            if (session == null) {
                _uiState.update {
                    it.copy(
                        loadState = ContentLoadState.ERROR,
                        userMessage =
                            resourceProvider.getString(R.string.tour_operation_session_not_found),
                    )
                }
                return
            }
            val zone = details.tour.timeZoneId.toZoneId()
            val state =
                GuideTourEditUiState(
                    tourId = details.tour.id,
                    sessionId = session.id,
                    tourVersion = details.tour.version,
                    sessionVersion = session.version,
                    title = details.tour.title,
                    description = details.tour.description,
                    country = details.tour.country,
                    countryCode = details.tour.countryCode,
                    location = details.tour.city,
                    cityPlaceId = details.tour.cityPlaceId,
                    timeZoneId = details.tour.timeZoneId,
                    category = details.tour.category,
                    meetingPoint = session.meetingPoint,
                    tourDate = session.startsAt.atZone(zone).toLocalDate(),
                    startTime = session.startsAt.atZone(zone).toLocalTime(),
                    durationMinutes = session.durationMinutes.toString(),
                    price = session.priceMinor.toCurrencyInput(),
                    capacity = session.capacity.toString(),
                    languages = details.tour.languages,
                    coverImageResId = details.tour.coverImageResId,
                    coverImageUrl = details.tour.coverImageUrl,
                    coverMediaId = details.tour.coverMediaId,
                    hasBookings = session.bookedCount > 0,
                    isTourIdentityLocked = true,
                    approvalStatus = details.tour.approvalStatus,
                    requiresReviewConfirmation =
                        details.tour.approvalStatus == TourApprovalStatus.REJECTED,
                    loadState = ContentLoadState.CONTENT,
                )
            originalApprovalStatus = details.tour.approvalStatus
            originalState = state
            _uiState.value = state
        }

        private fun updateForm(transform: GuideTourEditUiState.() -> GuideTourEditUiState) {
            _uiState.update { state ->
                val updated = state.transform()
                updated.copy(
                    hasUnsavedChanges = updated.hasChangesFrom(originalState),
                    requiresReviewConfirmation =
                        updated.hasContentChangesFrom(originalState) ||
                            originalApprovalStatus == TourApprovalStatus.REJECTED,
                    errorResId = null,
                )
            }
        }

        private fun finishWithError(message: String) {
            _uiState.update { state ->
                state.copy(
                    isSaving = false,
                    hasUnsavedChanges = state.hasChangesFrom(originalState),
                    userMessage = message,
                )
            }
        }

        private fun showError(@StringRes errorResId: Int = R.string.error_tour_edit_invalid) {
            _uiState.update { it.copy(errorResId = errorResId) }
        }
    }

private fun GuideTourEditUiState.toContentInputOrNull(coverMediaId: String?): TourContentInput? {
    val selectedCategory = category ?: return null
    if (
        title.isBlank() ||
        description.isBlank() ||
        countryCode.isBlank() ||
        cityPlaceId.isBlank() ||
        location.isBlank() ||
        timeZoneId.isBlank() ||
        languages.isEmpty() ||
        coverMediaId.isNullOrBlank()
    ) {
        return null
    }
    return TourContentInput(
        title = title.trim(),
        description = description.trim(),
        countryCode = countryCode,
        cityPlaceId = cityPlaceId,
        cityName = location,
        timeZoneId = timeZoneId,
        category = selectedCategory,
        languageCodes = languages.map { it.code },
        coverMediaId = coverMediaId,
    )
}

private fun GuideTourEditUiState.toSessionInputOrNull(): TourSessionInput? {
    val date = tourDate ?: return null
    val time = startTime ?: return null
    val duration = durationMinutes.toIntOrNull()?.takeIf { it > 0 } ?: return null
    val amount = price.toCurrencyMinorUnitsOrNull()?.takeIf { it > 0 } ?: return null
    val participantCapacity = capacity.toIntOrNull()?.takeIf { it > 0 } ?: return null
    if (meetingPoint.isBlank() || participantCapacity < 1) return null
    val startsAt =
        runCatching {
            LocalDateTime.of(date, time).atZone(timeZoneId.toZoneId()).toInstant()
        }.getOrNull() ?: return null
    return TourSessionInput(
        meetingPoint = meetingPoint.trim(),
        startsAt = startsAt,
        durationMinutes = duration,
        priceMinor = amount,
        capacity = participantCapacity,
    )
}

private fun GuideTourEditUiState.hasChangesFrom(original: GuideTourEditUiState?): Boolean =
    hasContentChangesFrom(original) || hasSessionChangesFrom(original)

private fun GuideTourEditUiState.hasContentChangesFrom(original: GuideTourEditUiState?): Boolean =
    original != null &&
        (
            title != original.title ||
                description != original.description ||
                category != original.category ||
                languages != original.languages ||
                selectedCoverImageUri != null
        )

private fun GuideTourEditUiState.hasSessionChangesFrom(original: GuideTourEditUiState?): Boolean =
    original != null &&
        (
            meetingPoint != original.meetingPoint ||
                tourDate != original.tourDate ||
                startTime != original.startTime ||
                durationMinutes != original.durationMinutes ||
                price != original.price ||
                capacity != original.capacity
        )

private fun GuideTourEditUiState.withContentFrom(current: GuideTourEditUiState): GuideTourEditUiState =
    copy(
        title = current.title,
        description = current.description,
        category = current.category,
        languages = current.languages,
        coverMediaId = current.coverMediaId,
        coverImageUrl = current.coverImageUrl,
        selectedCoverImageUri = null,
    )

private fun GuideTourEditUiState.withSessionFrom(current: GuideTourEditUiState): GuideTourEditUiState =
    copy(
        meetingPoint = current.meetingPoint,
        tourDate = current.tourDate,
        startTime = current.startTime,
        durationMinutes = current.durationMinutes,
        price = current.price,
        capacity = current.capacity,
    )

private fun String.toZoneId(): ZoneId =
    runCatching { ZoneId.of(this) }.getOrDefault(ZoneId.systemDefault())
