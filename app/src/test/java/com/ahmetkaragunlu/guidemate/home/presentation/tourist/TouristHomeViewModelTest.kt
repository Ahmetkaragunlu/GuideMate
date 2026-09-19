package com.ahmetkaragunlu.guidemate.home.presentation.tourist

import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.testing.FakeGuideProfileRepository
import com.ahmetkaragunlu.guidemate.testing.FakeReviewRepository
import com.ahmetkaragunlu.guidemate.testing.FakeTourDiscoveryRepository
import com.ahmetkaragunlu.guidemate.testing.FakeUserRepository
import com.ahmetkaragunlu.guidemate.testing.testGuideSearchResult
import com.ahmetkaragunlu.guidemate.testing.testTourSearchItem
import com.ahmetkaragunlu.guidemate.testing.tourSearchPage
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import com.ahmetkaragunlu.guidemate.tour.domain.model.discovery.TourSearchSort
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TouristHomeViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun initialLoadPublishesPopularToursAndTopGuides() =
        runTest {
            val tourRepository =
                FakeTourDiscoveryRepository().apply {
                    popularResult =
                        DataResult.Success(
                            tourSearchPage(
                                page = 0,
                                isLast = true,
                                testTourSearchItem("tour-1", "session-1"),
                            )
                        )
                }
            val profileRepository =
                FakeGuideProfileRepository().apply {
                    topGuidesResult = DataResult.Success(listOf(testGuideSearchResult()))
                }

            val viewModel =
                createViewModel(
                    tourRepository = tourRepository,
                    profileRepository = profileRepository,
                )
            runCurrent()

            assertEquals(ContentLoadState.CONTENT, viewModel.uiState.value.popularToursLoadState)
            assertEquals("session-1", viewModel.uiState.value.popularTours.single().id)
            assertEquals(ContentLoadState.CONTENT, viewModel.uiState.value.bestGuidesLoadState)
            assertEquals(7L, viewModel.uiState.value.bestGuides.single().guideId)
            assertEquals(listOf(0 to 10), tourRepository.popularRequests)
            assertEquals(listOf(4), profileRepository.topGuideLimits)
        }

    @Test
    fun categorySelectionUsesRatingSortedBackendSearch() =
        runTest {
            val tourRepository = FakeTourDiscoveryRepository()
            val viewModel = createViewModel(tourRepository = tourRepository)
            runCurrent()
            tourRepository.searchResults +=
                DataResult.Success(tourSearchPage(page = 0, isLast = true))

            viewModel.updateSelectedCategory(TourCategory.CULTURE)
            runCurrent()

            val request = tourRepository.searchRequests.single()
            assertEquals(TourCategory.CULTURE.code, request.query.categoryCode)
            assertEquals(TourSearchSort.RATING_DESC, request.query.sort)
            assertEquals(0, request.page)
            assertEquals(10, request.size)
        }

    @Test
    fun initialErrorsExposeIndependentSectionErrorStates() =
        runTest {
            val tourRepository =
                FakeTourDiscoveryRepository().apply {
                    popularResult = DataResult.Error(AppError.NoInternet)
                }
            val profileRepository =
                FakeGuideProfileRepository().apply {
                    topGuidesResult = DataResult.Error(AppError.NoInternet)
                }

            val viewModel =
                createViewModel(
                    tourRepository = tourRepository,
                    profileRepository = profileRepository,
                )
            runCurrent()

            assertEquals(ContentLoadState.ERROR, viewModel.uiState.value.popularToursLoadState)
            assertEquals(ContentLoadState.ERROR, viewModel.uiState.value.bestGuidesLoadState)
            assertTrue(viewModel.uiState.value.popularTours.isEmpty())
            assertTrue(viewModel.uiState.value.bestGuides.isEmpty())
        }

    @Test
    fun reviewChangeRefreshesPopularToursAndTopGuides() =
        runTest {
            val tourRepository = FakeTourDiscoveryRepository()
            val profileRepository = FakeGuideProfileRepository()
            val reviewRepository = FakeReviewRepository()
            createViewModel(
                tourRepository = tourRepository,
                profileRepository = profileRepository,
                reviewRepository = reviewRepository,
            )
            runCurrent()

            reviewRepository.publishReviewChange()
            runCurrent()

            assertEquals(2, tourRepository.popularRequests.size)
            assertEquals(2, profileRepository.topGuideLimits.size)
        }

    private fun createViewModel(
        tourRepository: FakeTourDiscoveryRepository = FakeTourDiscoveryRepository(),
        profileRepository: FakeGuideProfileRepository = FakeGuideProfileRepository(),
        reviewRepository: FakeReviewRepository = FakeReviewRepository(),
    ): TouristHomeViewModel =
        TouristHomeViewModel(
            userRepository = FakeUserRepository(),
            tourRepository = tourRepository,
            profileRepository = profileRepository,
            reviewRepository = reviewRepository,
        )
}
