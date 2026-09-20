package com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage

import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.testing.tour.FakeGuideTourRepository
import com.ahmetkaragunlu.guidemate.testing.tour.GuideTourListCall
import com.ahmetkaragunlu.guidemate.testing.common.FakeResourceProvider
import com.ahmetkaragunlu.guidemate.testing.tour.testGuideTourCard
import com.ahmetkaragunlu.guidemate.tour.domain.model.guide.GuideTourListType
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.model.GuideTourTab
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
class GuideMyToursViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun loadMoreAppendsNextPageWithoutReplacingExistingTours() =
        runTest {
            val repository =
                FakeGuideTourRepository().apply {
                    results.lists +=
                        DataResult.Success(
                            guideTourPage(
                                page = 0,
                                isLast = false,
                                testGuideTourCard("tour-1", "session-1"),
                            )
                        )
                    results.lists +=
                        DataResult.Success(
                            guideTourPage(
                                page = 1,
                                isLast = true,
                                testGuideTourCard("tour-2", "session-2"),
                            )
                        )
                }
            val viewModel =
                GuideMyToursViewModel(
                    repository = repository,
                    resourceProvider = FakeResourceProvider(),
                )
            runCurrent()

            assertEquals(1, viewModel.uiState.value.tours.size)
            assertTrue(viewModel.uiState.value.canLoadMore)

            viewModel.loadMore()
            runCurrent()

            assertEquals(listOf("session-1", "session-2"), viewModel.uiState.value.tours.map { it.id })
            assertFalse(viewModel.uiState.value.canLoadMore)
            assertEquals(
                listOf(
                    GuideTourListCall(GuideTourListType.ACTIVE, 0),
                    GuideTourListCall(GuideTourListType.ACTIVE, 1),
                ),
                repository.calls.listRequests,
            )
        }

    @Test
    fun navigationResultSelectsRequestedTabAndRefreshesItsFirstPage() =
        runTest {
            val repository =
                FakeGuideTourRepository().apply {
                    results.lists +=
                        DataResult.Success(
                            guideTourPage(page = 0, isLast = true)
                        )
                    results.lists +=
                        DataResult.Success(
                            guideTourPage(
                                page = 0,
                                isLast = true,
                                testGuideTourCard("tour-review", "session-review"),
                            )
                        )
                }
            val viewModel =
                GuideMyToursViewModel(
                    repository = repository,
                    resourceProvider = FakeResourceProvider(),
                )
            runCurrent()

            viewModel.applyNavigationResult(GuideTourTab.REVIEW)
            runCurrent()

            assertEquals(GuideTourTab.REVIEW, viewModel.uiState.value.selectedTab)
            assertEquals(listOf("session-review"), viewModel.uiState.value.tours.map { it.id })
            assertEquals(
                listOf(
                    GuideTourListCall(GuideTourListType.ACTIVE, 0),
                    GuideTourListCall(GuideTourListType.REVIEW, 0),
                ),
                repository.calls.listRequests,
            )
        }

    private fun guideTourPage(
        page: Int,
        isLast: Boolean,
        vararg items: com.ahmetkaragunlu.guidemate.tour.domain.model.guide.GuideTourCard,
    ) =
        PagedResult(
            items = items.toList(),
            page = page,
            size = 20,
            totalElements = 2,
            totalPages = 2,
            isFirst = page == 0,
            isLast = isLast,
        )
}
