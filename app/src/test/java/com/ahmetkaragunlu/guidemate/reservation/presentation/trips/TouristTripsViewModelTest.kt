package com.ahmetkaragunlu.guidemate.reservation.presentation.trips

import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationCancellationResult
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationListType
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationRefundEligibility
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationRefundStatus
import com.ahmetkaragunlu.guidemate.reservation.domain.model.TouristReservationStatus
import com.ahmetkaragunlu.guidemate.reservation.presentation.trips.model.TripTab
import com.ahmetkaragunlu.guidemate.testing.FakeReservationRepository
import com.ahmetkaragunlu.guidemate.testing.FakeResourceProvider
import com.ahmetkaragunlu.guidemate.testing.FakeWalletRepository
import com.ahmetkaragunlu.guidemate.testing.reservationPage
import com.ahmetkaragunlu.guidemate.testing.testReservation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TouristTripsViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun successfulCancellationUsesReservationVersionAndMovesToPastTab() =
        runTest {
            val repository =
                FakeReservationRepository().apply {
                    reservationPages += DataResult.Success(reservationPage(testReservation()))
                    reservationPages += DataResult.Success(reservationPage())
                    cancellationResult = successfulWalletRefund()
                }
            val walletRepository = FakeWalletRepository()
            val viewModel =
                TouristTripsViewModel(repository, walletRepository, FakeResourceProvider())
            runCurrent()

            viewModel.cancelReservation("reservation-1")
            runCurrent()

            assertEquals("reservation-1", repository.cancellationRequest?.first)
            assertEquals(4L, repository.cancellationRequest?.second?.version)
            assertNotNull(repository.cancellationRequest?.third)
            assertEquals(TripTab.PAST, viewModel.uiState.value.selectedTab)
            assertTrue(viewModel.uiState.value.cancellationFeedback?.isSuccess == true)
            assertEquals(1, walletRepository.getWalletCalls)
            assertEquals(
                listOf(ReservationListType.UPCOMING, ReservationListType.PAST),
                repository.listRequests,
            )
        }

    @Test
    fun walletRefreshFailureDoesNotTurnSuccessfulCancellationIntoFailure() =
        runTest {
            val repository =
                FakeReservationRepository().apply {
                    reservationPages += DataResult.Success(reservationPage(testReservation()))
                    reservationPages += DataResult.Success(reservationPage())
                    cancellationResult = successfulWalletRefund()
                }
            val walletRepository =
                FakeWalletRepository().apply {
                    walletResult = DataResult.Error(AppError.NoResponseFromServer)
                }
            val viewModel =
                TouristTripsViewModel(repository, walletRepository, FakeResourceProvider())
            runCurrent()

            viewModel.cancelReservation("reservation-1")
            runCurrent()

            assertEquals(1, walletRepository.getWalletCalls)
            assertEquals(TripTab.PAST, viewModel.uiState.value.selectedTab)
            assertTrue(viewModel.uiState.value.cancellationFeedback?.isSuccess == true)
        }

    @Test
    fun purchaseCompletionSelectsUpcomingAndReloadsItsReservations() =
        runTest {
            val repository =
                FakeReservationRepository().apply {
                    repeat(3) {
                        reservationPages += DataResult.Success(reservationPage())
                    }
                }
            val viewModel =
                TouristTripsViewModel(repository, FakeWalletRepository(), FakeResourceProvider())
            runCurrent()
            viewModel.changeTab(TripTab.PAST)
            runCurrent()

            viewModel.showUpcomingAfterPurchase()
            runCurrent()

            assertEquals(TripTab.UPCOMING, viewModel.uiState.value.selectedTab)
            assertEquals(
                listOf(
                    ReservationListType.UPCOMING,
                    ReservationListType.PAST,
                    ReservationListType.UPCOMING,
                ),
                repository.listRequests,
            )
        }

    private fun successfulWalletRefund(): DataResult<ReservationCancellationResult> =
        DataResult.Success(
            ReservationCancellationResult(
                reservation = testReservation(status = TouristReservationStatus.CANCELLED),
                refundEligibility = ReservationRefundEligibility.FULL_REFUND,
                refundId = "refund-1",
                refundStatus = ReservationRefundStatus.SUCCEEDED,
            ),
        )
}
