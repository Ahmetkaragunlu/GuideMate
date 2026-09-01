package com.ahmetkaragunlu.guidemate.reservation.presentation.detail

import androidx.lifecycle.SavedStateHandle
import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.reservation.domain.model.TouristReservationStatus
import com.ahmetkaragunlu.guidemate.testing.FakeReservationRepository
import com.ahmetkaragunlu.guidemate.testing.FakeResourceProvider
import com.ahmetkaragunlu.guidemate.testing.FakeReviewRepository
import com.ahmetkaragunlu.guidemate.testing.testReservation
import com.ahmetkaragunlu.guidemate.testing.testSubmittedReview
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
            val viewModel =
                TouristReservationDetailViewModel(
                    savedStateHandle = SavedStateHandle(mapOf("reservationId" to "reservation-1")),
                    reservationRepository = reservationRepository,
                    reviewRepository = reviewRepository,
                    resourceProvider = FakeResourceProvider(),
                )
            runCurrent()

            assertTrue(viewModel.uiState.value.canSubmitReview)
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
}
