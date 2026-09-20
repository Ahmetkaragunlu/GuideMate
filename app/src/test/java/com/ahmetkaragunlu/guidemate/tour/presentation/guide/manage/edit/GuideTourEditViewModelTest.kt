package com.ahmetkaragunlu.guidemate.tour.presentation.guide.manage.edit

import androidx.lifecycle.SavedStateHandle
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.state.ContentLoadState
import com.ahmetkaragunlu.guidemate.testing.tour.FakeGuideTourRepository
import com.ahmetkaragunlu.guidemate.testing.media.FakeMediaRepository
import com.ahmetkaragunlu.guidemate.testing.common.FakeResourceProvider
import com.ahmetkaragunlu.guidemate.testing.tour.testTourDetails
import com.ahmetkaragunlu.guidemate.tour.domain.usecase.SubmitGuideTourContentChangeUseCase
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

            assertEquals(ContentLoadState.CONTENT, viewModel.uiState.value.operation.loadState)
            viewModel.saveChanges()

            assertEquals(
                R.string.error_tour_edit_no_changes,
                viewModel.uiState.value.operation.errorResId,
            )
            assertNull(repository.calls.submitChange)
            assertNull(repository.calls.updateSession)
        }

    @Test
    fun contentOnlyChangeSubmitsReviewWithoutUpdatingSession() =
        runTest {
            val repository = FakeGuideTourRepository()
            val viewModel = createViewModel(repository)
            runCurrent()

            viewModel.onTitleChange("Updated city walk")
            assertTrue(viewModel.uiState.value.operation.hasUnsavedChanges)
            assertTrue(viewModel.uiState.value.operation.requiresReviewConfirmation)

            viewModel.saveChanges()
            runCurrent()

            assertEquals("Updated city walk", repository.calls.submitChange?.content?.title)
            assertNull(repository.calls.updateSession)
            assertEquals(GuideTourTab.REVIEW, viewModel.uiState.value.operation.savedTargetTab)
            assertFalse(viewModel.uiState.value.operation.hasUnsavedChanges)
        }

    @Test
    fun sessionRetryAfterPartialSuccessDoesNotResubmitContent() =
        runTest {
            val repository =
                FakeGuideTourRepository().apply {
                    results.updateSession = DataResult.Error(AppError.NoInternet)
                }
            val viewModel = createViewModel(repository)
            runCurrent()

            viewModel.onTitleChange("Updated city walk")
            viewModel.onMeetingPointChange("Updated meeting point")
            viewModel.saveChanges()
            runCurrent()

            assertEquals(1, repository.calls.submitChangeInputs.size)
            assertEquals(1, repository.calls.updateSessionInputs.size)
            assertTrue(viewModel.uiState.value.operation.contentReviewSubmitted)
            assertFalse(viewModel.uiState.value.operation.isSaving)

            repository.results.updateSession =
                DataResult.Success(testTourDetails().sessions.first().copy(version = 6))
            viewModel.saveChanges()
            runCurrent()

            assertEquals(1, repository.calls.submitChangeInputs.size)
            assertEquals(2, repository.calls.updateSessionInputs.size)
            assertEquals(GuideTourTab.REVIEW, viewModel.uiState.value.operation.savedTargetTab)
            assertFalse(viewModel.uiState.value.operation.hasUnsavedChanges)
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
            submitTourContentChange =
                SubmitGuideTourContentChangeUseCase(
                    tourRepository = repository,
                    mediaRepository = FakeMediaRepository(),
                ),
            resourceProvider = FakeResourceProvider(),
        )
}
