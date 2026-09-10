package com.ahmetkaragunlu.guidemate.reservation.presentation.detail

import androidx.lifecycle.SavedStateHandle
import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.reservation.domain.model.TouristReservationStatus
import com.ahmetkaragunlu.guidemate.testing.FakeReservationRepository
import com.ahmetkaragunlu.guidemate.testing.FakeResourceProvider
import com.ahmetkaragunlu.guidemate.testing.FakeNotificationRepository
import com.ahmetkaragunlu.guidemate.testing.FakeReviewRepository
import com.ahmetkaragunlu.guidemate.testing.testReservation
import com.ahmetkaragunlu.guidemate.testing.testSubmittedReview
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourReview
import java.time.Instant
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class TouristReservationDetailViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun completedReservationAllowsOneValidatedReviewSubmission() =
        runTest {
            val reservationRepository =
                FakeReservationRepository().apply {
                    reservationResult =
                        com.ahmetkaragunlu.guidemate.common.result.DataResult.Success(
                            testReservation(status = TouristReservationStatus.COMPLETED)
                        )
                }
            val reviewRepository = FakeReviewRepository()
            val notificationRepository = FakeNotificationRepository()
            val viewModel =
                TouristReservationDetailViewModel(
                    savedStateHandle = SavedStateHandle(mapOf("reservationId" to "reservation-1")),
                    reservationRepository = reservationRepository,
                    reviewRepository = reviewRepository,
                    notificationRepository = notificationRepository,
                    resourceProvider = FakeResourceProvider(),
                )
            runCurrent()

            assertTrue(viewModel.uiState.value.canSubmitReview)
            assertEquals(
                "reservation-1",
                notificationRepository.markedRelatedTargets.single().targetId,
            )
            viewModel.showReviewForm()
            viewModel.updateReviewRating(5)
            viewModel.updateReviewComment("Excellent tour")
            reservationRepository.reservationResult =
                com.ahmetkaragunlu.guidemate.common.result.DataResult.Success(
                    testReservation(status = TouristReservationStatus.COMPLETED).copy(
                        averageRating = 4.9,
                        reviewCount = 18,
                        review = testSubmittedReview(),
                    )
                )
            viewModel.submitReview()
            runCurrent()

            assertEquals("reservation-1", reviewRepository.submittedReview?.first)
            assertEquals(5, reviewRepository.submittedReview?.second?.rating)
            assertEquals("Excellent tour", reviewRepository.submittedReview?.second?.comment)
            assertTrue(viewModel.uiState.value.reviewForm.showSuccessDialog)
            val detail = requireNotNull(viewModel.uiState.value.detail)
            assertEquals(4.9, detail.rating ?: 0.0, 0.0)
            assertEquals(18L, detail.reviewCount)
            assertFalse(viewModel.uiState.value.canSubmitReview)
        }

    @Test
    fun reviewFailureKeepsReservationVisibleAndRetryLoadsOnlyReviews() =
        runTest {
            val reservationRepository =
                FakeReservationRepository().apply {
                    reservationResult = DataResult.Success(testReservation())
                }
            val reviewRepository =
                FakeReviewRepository().apply {
                    reviewsResults += DataResult.Error(AppError.NoInternet)
                    reviewsResults += DataResult.Success(reviewPage())
                }
            val viewModel =
                TouristReservationDetailViewModel(
                    savedStateHandle = SavedStateHandle(mapOf("reservationId" to "reservation-1")),
                    reservationRepository = reservationRepository,
                    reviewRepository = reviewRepository,
                    notificationRepository = FakeNotificationRepository(),
                    resourceProvider = FakeResourceProvider(),
                )
            advanceUntilIdle()

            assertEquals(ContentLoadState.CONTENT, viewModel.uiState.value.loadState)
            assertEquals(ContentLoadState.ERROR, viewModel.uiState.value.reviewsLoadState)
            assertTrue(viewModel.uiState.value.detail != null)
            assertTrue(viewModel.uiState.value.detail?.reviews.orEmpty().isEmpty())

            viewModel.retryReviews()
            advanceUntilIdle()

            assertEquals(listOf("tour-1", "tour-1"), reviewRepository.tourReviewRequests)
            assertEquals(listOf("reservation-1"), reservationRepository.reservationRequests)
            assertEquals(ContentLoadState.CONTENT, viewModel.uiState.value.reviewsLoadState)
            assertEquals("review-1", viewModel.uiState.value.detail?.reviews?.single()?.id)
        }

    private fun reviewPage(): PagedResult<TourReview> =
        PagedResult(
            items =
                listOf(
                    TourReview(
                        id = "review-1",
                        reviewerName = "Ada",
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
}
