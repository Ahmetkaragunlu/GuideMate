package com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish

import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.location.model.CityOption
import com.ahmetkaragunlu.guidemate.common.location.model.CountryOption
import com.ahmetkaragunlu.guidemate.common.location.model.LanguageOption
import com.ahmetkaragunlu.guidemate.common.location.model.LocationOption
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.AppFieldError
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.testing.FakeGuideProfileRepository
import com.ahmetkaragunlu.guidemate.testing.FakeGuideTourRepository
import com.ahmetkaragunlu.guidemate.testing.FakeMediaRepository
import com.ahmetkaragunlu.guidemate.testing.FakeResourceProvider
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish.model.GuideTourPublishStep
import java.time.LocalDate
import java.time.LocalTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GuideTourPublishViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun incompleteFirstStepExposesItsValidationTarget() =
        runTest {
            val viewModel = createViewModel()
            val collection = backgroundScope.launch { viewModel.uiState.collect {} }
            runCurrent()

            assertFalse(viewModel.validateStep1())
            runCurrent()
            assertEquals(
                GuideTourPublishStep.LOCATION_AND_TIME,
                viewModel.uiState.value.validationErrorStep,
            )
            collection.cancel()
        }

    @Test
    fun validDraftUploadsCoverAndCreatesTrimmedTourInput() =
        runTest {
            val tourRepository = FakeGuideTourRepository()
            val mediaRepository = FakeMediaRepository()
            val viewModel = createViewModel(tourRepository, mediaRepository)
            val collection = backgroundScope.launch { viewModel.uiState.collect {} }
            runCurrent()

            viewModel.fillValidDraft()

            viewModel.onPublishClick()
            runCurrent()

            assertEquals("content://cover", mediaRepository.uploadedUri)
            assertNotNull(tourRepository.createInput)
            assertEquals("City Walk", tourRepository.createInput?.content?.title)
            assertEquals("Main square", tourRepository.createInput?.session?.meetingPoint)
            assertEquals(10_000L, tourRepository.createInput?.session?.priceMinor)
            assertTrue(viewModel.uiState.value.publishSucceeded)
            assertFalse(viewModel.uiState.value.isPublishing)
            collection.cancel()
        }

    @Test
    fun shortTitleStopsPublishOnContentStep() =
        runTest {
            val viewModel = createViewModel()
            val collection = backgroundScope.launch { viewModel.uiState.collect {} }
            runCurrent()

            viewModel.onTourNameChange("AB")

            assertFalse(viewModel.validateStep3())
            runCurrent()
            assertEquals(
                GuideTourPublishStep.CONTENT_AND_MEDIA,
                viewModel.uiState.value.validationErrorStep,
            )
            assertEquals(R.string.error_tour_title_length, viewModel.uiState.value.validationErrorResId)
            collection.cancel()
        }

    @Test
    fun backendFieldErrorReturnsUserToItsPublishStep() =
        runTest {
            val tourRepository =
                FakeGuideTourRepository().apply {
                    createResult =
                        DataResult.Error(
                            AppError.Backend(
                                code = null,
                                fallbackMessage = null,
                                fieldErrors =
                                    listOf(
                                        AppFieldError(
                                            field = "content.description",
                                            code = "INVALID_SIZE",
                                            fallbackMessage = null,
                                        )
                                    ),
                            )
                        )
                }
            val viewModel = createViewModel(tourRepository = tourRepository)
            val collection = backgroundScope.launch { viewModel.uiState.collect {} }
            runCurrent()
            viewModel.fillValidDraft()

            viewModel.onPublishClick()
            runCurrent()

            assertEquals(
                GuideTourPublishStep.CONTENT_AND_MEDIA,
                viewModel.uiState.value.validationErrorStep,
            )
            assertEquals(
                R.string.error_tour_description_length,
                viewModel.uiState.value.validationErrorResId,
            )
            assertFalse(viewModel.uiState.value.isPublishing)
            collection.cancel()
        }

    private fun GuideTourPublishViewModel.fillValidDraft() {
        onLocationSelected(
            LocationOption(
                country = CountryOption("TR", "Turkiye"),
                city = CityOption("istanbul-place-id", "Istanbul", "TR"),
            )
        )
        onTourDateSelected(LocalDate.of(2099, 1, 1))
        onStartTimeSelected(LocalTime.of(12, 0))
        onDurationSelected(120)
        onCategorySelected(TourCategory.CULTURE)
        onLanguagesSelected(listOf(LanguageOption("en", "English", "")))
        onPriceChange("100")
        onCapacityChange("10")
        onTourNameChange("  City Walk  ")
        onTourDescriptionChange("  Historic route through the old city  ")
        onMeetingPointChange("  Main square  ")
        onCoverImageSelected("content://cover")
    }

    private fun createViewModel(
        tourRepository: FakeGuideTourRepository = FakeGuideTourRepository(),
        mediaRepository: FakeMediaRepository = FakeMediaRepository(),
    ) =
        GuideTourPublishViewModel(
            repository = tourRepository,
            mediaRepository = mediaRepository,
            profileRepository = FakeGuideProfileRepository(),
            resourceProvider = FakeResourceProvider(),
        )
}
