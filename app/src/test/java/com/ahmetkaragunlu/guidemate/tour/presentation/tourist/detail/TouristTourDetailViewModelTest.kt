package com.ahmetkaragunlu.guidemate.tour.presentation.tourist.detail

import androidx.lifecycle.SavedStateHandle
import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.testing.FakeNotificationRepository
import com.ahmetkaragunlu.guidemate.testing.FakeReviewRepository
import com.ahmetkaragunlu.guidemate.testing.FakeResourceProvider
import com.ahmetkaragunlu.guidemate.testing.FakeTourDiscoveryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class TouristTourDetailViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `successful detail load marks only the related tour notifications read`() =
        runTest {
            val notificationRepository = FakeNotificationRepository()
            val viewModel =
                createViewModel(
                    tourRepository = FakeTourDiscoveryRepository(),
                    notificationRepository = notificationRepository,
                )

            runCurrent()

            assertEquals(ContentLoadState.CONTENT, viewModel.uiState.value.loadState)
            assertEquals("tour-1", notificationRepository.markedRelatedTargets.single().targetId)
        }

    @Test
    fun `failed detail load does not mark related notifications read`() =
        runTest {
            val tourRepository = FakeTourDiscoveryRepository().apply {
                sessionResult = DataResult.Error(AppError.NoInternet)
            }
            val notificationRepository = FakeNotificationRepository()
            val viewModel = createViewModel(tourRepository, notificationRepository)

            runCurrent()

            assertEquals(ContentLoadState.ERROR, viewModel.uiState.value.loadState)
            assertTrue(notificationRepository.markedRelatedTargets.isEmpty())
        }

    @Test
    fun `review failure keeps tour detail visible and retry reloads only reviews`() =
        runTest {
            val reviewRepository =
                FakeReviewRepository().apply {
                    reviewsResults += DataResult.Error(AppError.NoInternet)
                    reviewsResults += DataResult.Success(com.ahmetkaragunlu.guidemate.testing.emptyPage())
                }
            val tourRepository = FakeTourDiscoveryRepository()
            val viewModel =
                createViewModel(
                    tourRepository = tourRepository,
                    notificationRepository = FakeNotificationRepository(),
                    reviewRepository = reviewRepository,
                )
            runCurrent()

            assertEquals(ContentLoadState.CONTENT, viewModel.uiState.value.loadState)
            assertEquals(ContentLoadState.ERROR, viewModel.uiState.value.reviewsLoadState)
            assertTrue(viewModel.uiState.value.detail != null)

            viewModel.retryReviews()
            runCurrent()

            assertEquals(ContentLoadState.CONTENT, viewModel.uiState.value.loadState)
            assertEquals(ContentLoadState.CONTENT, viewModel.uiState.value.reviewsLoadState)
            assertEquals(listOf("tour-1", "tour-1"), reviewRepository.tourReviewRequests)
        }

    private fun createViewModel(
        tourRepository: FakeTourDiscoveryRepository,
        notificationRepository: FakeNotificationRepository,
        reviewRepository: FakeReviewRepository = FakeReviewRepository(),
    ) =
        TouristTourDetailViewModel(
            savedStateHandle = SavedStateHandle(mapOf("sessionId" to "session-1")),
            tourRepository = tourRepository,
            reviewRepository = reviewRepository,
            notificationRepository = notificationRepository,
            resourceProvider = FakeResourceProvider(),
        )
}
