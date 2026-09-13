package com.ahmetkaragunlu.guidemate.tour.domain.usecase

import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaPurpose
import com.ahmetkaragunlu.guidemate.testing.FakeGuideTourRepository
import com.ahmetkaragunlu.guidemate.testing.FakeMediaRepository
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.CreateGuideTourInput
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.TourContentInput
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.TourSessionInput
import java.time.Instant
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PublishGuideTourUseCaseTest {
    @Test
    fun `successful publish attaches uploaded cover and keeps media`() = runTest {
        val tourRepository = FakeGuideTourRepository()
        val mediaRepository = FakeMediaRepository()
        val useCase = PublishGuideTourUseCase(tourRepository, mediaRepository)

        val result = useCase("content://cover", validInput())

        assertTrue(result is DataResult.Success)
        assertEquals(MediaPurpose.TOUR_COVER, mediaRepository.uploadedPurpose)
        assertEquals("media-1", tourRepository.createInput?.content?.coverMediaId)
        assertTrue(mediaRepository.deletedMediaIds.isEmpty())
    }

    @Test
    fun `failed publish deletes uploaded cover`() = runTest {
        val tourRepository =
            FakeGuideTourRepository().apply {
                createResult = DataResult.Error(AppError.GenericFailure)
            }
        val mediaRepository = FakeMediaRepository()
        val useCase = PublishGuideTourUseCase(tourRepository, mediaRepository)

        assertTrue(useCase("content://cover", validInput()) is DataResult.Error)
        assertEquals(listOf("media-1"), mediaRepository.deletedMediaIds)
    }

    @Test
    fun `failed upload does not create tour`() = runTest {
        val tourRepository = FakeGuideTourRepository()
        val mediaRepository =
            FakeMediaRepository().apply {
                uploadResult = DataResult.Error(AppError.ImageUnavailable)
            }
        val useCase = PublishGuideTourUseCase(tourRepository, mediaRepository)

        assertTrue(useCase("content://cover", validInput()) is DataResult.Error)
        assertNull(tourRepository.createInput)
    }

    private fun validInput() =
        CreateGuideTourInput(
            content =
                TourContentInput(
                    title = "City Walk",
                    description = "Historic route through the old city",
                    countryCode = "TR",
                    cityPlaceId = "istanbul-place-id",
                    cityName = "Istanbul",
                    timeZoneId = "UTC",
                    category = TourCategory.CULTURE,
                    languageCodes = listOf("en"),
                    coverMediaId = "",
                ),
            session =
                TourSessionInput(
                    meetingPoint = "Main square",
                    startsAt = Instant.parse("2099-01-01T12:00:00Z"),
                    durationMinutes = 120,
                    priceMinor = 10_000,
                    capacity = 10,
                ),
        )
}
