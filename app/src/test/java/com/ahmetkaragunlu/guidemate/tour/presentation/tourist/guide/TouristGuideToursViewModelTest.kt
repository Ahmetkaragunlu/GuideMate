package com.ahmetkaragunlu.guidemate.tour.presentation.tourist.guide

import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.testing.discovery.FakeTourDiscoveryRepository
import com.ahmetkaragunlu.guidemate.testing.discovery.testTourSearchItem
import com.ahmetkaragunlu.guidemate.testing.discovery.tourSearchPage
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
class TouristGuideToursViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `loads first page then appends final page`() =
        runTest {
            val repository =
                FakeTourDiscoveryRepository().apply {
                    results.popularForGuideResults +=
                        DataResult.Success(
                            tourSearchPage(
                                page = 0,
                                isLast = false,
                                testTourSearchItem("tour-1", "session-1"),
                            ),
                        )
                    results.popularForGuideResults +=
                        DataResult.Success(
                            tourSearchPage(
                                page = 1,
                                isLast = true,
                                testTourSearchItem("tour-2", "session-2"),
                            ),
                        )
                }
            val viewModel = TouristGuideToursViewModel(repository)

            viewModel.loadGuideTours(GUIDE_ID)
            runCurrent()

            assertEquals(listOf("session-1"), viewModel.uiState.value.tours.map { it.sessionId })
            assertTrue(viewModel.uiState.value.canLoadMore)

            viewModel.loadMore()
            runCurrent()

            assertEquals(
                listOf("session-1", "session-2"),
                viewModel.uiState.value.tours.map { it.sessionId },
            )
            assertFalse(viewModel.uiState.value.canLoadMore)
            assertEquals(listOf(0, 1), repository.calls.popularForGuideRequests.map { it.page })
        }

    @Test
    fun `append failure keeps loaded tours and exposes retry state`() =
        runTest {
            val repository =
                FakeTourDiscoveryRepository().apply {
                    results.popularForGuideResults +=
                        DataResult.Success(
                            tourSearchPage(
                                page = 0,
                                isLast = false,
                                testTourSearchItem("tour-1", "session-1"),
                            ),
                        )
                    results.popularForGuideResults += DataResult.Error(AppError.NoInternet)
                }
            val viewModel = TouristGuideToursViewModel(repository)

            viewModel.loadGuideTours(GUIDE_ID)
            runCurrent()
            viewModel.loadMore()
            runCurrent()

            assertEquals(ContentLoadState.CONTENT, viewModel.uiState.value.loadState)
            assertEquals(listOf("session-1"), viewModel.uiState.value.tours.map { it.sessionId })
            assertTrue(viewModel.uiState.value.appendFailed)
        }

    @Test
    fun `empty final page exposes content empty state`() =
        runTest {
            val repository = FakeTourDiscoveryRepository()
            val viewModel = TouristGuideToursViewModel(repository)

            viewModel.loadGuideTours(GUIDE_ID)
            runCurrent()

            assertEquals(ContentLoadState.CONTENT, viewModel.uiState.value.loadState)
            assertTrue(viewModel.uiState.value.tours.isEmpty())
            assertFalse(viewModel.uiState.value.canLoadMore)
        }

    private companion object {
        const val GUIDE_ID = 42L
    }
}
