package com.ahmetkaragunlu.guidemate.discovery.presentation.tourist

import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.location.model.CountryOption
import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.review.domain.model.ReviewSubmissionInput
import com.ahmetkaragunlu.guidemate.review.domain.model.SubmittedReview
import com.ahmetkaragunlu.guidemate.review.domain.repository.ReviewRepository
import com.ahmetkaragunlu.guidemate.testing.FakeGuideProfileRepository
import com.ahmetkaragunlu.guidemate.testing.FakeTourDiscoveryRepository
import com.ahmetkaragunlu.guidemate.testing.testTourSearchItem
import com.ahmetkaragunlu.guidemate.testing.tourSearchPage
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourReview
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.advanceTimeBy
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
class TouristExploreViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun appliedFiltersBuildBackendQueryAndLoadMoreAppendsResults() =
        runTest {
            val repository =
                FakeTourDiscoveryRepository().apply {
                    searchResults +=
                        DataResult.Success(
                            tourSearchPage(
                                page = 0,
                                isLast = false,
                                testTourSearchItem("tour-1", "session-1"),
                            )
                        )
                    searchResults +=
                        DataResult.Success(
                            tourSearchPage(
                                page = 1,
                                isLast = true,
                                testTourSearchItem("tour-2", "session-2"),
                            )
                        )
                }
            val viewModel =
                TouristExploreViewModel(
                    tourRepository = repository,
                    profileRepository = FakeGuideProfileRepository(),
                    reviewRepository = FakeReviewRepository(),
                )
            runCurrent()

            viewModel.updateToursSearchQuery("museum")
            viewModel.updateSelectedCountry(CountryOption("TR", "Turkiye"))
            viewModel.updateSelectedCategory(TourCategory.CULTURE)
            viewModel.applyFilters()
            advanceTimeBy(351)
            runCurrent()

            assertEquals("museum", repository.searchRequests.single().query.text)
            assertEquals("TR", repository.searchRequests.single().query.countryCode)
            assertEquals("culture", repository.searchRequests.single().query.categoryCode)
            assertEquals(1, viewModel.uiState.value.tours.results.size)
            assertTrue(viewModel.uiState.value.tours.canLoadMore)

            viewModel.loadMoreTours()
            runCurrent()

            assertEquals(listOf(0, 1), repository.searchRequests.map { it.page })
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
                        searchResults +=
                            DataResult.Success(tourSearchPage(page = 0, isLast = true))
                    }
                }
            val viewModel =
                TouristExploreViewModel(
                    tourRepository = repository,
                    profileRepository = FakeGuideProfileRepository(),
                    reviewRepository = FakeReviewRepository(),
                )
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

            val clearedRequest = repository.searchRequests.last()
            assertEquals("", viewModel.uiState.value.tours.searchQuery)
            assertEquals(null, viewModel.uiState.value.draftFilters.selectedCountry)
            assertEquals(null, viewModel.uiState.value.appliedFilters.selectedCountry)
            assertEquals("", clearedRequest.query.text)
            assertEquals(null, clearedRequest.query.countryCode)
            assertEquals(null, clearedRequest.query.categoryCode)
            assertEquals(0, clearedRequest.page)
        }

    private fun createViewModel(): TouristExploreViewModel =
        TouristExploreViewModel(
            tourRepository =
                FakeTourDiscoveryRepository().apply {
                    repeat(2) {
                        searchResults +=
                            DataResult.Success(tourSearchPage(page = 0, isLast = true))
                    }
                },
            profileRepository = FakeGuideProfileRepository(),
            reviewRepository = FakeReviewRepository(),
        )

    private class FakeReviewRepository : ReviewRepository {
        override val reviewChanges: Flow<Unit> = MutableSharedFlow()

        override suspend fun submitReview(
            reservationId: String,
            input: ReviewSubmissionInput,
        ): DataResult<SubmittedReview> = error("Not used")

        override suspend fun getTourReviews(
            tourId: String,
            page: Int,
            size: Int,
        ): DataResult<PagedResult<TourReview>> = error("Not used")
    }
}
