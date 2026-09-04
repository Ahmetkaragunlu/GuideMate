package com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage

import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.testing.FakeGuideTourRepository
import com.ahmetkaragunlu.guidemate.testing.FakeResourceProvider
import com.ahmetkaragunlu.guidemate.testing.testGuideTourCard
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
                    listResults +=
                        DataResult.Success(
                            guideTourPage(
                                page = 0,
                                isLast = false,
                                testGuideTourCard("tour-1", "session-1"),
                            )
                        )
                    listResults +=
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
                listOf(GuideTourListType.ACTIVE to 0, GuideTourListType.ACTIVE to 1),
                repository.listRequests,
            )
        }

    @Test
    fun navigationResultSelectsRequestedTabAndRefreshesItsFirstPage() =
        runTest {
            val repository =
                FakeGuideTourRepository().apply {
                    listResults +=
                        DataResult.Success(
                            guideTourPage(page = 0, isLast = true)
                        )
                    listResults +=
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
                listOf(GuideTourListType.ACTIVE to 0, GuideTourListType.REVIEW to 0),
                repository.listRequests,
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
