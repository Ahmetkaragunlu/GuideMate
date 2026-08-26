package com.ahmetkaragunlu.guidemate.reservation.presentation.trips

import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.reservation.domain.model.ReservationListType
import com.ahmetkaragunlu.guidemate.reservation.presentation.trips.model.TripTab
import com.ahmetkaragunlu.guidemate.testing.FakeReservationRepository
import com.ahmetkaragunlu.guidemate.testing.FakeResourceProvider
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
                }
            val viewModel = TouristTripsViewModel(repository, FakeResourceProvider())
            runCurrent()

            viewModel.cancelReservation("reservation-1")
            runCurrent()

            assertEquals("reservation-1", repository.cancellationRequest?.first)
            assertEquals(4L, repository.cancellationRequest?.second?.version)
            assertNotNull(repository.cancellationRequest?.third)
            assertEquals(TripTab.PAST, viewModel.uiState.value.selectedTab)
            assertTrue(viewModel.uiState.value.cancellationFeedback?.isSuccess == true)
            assertEquals(
                listOf(ReservationListType.UPCOMING, ReservationListType.PAST),
                repository.listRequests,
            )
        }
}
