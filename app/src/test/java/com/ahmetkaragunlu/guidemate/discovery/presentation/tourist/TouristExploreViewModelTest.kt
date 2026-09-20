package com.ahmetkaragunlu.guidemate.discovery.presentation.tourist

import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.location.model.CountryOption
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.testing.profile.FakeGuideProfileRepository
import com.ahmetkaragunlu.guidemate.testing.review.FakeReviewRepository
import com.ahmetkaragunlu.guidemate.testing.discovery.FakeTourDiscoveryRepository
import com.ahmetkaragunlu.guidemate.testing.discovery.testTourSearchItem
import com.ahmetkaragunlu.guidemate.testing.discovery.tourSearchPage
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class TouristExploreViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun appliedFiltersBuildBackendQueryAndLoadMoreAppendsResults() =
        runTest {
            val repository =
                FakeTourDiscoveryRepository().apply {
                    results.searchResults +=
                        DataResult.Success(
                            tourSearchPage(
                                page = 0,
                                isLast = false,
                                testTourSearchItem("tour-1", "session-1"),
                            )
                        )
                    results.searchResults +=
                        DataResult.Success(
                            tourSearchPage(
                                page = 1,
                                isLast = true,
                                testTourSearchItem("tour-2", "session-2"),
                            )
                        )
                }
            val viewModel = createViewModel(tourRepository = repository)
            runCurrent()

            viewModel.updateToursSearchQuery("museum")
            viewModel.updateSelectedCountry(CountryOption("TR", "Turkiye"))
            viewModel.updateSelectedCategory(TourCategory.CULTURE)
            viewModel.applyFilters()
            advanceTimeBy(351)
            runCurrent()

            assertEquals("museum", repository.calls.searchRequests.single().query.text)
            assertEquals("TR", repository.calls.searchRequests.single().query.countryCode)
            assertEquals("culture", repository.calls.searchRequests.single().query.categoryCode)
            assertEquals(1, viewModel.uiState.value.tours.results.size)
            assertTrue(viewModel.uiState.value.tours.canLoadMore)

            viewModel.loadMoreTours()
            runCurrent()

            assertEquals(listOf(0, 1), repository.calls.searchRequests.map { it.page })
            assertEquals(2, viewModel.uiState.value.tours.results.size)
            assertFalse(viewModel.uiState.value.tours.canLoadMore)
        }

    @Test
    fun filterEditing_canBeCancelledOrAppliedWithoutLosingCanonicalFilters() =
        runTest {
            val viewModel = createViewModel()
            runCurrent()
            val turkey = CountryOption("TR", "Turkiye")

            viewModel.beginFilterEditing()
            viewModel.updateSelectedCountry(turkey)
            viewModel.cancelFilterEditing()

            assertEquals(null, viewModel.uiState.value.draftFilters.selectedCountry)
            assertEquals(null, viewModel.uiState.value.appliedFilters.selectedCountry)

            viewModel.beginFilterEditing()
            viewModel.updateSelectedCountry(turkey)
            viewModel.applyFilters()
            viewModel.beginFilterEditing()
            viewModel.updateSelectedCategory(TourCategory.CULTURE)
            viewModel.cancelFilterEditing()

            assertEquals(turkey, viewModel.uiState.value.appliedFilters.selectedCountry)
            assertEquals(turkey, viewModel.uiState.value.draftFilters.selectedCountry)
            assertEquals(null, viewModel.uiState.value.draftFilters.selectedCategory)
        }

    @Test
    fun clearSearchAndFilters_resetsStateAndReloadsFirstPage() =
        runTest {
            val repository =
                FakeTourDiscoveryRepository().apply {
                    repeat(3) {
                        results.searchResults +=
                            DataResult.Success(tourSearchPage(page = 0, isLast = true))
                    }
                }
            val viewModel = createViewModel(tourRepository = repository)
            advanceTimeBy(351)
            runCurrent()

            viewModel.updateToursSearchQuery("museum")
            viewModel.beginFilterEditing()
            viewModel.updateSelectedCountry(CountryOption("TR", "Turkiye"))
            viewModel.updateSelectedCategory(TourCategory.CULTURE)
            viewModel.applyFilters()
            advanceTimeBy(351)
            runCurrent()

            viewModel.clearSearchAndFilters()
            advanceTimeBy(351)
            runCurrent()

            val clearedRequest = repository.calls.searchRequests.last()
            assertEquals("", viewModel.uiState.value.tours.searchQuery)
            assertEquals(null, viewModel.uiState.value.draftFilters.selectedCountry)
            assertEquals(null, viewModel.uiState.value.appliedFilters.selectedCountry)
            assertEquals("", clearedRequest.query.text)
            assertEquals(null, clearedRequest.query.countryCode)
            assertEquals(null, clearedRequest.query.categoryCode)
            assertEquals(0, clearedRequest.page)
        }

    @Test
    fun newSearchClearsOldResultsAndIgnoresDelayedPreviousResponse() =
        runTest {
            val oldRequestStarted = CompletableDeferred<Unit>()
            val releaseOldRequest = CompletableDeferred<Unit>()
            val repository =
                FakeTourDiscoveryRepository().apply {
                    handlers.search = { query, _, _ ->
                        when (query.text) {
                            "old" -> {
                                oldRequestStarted.complete(Unit)
                                withContext(NonCancellable) { releaseOldRequest.await() }
                                DataResult.Success(
                                    tourSearchPage(
                                        page = 0,
                                        isLast = true,
                                        testTourSearchItem("old-tour", "old-session"),
                                    )
                                )
                            }
                            "new" ->
                                DataResult.Success(
                                    tourSearchPage(
                                        page = 0,
                                        isLast = true,
                                        testTourSearchItem("new-tour", "new-session"),
                                    )
                                )
                            else -> DataResult.Success(tourSearchPage(page = 0, isLast = true))
                        }
                    }
                }
            val viewModel = createViewModel(tourRepository = repository)

            viewModel.updateToursSearchQuery("old")
            advanceTimeBy(351)
            runCurrent()
            oldRequestStarted.await()

            viewModel.updateToursSearchQuery("new")
            advanceTimeBy(351)
            runCurrent()

            assertEquals(
                listOf("new-session"),
                viewModel.uiState.value.tours.results.map { it.sessionId },
            )

            releaseOldRequest.complete(Unit)
            runCurrent()

            assertEquals(
                listOf("new-session"),
                viewModel.uiState.value.tours.results.map { it.sessionId },
            )
        }

    @Test
    fun failedNewSearchShowsErrorInsteadOfPreviousResults() =
        runTest {
            val repository =
                FakeTourDiscoveryRepository().apply {
                    results.searchResults +=
                        DataResult.Success(
                            tourSearchPage(
                                page = 0,
                                isLast = true,
                                testTourSearchItem("tour-1", "session-1"),
                            )
                        )
                    results.searchResults += DataResult.Error(AppError.NoInternet)
                    results.searchResults +=
                        DataResult.Success(
                            tourSearchPage(
                                page = 0,
                                isLast = true,
                                testTourSearchItem("tour-2", "session-2"),
                            )
                        )
                }
            val viewModel = createViewModel(tourRepository = repository)
            advanceTimeBy(351)
            runCurrent()
            assertEquals(1, viewModel.uiState.value.tours.results.size)

            viewModel.updateToursSearchQuery("offline")
            advanceTimeBy(351)
            runCurrent()

            assertTrue(viewModel.uiState.value.tours.results.isEmpty())
            assertEquals(ContentLoadState.ERROR, viewModel.uiState.value.tours.loadState)

            viewModel.refreshTours()
            runCurrent()

            assertEquals("offline", repository.calls.searchRequests.last().query.text)
            assertEquals("session-2", viewModel.uiState.value.tours.results.single().sessionId)
        }

    @Test
    fun loadMoreFailureKeepsPreviouslyLoadedResults() =
        runTest {
            val repository =
                FakeTourDiscoveryRepository().apply {
                    results.searchResults +=
                        DataResult.Success(
                            tourSearchPage(
                                page = 0,
                                isLast = false,
                                testTourSearchItem("tour-1", "session-1"),
                            )
                        )
                    results.searchResults += DataResult.Error(AppError.NoInternet)
                }
            val viewModel = createViewModel(tourRepository = repository)
            advanceTimeBy(351)
            runCurrent()

            viewModel.loadMoreTours()
            runCurrent()

            assertEquals(listOf("session-1"), viewModel.uiState.value.tours.results.map { it.sessionId })
            assertEquals(ContentLoadState.CONTENT, viewModel.uiState.value.tours.loadState)
            assertTrue(viewModel.uiState.value.tours.appendFailed)
            assertTrue(viewModel.uiState.value.tours.canLoadMore)
        }

    private fun createViewModel(
        tourRepository: FakeTourDiscoveryRepository = defaultTourRepository(),
    ): TouristExploreViewModel =
        TouristExploreViewModel(
            tourRepository = tourRepository,
            profileRepository = FakeGuideProfileRepository(),
            reviewRepository = FakeReviewRepository(),
        )

    private fun defaultTourRepository(): FakeTourDiscoveryRepository =
        FakeTourDiscoveryRepository().apply {
            repeat(2) {
                results.searchResults += DataResult.Success(tourSearchPage(page = 0, isLast = true))
            }
        }
}
