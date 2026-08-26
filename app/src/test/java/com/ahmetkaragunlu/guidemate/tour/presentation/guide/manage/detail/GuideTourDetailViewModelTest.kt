package com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.detail

import androidx.lifecycle.SavedStateHandle
import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.testing.FakeGuideTourRepository
import com.ahmetkaragunlu.guidemate.testing.FakeResourceProvider
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.model.GuideTourTab
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class GuideTourDetailViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun cancellationRequiresReasonThenUsesIdempotentRequestAndFinishesInPast() =
        runTest {
            val repository = FakeGuideTourRepository()
            val viewModel = createViewModel(repository)
            runCurrent()

            assertEquals(ContentLoadState.CONTENT, viewModel.uiState.value.loadState)
            viewModel.cancelSession()
            assertNull(repository.cancelSessionRequest)

            viewModel.onCancellationReasonChange("  Weather conditions  ")
            viewModel.cancelSession()
            runCurrent()

            assertEquals("session-1", repository.cancelSessionRequest?.first)
            assertEquals("Weather conditions", repository.cancelSessionRequest?.second)
            assertNotNull(repository.cancelSessionRequest?.third)
            assertEquals(GuideTourTab.PAST, viewModel.uiState.value.finishedTab)
        }

    private fun createViewModel(repository: FakeGuideTourRepository) =
        GuideTourDetailViewModel(
            savedStateHandle =
                SavedStateHandle(
                    mapOf(
                        "tourId" to "tour-1",
                        "sessionId" to "session-1",
                    )
                ),
            repository = repository,
            resourceProvider = FakeResourceProvider(),
        )
}
