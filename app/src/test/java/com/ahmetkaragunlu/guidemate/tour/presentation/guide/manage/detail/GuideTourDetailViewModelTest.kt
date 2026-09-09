package com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.detail

import androidx.lifecycle.SavedStateHandle
import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.testing.FakeGuideTourRepository
import com.ahmetkaragunlu.guidemate.testing.FakeNotificationRepository
import com.ahmetkaragunlu.guidemate.testing.FakeReviewRepository
import com.ahmetkaragunlu.guidemate.testing.FakeResourceProvider
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourReview
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.model.GuideTourTab
import java.time.Instant
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class GuideTourDetailViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun cancellationRequiresReasonThenUsesIdempotentRequestAndFinishesInPast() =
        runTest {
            val repository = FakeGuideTourRepository()
            val notificationRepository = FakeNotificationRepository()
            val viewModel = createViewModel(repository, notificationRepository)
            runCurrent()

            assertEquals(ContentLoadState.CONTENT, viewModel.uiState.value.loadState)
            assertEquals("tour-1", notificationRepository.markedRelatedTargets.single().targetId)
            viewModel.cancelSession()
            assertNull(repository.cancelSessionRequest)

            viewModel.onCancellationReasonChange("  Weather conditions  ")
            viewModel.cancelSession()
            runCurrent()

            assertEquals("session-1", repository.cancelSessionRequest?.first)
            assertEquals("Weather conditions", repository.cancelSessionRequest?.second)
            assertNotNull(repository.cancelSessionRequest?.third)
            assertEquals(GuideTourTab.PAST, viewModel.uiState.value.finishedTab)
        }

    @Test
    fun successfulDetailLoadIncludesPublicTourReviews() =
        runTest {
            val reviewRepository =
                FakeReviewRepository().apply {
                    reviewsResult =
                        DataResult.Success(
                            PagedResult(
                                items =
                                    listOf(
                                        TourReview(
                                            id = "review-1",
                                            reviewerName = "Tourist",
                                            rating = 5,
                                            comment = "Excellent tour",
                                            submittedAt = Instant.parse("2026-01-01T00:00:00Z"),
                                        )
                                    ),
                                page = 0,
                                size = 20,
                                totalElements = 1,
                                totalPages = 1,
                                isFirst = true,
                                isLast = true,
                            )
                        )
                }

            val viewModel =
                createViewModel(
                    repository = FakeGuideTourRepository(),
                    notificationRepository = FakeNotificationRepository(),
                    reviewRepository = reviewRepository,
                )
            runCurrent()

            assertTrue(reviewRepository.ownedTourReviewsRequested)
            assertEquals("review-1", viewModel.uiState.value.detail?.reviews?.single()?.id)
        }

    @Test
    fun reviewFailureKeepsLoadedTourDetailAvailable() =
        runTest {
            val reviewRepository =
                FakeReviewRepository().apply {
                    reviewsResult = DataResult.Error(AppError.NoInternet)
                }

            val viewModel =
                createViewModel(
                    repository = FakeGuideTourRepository(),
                    notificationRepository = FakeNotificationRepository(),
                    reviewRepository = reviewRepository,
                )
            runCurrent()

            assertEquals(ContentLoadState.CONTENT, viewModel.uiState.value.loadState)
            assertNotNull(viewModel.uiState.value.detail)
            assertTrue(viewModel.uiState.value.detail?.reviews.orEmpty().isEmpty())
        }

    private fun createViewModel(
        repository: FakeGuideTourRepository,
        notificationRepository: FakeNotificationRepository,
        reviewRepository: FakeReviewRepository = FakeReviewRepository(),
    ) =
        GuideTourDetailViewModel(
            savedStateHandle =
                SavedStateHandle(
                    mapOf(
                        "tourId" to "tour-1",
                        "sessionId" to "session-1",
                    )
                ),
            repository = repository,
            reviewRepository = reviewRepository,
            notificationRepository = notificationRepository,
            resourceProvider = FakeResourceProvider(),
        )
}
