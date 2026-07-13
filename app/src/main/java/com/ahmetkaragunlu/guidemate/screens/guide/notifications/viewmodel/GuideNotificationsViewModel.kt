package com.ahmetkaragunlu.guidemate.screens.guide.notifications.viewmodel

import androidx.lifecycle.ViewModel
import com.ahmetkaragunlu.guidemate.screens.guide.notifications.model.GuideNotificationType
import com.ahmetkaragunlu.guidemate.screens.guide.notifications.model.GuideNotificationUiModel
import com.ahmetkaragunlu.guidemate.screens.guide.notifications.model.GuideNotificationsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class GuideNotificationsViewModel
    @Inject
    constructor() : ViewModel() {
        private val _uiState = MutableStateFlow(createMockUiState())
        val uiState: StateFlow<GuideNotificationsUiState> = _uiState.asStateFlow()

        private companion object {
            private const val MINUTE_MILLIS = 60_000L
            private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
            private const val DAY_MILLIS = 24 * HOUR_MILLIS

            // Mock data: Backend notification records will replace this list after MVP.
            fun createMockUiState(): GuideNotificationsUiState {
                val now = System.currentTimeMillis()
                return GuideNotificationsUiState(
                    notifications =
                        listOf(
                            GuideNotificationUiModel(
                                id = "notification-tour-approved-1",
                                type = GuideNotificationType.TOUR_APPROVED,
                                tourTitle = "Kapadokya Balon Turu",
                                occurredAtMillis = now - (5 * MINUTE_MILLIS),
                                isRead = false,
                            ),
                            GuideNotificationUiModel(
                                id = "notification-tour-rejected-1",
                                type = GuideNotificationType.TOUR_REJECTED,
                                tourTitle = "Mardin Taş Evleri",
                                rejectionReason =
                                    "Tur açıklamasını ve buluşma noktası bilgisini daha ayrıntılı yazmalısınız.",
                                occurredAtMillis = now - (10 * MINUTE_MILLIS),
                                isRead = false,
                            ),
                            GuideNotificationUiModel(
                                id = "notification-comment-1",
                                type = GuideNotificationType.COMMENT_RECEIVED,
                                actorName = "Elif",
                                tourTitle = "Ayasofya Tarih Turu",
                                commentPreview = "Rehberin anlatımı çok başarılıydı.",
                                occurredAtMillis = now - (15 * MINUTE_MILLIS),
                                isRead = false,
                            ),
                            GuideNotificationUiModel(
                                id = "notification-rating-1",
                                type = GuideNotificationType.RATING_RECEIVED,
                                actorName = "Elif",
                                tourTitle = "Ayasofya Tarih Turu",
                                rating = 5,
                                occurredAtMillis = now - (2 * HOUR_MILLIS),
                                isRead = false,
                            ),
                            GuideNotificationUiModel(
                                id = "notification-reservation-1",
                                type = GuideNotificationType.TOUR_PURCHASED,
                                actorName = "Ahmet",
                                tourTitle = "Kapadokya Balon Turu",
                                occurredAtMillis = now - DAY_MILLIS,
                                isRead = true,
                            ),
                            GuideNotificationUiModel(
                                id = "notification-withdrawal-1",
                                type = GuideNotificationType.WITHDRAWAL_COMPLETED,
                                amount = 5_000.0,
                                occurredAtMillis = now - (2 * DAY_MILLIS),
                                isRead = true,
                            ),
                        ),
                )
            }
        }
    }
