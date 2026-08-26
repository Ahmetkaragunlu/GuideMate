package com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.edit

import androidx.lifecycle.SavedStateHandle
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.testing.FakeGuideTourRepository
import com.ahmetkaragunlu.guidemate.testing.FakeMediaRepository
import com.ahmetkaragunlu.guidemate.testing.FakeResourceProvider
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.model.GuideTourTab
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class GuideTourEditViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun unchangedTourIsNotSubmitted() =
        runTest {
            val repository = FakeGuideTourRepository()
            val viewModel = createViewModel(repository)
            runCurrent()

            assertEquals(ContentLoadState.CONTENT, viewModel.uiState.value.loadState)
            viewModel.saveChanges()

            assertEquals(R.string.error_tour_edit_no_changes, viewModel.uiState.value.errorResId)
            assertNull(repository.submitChangeInput)
            assertNull(repository.updateSessionInput)
        }

    @Test
    fun contentOnlyChangeSubmitsReviewWithoutUpdatingSession() =
        runTest {
            val repository = FakeGuideTourRepository()
            val viewModel = createViewModel(repository)
            runCurrent()

            viewModel.onTitleChange("Updated city walk")
            assertTrue(viewModel.uiState.value.hasUnsavedChanges)
            assertTrue(viewModel.uiState.value.requiresReviewConfirmation)

            viewModel.saveChanges()
            runCurrent()

            assertEquals("Updated city walk", repository.submitChangeInput?.content?.title)
            assertNull(repository.updateSessionInput)
            assertEquals(GuideTourTab.REVIEW, viewModel.uiState.value.savedTargetTab)
            assertFalse(viewModel.uiState.value.hasUnsavedChanges)
        }

    private fun createViewModel(repository: FakeGuideTourRepository) =
        GuideTourEditViewModel(
            savedStateHandle =
                SavedStateHandle(
                    mapOf(
                        "tourId" to "tour-1",
                        "sessionId" to "session-1",
                    )
                ),
            repository = repository,
            mediaRepository = FakeMediaRepository(),
            resourceProvider = FakeResourceProvider(),
        )
}
