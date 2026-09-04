package com.ahmetkaragunlu.guidemate.home.presentation.guide

import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationNavigationTarget
import com.ahmetkaragunlu.guidemate.notification.domain.model.NotificationType
import com.ahmetkaragunlu.guidemate.profile.domain.model.level.GuideLevelTier
import com.ahmetkaragunlu.guidemate.testing.FakeGuideTourRepository
import com.ahmetkaragunlu.guidemate.testing.FakeNotificationRepository
import com.ahmetkaragunlu.guidemate.testing.FakeUserRepository
import com.ahmetkaragunlu.guidemate.tour.domain.model.guide.GuideDashboard
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GuideHomeViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun adminTourDecisionPushRefreshesDashboardProjection() =
        runTest {
            val tourRepository =
                FakeGuideTourRepository().apply {
                    dashboardResults += DataResult.Success(dashboard(active = 0, pending = 1))
                    dashboardResults += DataResult.Success(dashboard(active = 1, pending = 0))
                }
            val notificationRepository = FakeNotificationRepository()
            val viewModel =
                GuideHomeViewModel(
                    userRepository = FakeUserRepository(),
                    tourRepository = tourRepository,
                    notificationRepository = notificationRepository,
                )
            runCurrent()

            notificationRepository.pushEventState.emit(
                NotificationNavigationTarget(
                    notificationId = "notification-1",
                    type = NotificationType.TOUR_APPROVED,
                    tourId = "tour-1",
                )
            )
            runCurrent()

            assertEquals(2, tourRepository.dashboardRequests)
            assertEquals(1L, viewModel.uiState.value.activeCount)
            assertEquals(0L, viewModel.uiState.value.pendingCount)
        }

    private fun dashboard(active: Long, pending: Long) =
        GuideDashboard(
            activeSessionCount = active,
            pendingReviewCount = pending,
            completedSessionCount = 0,
            totalParticipantCount = 0,
            averageRating = 0.0,
            reviewCount = 0,
            level = GuideLevelTier.APPROVED,
            currentMonthEarningsMinor = 0,
            currencyCode = "USD",
        )
}
