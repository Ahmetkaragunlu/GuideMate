package com.ahmetkaragunlu.guidemate.screens.guide.tours.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.navigation.guide.GUIDE_TOUR_SESSION_ID_ARGUMENT
import com.ahmetkaragunlu.guidemate.screens.common.tours.detail.model.TourDetailMode
import com.ahmetkaragunlu.guidemate.screens.guide.tours.detail.model.GuideTourDetailActionUiState
import com.ahmetkaragunlu.guidemate.screens.guide.tours.detail.model.GuideTourDetailScreenState
import com.ahmetkaragunlu.guidemate.screens.guide.tours.detail.model.NewTourSessionFormState
import com.ahmetkaragunlu.guidemate.screens.guide.tours.mapper.toTourDetailUiState
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.CancelTourSessionRequest
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.CreateTourSessionRequest
import com.ahmetkaragunlu.guidemate.screens.guide.tours.model.TourApprovalStatus
import com.ahmetkaragunlu.guidemate.screens.guide.tours.shared.GuideTourStore
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

@HiltViewModel
class GuideTourDetailViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val tourStore: GuideTourStore,
    ) : ViewModel() {
        private val sessionId: String =
            checkNotNull(savedStateHandle[GUIDE_TOUR_SESSION_ID_ARGUMENT])
        private val actionState = MutableStateFlow(GuideTourDetailActionUiState())

        val uiState: StateFlow<GuideTourDetailScreenState?> =
            combine(tourStore.state, actionState) { catalog, action ->
                catalog.findBySessionId(sessionId)?.let { tourWithSession ->
                    GuideTourDetailScreenState(
                        detail = tourWithSession.toTourDetailUiState(),
                        mode =
                            when {
                                tourWithSession.tour.approvalStatus != TourApprovalStatus.APPROVED ->
                                    TourDetailMode.GUIDE_REVIEW

                                tourWithSession.session.status.isTerminal ->
                                    TourDetailMode.GUIDE_PAST

                                else -> TourDetailMode.GUIDE_ACTIVE
                            },
                        action = action,
                    )
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null,
            )

        fun showCancelDialog() {
            actionState.update { it.copy(isCancelDialogVisible = true) }
        }

        fun dismissCancelDialog() {
            actionState.update {
                it.copy(
                    isCancelDialogVisible = false,
                    cancellationReason = "",
                )
            }
        }

        fun onCancellationReasonChange(reason: String) {
            actionState.update { it.copy(cancellationReason = reason) }
        }

        fun cancelSession(): Boolean {
            val result =
                tourStore.cancelSession(
                    CancelTourSessionRequest(
                        sessionId = sessionId,
                        reason = actionState.value.cancellationReason,
                    ),
                )
            if (result) dismissCancelDialog()
            return result
        }

        fun showNewSessionSheet() {
            val current = tourStore.state.value.findBySessionId(sessionId) ?: return
            actionState.update { state ->
                state.copy(
                    isNewSessionSheetVisible = true,
                    newSessionForm =
                        NewTourSessionFormState(
                            timeZoneId = current.tour.timeZoneId,
                            durationMinutes = current.session.durationMinutes,
                            meetingPoint = current.session.meetingPoint,
                            price = current.session.price.toInt().toString(),
                            capacity = current.session.capacity.toString(),
                        ),
                )
            }
        }

        fun dismissNewSessionSheet() {
            actionState.update {
                it.copy(
                    isNewSessionSheetVisible = false,
                    newSessionForm = NewTourSessionFormState(),
                )
            }
        }

        fun onNewSessionDateSelected(date: LocalDate) {
            updateNewSessionForm {
                val zoneId = runCatching { ZoneId.of(timeZoneId) }.getOrNull()
                val today = zoneId?.let { LocalDate.now(it) }
                val currentTime = zoneId?.let { LocalTime.now(it) }
                val validSelectedTime =
                    selectedTime?.takeIf { time ->
                        today == null ||
                            currentTime == null ||
                            date.isAfter(today) ||
                            time.isAfter(currentTime)
                    }
                copy(
                    selectedDate = date,
                    selectedTime = validSelectedTime,
                )
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
            if (value.all(Char::isDigit)) {
                updateNewSessionForm { copy(price = value) }
            }
        }

        fun onNewSessionCapacityChange(value: String) {
            if (value.all(Char::isDigit)) {
                updateNewSessionForm { copy(capacity = value) }
            }
        }

        fun addNewSession(): Boolean {
            val form = actionState.value.newSessionForm
            if (!form.canSubmit) return showNewSessionError()
            val current =
                tourStore.state.value.findBySessionId(sessionId) ?: return showNewSessionError()
            val selectedDate = form.selectedDate ?: return showNewSessionError()
            val selectedTime = form.selectedTime ?: return showNewSessionError()
            val startsAt =
                runCatching {
                    selectedDate
                        .atTime(selectedTime)
                        .atZone(ZoneId.of(current.tour.timeZoneId))
                        .toInstant()
                }.getOrNull() ?: return showNewSessionError()
            val result =
                tourStore.addSession(
                    CreateTourSessionRequest(
                        tourId = current.tour.id,
                        meetingPoint = form.meetingPoint,
                        startsAt = startsAt,
                        durationMinutes = form.durationMinutes ?: 0,
                        price = form.price.toDoubleOrNull() ?: 0.0,
                        capacity = form.capacity.toIntOrNull() ?: 0,
                    ),
                )
            if (result) {
                actionState.value = GuideTourDetailActionUiState()
            } else {
                showNewSessionError()
            }
            return result
        }

        private fun updateNewSessionForm(
            transform: NewTourSessionFormState.() -> NewTourSessionFormState,
        ) {
            actionState.update { state ->
                state.copy(newSessionForm = state.newSessionForm.transform().copy(errorResId = null))
            }
        }

        private fun showNewSessionError(): Boolean {
            actionState.update { state ->
                state.copy(
                    newSessionForm =
                        state.newSessionForm.copy(
                            errorResId = R.string.error_session_creation,
                        ),
                )
            }
            return false
        }
    }
