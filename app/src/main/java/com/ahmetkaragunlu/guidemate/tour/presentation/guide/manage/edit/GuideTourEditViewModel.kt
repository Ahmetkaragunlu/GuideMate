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
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaPurpose
import com.ahmetkaragunlu.guidemate.media.domain.repository.MediaRepository
import com.ahmetkaragunlu.guidemate.navigation.guide.tours.GuideTourDestination
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourApprovalStatus
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourDetails
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.SubmitTourChangeInput
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.TourSessionInput
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.UpdateTourSessionInput
import com.ahmetkaragunlu.guidemate.tour.domain.repository.GuideTourRepository
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.edit.model.GuideTourEditUiState
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.edit.model.GuideTourEditContentFormState
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.edit.model.GuideTourEditIdentityState
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.edit.model.GuideTourEditOperationState
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.edit.model.GuideTourEditSessionFormState
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.model.GuideTourTab
import com.ahmetkaragunlu.guidemate.tour.presentation.mapper.toTourLanguage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.LocalTime
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
                _uiState.update {
                    it.copy(operation = it.operation.copy(loadState = ContentLoadState.LOADING))
                }
                when (val result = repository.getTour(route.tourId)) {
                    is DataResult.Success -> setInitialState(result.data)
                    is DataResult.Error -> {
                        _uiState.update {
                            it.copy(
                                operation =
                                    it.operation.copy(
                                        loadState = ContentLoadState.ERROR,
                                        userMessage = result.error.toMessage(resourceProvider),
                                    ),
                            )
                        }
                    }
                }
            }
        }

        fun onTitleChange(value: String) =
            updateForm { copy(content = content.copy(title = value)) }

        fun onDescriptionChange(value: String) =
            updateForm { copy(content = content.copy(description = value)) }

        fun onCategorySelected(category: TourCategory) =
            updateForm { copy(content = content.copy(category = category)) }

        fun onTourDateSelected(date: LocalDate) {
            updateForm {
                val zone = timeZoneId.toZoneId()
                val today = LocalDate.now(zone)
                val currentTime = LocalTime.now(zone)
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
            updateForm { copy(session = session.copy(startTime = time)) }

        fun onMeetingPointChange(value: String) =
            updateForm { copy(session = session.copy(meetingPoint = value)) }

        fun onDurationChange(value: String) {
            if (value.all(Char::isDigit)) {
                updateForm { copy(session = session.copy(durationMinutes = value)) }
            }
        }

        fun onPriceChange(value: String) {
            if (value.isValidCurrencyInput()) {
                updateForm { copy(session = session.copy(price = value)) }
            }
        }

        fun onCapacityChange(value: String) {
            if (value.all(Char::isDigit)) {
                updateForm { copy(session = session.copy(capacity = value)) }
            }
        }

        fun removeLanguage(code: String) {
            if (_uiState.value.languages.size > 1) {
                updateForm {
                    copy(content = content.copy(languages = languages.filterNot { it.code == code }))
                }
            }
        }

        fun onLanguagesSelected(languages: List<LanguageOption>) {
            updateForm {
                copy(
                    content = content.copy(languages = languages.map(LanguageOption::toTourLanguage)),
                )
            }
        }

        fun onCoverImageSelected(uri: String) =
            updateForm { copy(content = content.copy(selectedCoverImageUri = uri)) }

        fun onCoverImageSelectionError(@StringRes errorResId: Int) {
            _uiState.update {
                it.copy(operation = it.operation.copy(errorResId = errorResId))
            }
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
            _uiState.update {
                it.copy(operation = it.operation.copy(isSaving = true, errorResId = null))
            }
            viewModelScope.launch {
                val current = _uiState.value
                val shouldSubmitContent =
                    !current.contentReviewSubmitted && (contentChanged || mustResubmit)
                val contentUpdatedState =
                    if (shouldSubmitContent) {
                        submitContentChanges(current) ?: return@launch
                    } else {
                        current
                    }
                val savedState =
                    updateSessionIfNeeded(contentUpdatedState, sessionInput) ?: return@launch

                _uiState.value =
                    savedState.copy(
                        operation =
                            savedState.operation.copy(
                                hasUnsavedChanges = false,
                                isSaving = false,
                                savedTargetTab =
                                    if (savedState.contentReviewSubmitted) {
                                        GuideTourTab.REVIEW
                                    } else {
                                        GuideTourTab.ACTIVE
                                    },
                            ),
                    )
            }
        }

        private suspend fun submitContentChanges(
            state: GuideTourEditUiState,
        ): GuideTourEditUiState? {
            var uploadedMediaId: String? = null
            state.selectedCoverImageUri?.let { uri ->
                when (val upload = mediaRepository.uploadImage(uri, MediaPurpose.TOUR_COVER)) {
                    is DataResult.Success -> uploadedMediaId = upload.data.mediaAssetId
                    is DataResult.Error -> {
                        finishWithError(upload.error.toMessage(resourceProvider))
                        return null
                    }
                }
            }
            val coverMediaId = uploadedMediaId ?: state.coverMediaId
            val content = state.toContentInputOrNull(coverMediaId)
            if (content == null) {
                uploadedMediaId?.let { mediaRepository.deleteUnreferenced(it) }
                showError()
                _uiState.update {
                    it.copy(operation = it.operation.copy(isSaving = false))
                }
                return null
            }
            return when (
                val change =
                    repository.submitChange(
                        tourId = route.tourId,
                        input =
                            SubmitTourChangeInput(
                                baseVersion = state.tourVersion,
                                content = content,
                            ),
                    )
            ) {
                is DataResult.Error -> {
                    uploadedMediaId?.let { mediaRepository.deleteUnreferenced(it) }
                    finishWithError(change.error.toMessage(resourceProvider))
                    null
                }
                is DataResult.Success -> {
                    val updated =
                        state.copy(
                            identity =
                                state.identity.copy(tourVersion = change.data.details.tour.version),
                            content =
                                state.content.copy(
                                    coverMediaId = coverMediaId,
                                    coverImageUrl = change.data.details.tour.coverImageUrl,
                                    selectedCoverImageUri = null,
                                ),
                            operation =
                                state.operation.copy(
                                    contentReviewSubmitted = true,
                                    requiresReviewConfirmation = false,
                                ),
                        )
                    originalState = originalState?.withContentFrom(updated)
                    _uiState.value =
                        updated.copy(operation = updated.operation.copy(isSaving = true))
                    updated
                }
            }
        }

        private suspend fun updateSessionIfNeeded(
            state: GuideTourEditUiState,
            sessionInput: TourSessionInput,
        ): GuideTourEditUiState? {
            if (!state.hasSessionChangesFrom(originalState)) return state
            return when (
                val result =
                    repository.updateSession(
                        sessionId = route.sessionId,
                        input =
                            UpdateTourSessionInput(
                                version = state.sessionVersion,
                                session = sessionInput,
                            ),
                    )
            ) {
                is DataResult.Error -> {
                    val message = result.error.toMessage(resourceProvider)
                    finishWithError(
                        if (state.contentReviewSubmitted) {
                            resourceProvider.getString(R.string.tour_edit_partial_success, message)
                        } else {
                            message
                        },
                    )
                    null
                }
                is DataResult.Success -> {
                    originalState = originalState?.withSessionFrom(state)
                    state.copy(
                        identity = state.identity.copy(sessionVersion = result.data.version),
                    )
                }
            }
        }

        fun onSavedHandled() {
            _uiState.update {
                it.copy(operation = it.operation.copy(savedTargetTab = null))
            }
        }

        fun onUserMessageShown() {
            _uiState.update {
                it.copy(operation = it.operation.copy(userMessage = null))
            }
        }

        private fun setInitialState(details: TourDetails) {
            val session = details.session(route.sessionId)
            if (session == null) {
                _uiState.update {
                    it.copy(
                        operation =
                            it.operation.copy(
                                loadState = ContentLoadState.ERROR,
                                userMessage =
                                    resourceProvider.getString(
                                        R.string.tour_operation_session_not_found,
                                    ),
                            ),
                    )
                }
                return
            }
            val zone = details.tour.timeZoneId.toZoneId()
            val state =
                GuideTourEditUiState(
                    identity =
                        GuideTourEditIdentityState(
                            tourId = details.tour.id,
                            sessionId = session.id,
                            tourVersion = details.tour.version,
                            sessionVersion = session.version,
                            country = details.tour.country,
                            countryCode = details.tour.countryCode,
                            location = details.tour.city,
                            cityPlaceId = details.tour.cityPlaceId,
                            timeZoneId = details.tour.timeZoneId,
                            isTourIdentityLocked = true,
                        ),
                    content =
                        GuideTourEditContentFormState(
                            title = details.tour.title,
                            description = details.tour.description,
                            category = details.tour.category,
                            languages = details.tour.languages,
                            coverImageUrl = details.tour.coverImageUrl,
                            coverMediaId = details.tour.coverMediaId,
                        ),
                    session =
                        GuideTourEditSessionFormState(
                            meetingPoint = session.meetingPoint,
                            tourDate = session.startsAt.atZone(zone).toLocalDate(),
                            startTime = session.startsAt.atZone(zone).toLocalTime(),
                            durationMinutes = session.durationMinutes.toString(),
                            price = session.priceMinor.toCurrencyInput(),
                            capacity = session.capacity.toString(),
                            hasBookings = session.bookedCount > 0,
                        ),
                    operation =
                        GuideTourEditOperationState(
                            approvalStatus = details.tour.approvalStatus,
                            requiresReviewConfirmation =
                                details.tour.approvalStatus == TourApprovalStatus.REJECTED,
                            loadState = ContentLoadState.CONTENT,
                        ),
                )
            originalApprovalStatus = details.tour.approvalStatus
            originalState = state
            _uiState.value = state
        }

        private fun updateForm(transform: GuideTourEditUiState.() -> GuideTourEditUiState) {
            _uiState.update { state ->
                val updated = state.transform()
                updated.copy(
                    operation =
                        updated.operation.copy(
                            hasUnsavedChanges = updated.hasChangesFrom(originalState),
                            requiresReviewConfirmation =
                                updated.hasContentChangesFrom(originalState) ||
                                    originalApprovalStatus == TourApprovalStatus.REJECTED,
                            errorResId = null,
                        ),
                )
            }
        }

        private fun finishWithError(message: String) {
            _uiState.update { state ->
                state.copy(
                    operation =
                        state.operation.copy(
                            isSaving = false,
                            hasUnsavedChanges = state.hasChangesFrom(originalState),
                            userMessage = message,
                        ),
                )
            }
        }

        private fun showError(@StringRes errorResId: Int = R.string.error_tour_edit_invalid) {
            _uiState.update {
                it.copy(operation = it.operation.copy(errorResId = errorResId))
            }
        }
    }
