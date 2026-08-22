package com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.error.toMessage
import com.ahmetkaragunlu.guidemate.common.ui.formatting.isValidCurrencyInput
import com.ahmetkaragunlu.guidemate.common.ui.formatting.toCurrencyInput
import com.ahmetkaragunlu.guidemate.common.ui.formatting.toCurrencyMinorUnitsOrNull
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.navigation.guide.tours.GuideTourDestination
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourApprovalStatus
import com.ahmetkaragunlu.guidemate.tour.domain.model.catalog.TourWithSession
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourDetails
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.TourSessionInput
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.isEffectivelyTerminal
import com.ahmetkaragunlu.guidemate.tour.domain.repository.GuideTourRepository
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.mapper.toTourDetailUiState
import com.ahmetkaragunlu.guidemate.tour.presentation.detail.model.TourDetailMode
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.detail.model.GuideTourDetailActionUiState
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.detail.model.GuideTourDetailScreenState
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.detail.model.NewTourSessionFormState
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.model.GuideTourTab
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class GuideTourDetailViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val repository: GuideTourRepository,
        private val resourceProvider: ResourceProvider,
    ) : ViewModel() {
        private val route = savedStateHandle.toRoute<GuideTourDestination.Detail>()
        private val _uiState = MutableStateFlow(GuideTourDetailScreenState())
        val uiState = _uiState.asStateFlow()

        private var details: TourDetails? = null
        private var cancellationIdempotencyKey: String? = null

        init {
            refresh()
        }

        fun refresh() {
            viewModelScope.launch {
                _uiState.update { it.copy(loadState = ContentLoadState.LOADING) }
                when (val result = repository.getTour(route.tourId)) {
                    is DataResult.Success -> {
                        val session = result.data.session(route.sessionId)
                        if (session == null) {
                            _uiState.update {
                                it.copy(
                                    loadState = ContentLoadState.ERROR,
                                    userMessage =
                                        resourceProvider.getString(
                                            R.string.tour_operation_session_not_found,
                                        ),
                                )
                            }
                        } else {
                            details = result.data
                            val now = Instant.now()
                            _uiState.update {
                                it.copy(
                                    detail =
                                        TourWithSession(result.data.tour, session)
                                            .toTourDetailUiState(now),
                                    mode =
                                        when {
                                            result.data.tour.approvalStatus !=
                                                TourApprovalStatus.APPROVED ->
                                                TourDetailMode.GUIDE_REVIEW
                                            session.isEffectivelyTerminal(now) ->
                                                TourDetailMode.GUIDE_PAST
                                            else -> TourDetailMode.GUIDE_ACTIVE
                                        },
                                    loadState = ContentLoadState.CONTENT,
                                )
                            }
                        }
                    }
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

        fun showCancelDialog() {
            _uiState.update {
                it.copy(action = it.action.copy(isCancelDialogVisible = true))
            }
        }

        fun dismissCancelDialog() {
            if (_uiState.value.action.isSubmitting) return
            cancellationIdempotencyKey = null
            _uiState.update {
                it.copy(
                    action =
                        it.action.copy(
                            isCancelDialogVisible = false,
                            cancellationReason = "",
                        ),
                )
            }
        }

        fun onCancellationReasonChange(reason: String) {
            _uiState.update {
                it.copy(action = it.action.copy(cancellationReason = reason))
            }
        }

        fun cancelSession() {
            val reason = _uiState.value.action.cancellationReason.trim()
            if (reason.isBlank() || _uiState.value.action.isSubmitting) return
            val idempotencyKey =
                cancellationIdempotencyKey ?: UUID.randomUUID().toString().also {
                    cancellationIdempotencyKey = it
                }
            setSubmitting(true)
            viewModelScope.launch {
                when (
                    val result =
                        repository.cancelSession(
                            sessionId = route.sessionId,
                            reason = reason,
                            idempotencyKey = idempotencyKey,
                        )
                ) {
                    is DataResult.Success -> {
                        cancellationIdempotencyKey = null
                        _uiState.update {
                            it.copy(
                                action = GuideTourDetailActionUiState(),
                                finishedTab = GuideTourTab.PAST,
                            )
                        }
                    }
                    is DataResult.Error -> {
                        setSubmitting(false)
                        showMessage(result.error.toMessage(resourceProvider))
                    }
                }
            }
        }

        fun showNewSessionSheet() {
            val current = details ?: return
            val session = current.session(route.sessionId) ?: return
            _uiState.update { state ->
                state.copy(
                    action =
                        state.action.copy(
                            isNewSessionSheetVisible = true,
                            newSessionForm =
                                NewTourSessionFormState(
                                    timeZoneId = current.tour.timeZoneId,
                                    durationMinutes = session.durationMinutes,
                                    meetingPoint = session.meetingPoint,
                                    price = session.priceMinor.toCurrencyInput(),
                                    capacity = session.capacity.toString(),
                                ),
                        ),
                )
            }
        }

        fun dismissNewSessionSheet() {
            if (_uiState.value.action.isSubmitting) return
            _uiState.update {
                it.copy(action = GuideTourDetailActionUiState())
            }
        }

        fun onNewSessionDateSelected(date: LocalDate) {
            updateNewSessionForm {
                val zoneId = runCatching { ZoneId.of(timeZoneId) }.getOrNull()
                val today = zoneId?.let(LocalDate::now)
                val currentTime = zoneId?.let(LocalTime::now)
                val validSelectedTime =
                    selectedTime?.takeIf { time ->
                        today == null ||
                            currentTime == null ||
                            date.isAfter(today) ||
                            time.isAfter(currentTime)
                    }
                copy(selectedDate = date, selectedTime = validSelectedTime)
            }
        }

        fun onNewSessionTimeSelected(time: LocalTime) {
            updateNewSessionForm { copy(selectedTime = time) }
        }

        fun onNewSessionDurationSelected(durationMinutes: Int) {
            updateNewSessionForm { copy(durationMinutes = durationMinutes) }
        }

        fun onNewSessionMeetingPointChange(value: String) {
            updateNewSessionForm { copy(meetingPoint = value) }
        }

        fun onNewSessionPriceChange(value: String) {
            if (value.isValidCurrencyInput()) updateNewSessionForm { copy(price = value) }
        }

        fun onNewSessionCapacityChange(value: String) {
            if (value.all(Char::isDigit)) updateNewSessionForm { copy(capacity = value) }
        }

        fun addNewSession() {
            val form = _uiState.value.action.newSessionForm
            if (!form.canSubmit || _uiState.value.action.isSubmitting) {
                showNewSessionError()
                return
            }
            val selectedDate = form.selectedDate ?: return showNewSessionError()
            val selectedTime = form.selectedTime ?: return showNewSessionError()
            val startsAt =
                runCatching {
                    selectedDate
                        .atTime(selectedTime)
                        .atZone(ZoneId.of(form.timeZoneId))
                        .toInstant()
                }.getOrNull() ?: return showNewSessionError()
            val input =
                TourSessionInput(
                    meetingPoint = form.meetingPoint.trim(),
                    startsAt = startsAt,
                    durationMinutes = form.durationMinutes ?: return showNewSessionError(),
                    priceMinor = form.price.toCurrencyMinorUnitsOrNull() ?: return showNewSessionError(),
                    capacity = form.capacity.toIntOrNull() ?: return showNewSessionError(),
                )
            setSubmitting(true)
            viewModelScope.launch {
                when (val result = repository.addSession(route.tourId, input)) {
                    is DataResult.Success -> {
                        _uiState.update {
                            it.copy(
                                action = GuideTourDetailActionUiState(),
                                finishedTab = GuideTourTab.ACTIVE,
                            )
                        }
                    }
                    is DataResult.Error -> {
                        setSubmitting(false)
                        showMessage(result.error.toMessage(resourceProvider))
                    }
                }
            }
        }

        fun onFinishedHandled() {
            _uiState.update { it.copy(finishedTab = null) }
        }

        fun onUserMessageShown() {
            _uiState.update { it.copy(userMessage = null) }
        }

        private fun updateNewSessionForm(
            transform: NewTourSessionFormState.() -> NewTourSessionFormState,
        ) {
            _uiState.update { state ->
                state.copy(
                    action =
                        state.action.copy(
                            newSessionForm =
                                state.action.newSessionForm.transform().copy(errorResId = null),
                        ),
                )
            }
        }

        private fun setSubmitting(value: Boolean) {
            _uiState.update { it.copy(action = it.action.copy(isSubmitting = value)) }
        }

        private fun showNewSessionError() {
            _uiState.update { state ->
                state.copy(
                    action =
                        state.action.copy(
                            newSessionForm =
                                state.action.newSessionForm.copy(
                                    errorResId = R.string.error_session_creation,
                                ),
                        ),
                )
            }
        }

        private fun showMessage(message: String) {
            _uiState.update { it.copy(userMessage = message) }
        }
    }
