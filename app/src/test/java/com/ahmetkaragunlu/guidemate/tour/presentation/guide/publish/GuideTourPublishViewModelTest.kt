package com.ahmetkaragunlu.guidemate.tour.presentation.guide.publish

import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.location.model.CityOption
import com.ahmetkaragunlu.guidemate.common.location.model.CountryOption
import com.ahmetkaragunlu.guidemate.common.location.model.LanguageOption
import com.ahmetkaragunlu.guidemate.common.location.model.LocationOption
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

            viewModel.onLocationSelected(
                LocationOption(
                    country = CountryOption("TR", "Turkiye"),
                    city = CityOption("istanbul-place-id", "Istanbul", "TR"),
                )
            )
            viewModel.onTourDateSelected(LocalDate.of(2099, 1, 1))
            viewModel.onStartTimeSelected(LocalTime.of(12, 0))
            viewModel.onDurationSelected(120)
            viewModel.onCategorySelected(TourCategory.CULTURE)
            viewModel.onLanguagesSelected(listOf(LanguageOption("en", "English", "")))
            viewModel.onPriceChange("100")
            viewModel.onCapacityChange("10")
            viewModel.onTourNameChange("  City Walk  ")
            viewModel.onTourDescriptionChange("  Historic route  ")
            viewModel.onMeetingPointChange("  Main square  ")
            viewModel.onCoverImageSelected("content://cover")

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
