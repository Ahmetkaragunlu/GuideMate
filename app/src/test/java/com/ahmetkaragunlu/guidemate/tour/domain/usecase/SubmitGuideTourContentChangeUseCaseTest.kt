package com.ahmetkaragunlu.guidemate.tour.domain.usecase

import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaPurpose
import com.ahmetkaragunlu.guidemate.testing.FakeGuideTourRepository
import com.ahmetkaragunlu.guidemate.testing.FakeMediaRepository
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.TourContentInput
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SubmitGuideTourContentChangeUseCaseTest {
    @Test
    fun `new cover is attached and kept when change succeeds`() = runTest {
        val tourRepository = FakeGuideTourRepository()
        val mediaRepository = FakeMediaRepository()
        val useCase = SubmitGuideTourContentChangeUseCase(tourRepository, mediaRepository)

        val result =
            useCase(
                tourId = "tour-1",
                baseVersion = 3,
                content = validContent(),
                newCoverImageUri = "content://cover",
            )

        assertTrue(result is DataResult.Success)
        assertEquals(MediaPurpose.TOUR_COVER, mediaRepository.uploadedPurpose)
        assertEquals("media-1", tourRepository.submitChangeInput?.content?.coverMediaId)
        assertTrue(mediaRepository.deletedMediaIds.isEmpty())
    }

    @Test
    fun `existing cover is submitted without uploading new media`() = runTest {
        val tourRepository = FakeGuideTourRepository()
        val mediaRepository = FakeMediaRepository()
        val useCase = SubmitGuideTourContentChangeUseCase(tourRepository, mediaRepository)

        val result =
            useCase(
                tourId = "tour-1",
                baseVersion = 3,
                content = validContent(),
                newCoverImageUri = null,
            )

        assertTrue(result is DataResult.Success)
        assertNull(mediaRepository.uploadedUri)
        assertEquals("old-media", tourRepository.submitChangeInput?.content?.coverMediaId)
    }

    @Test
    fun `failed change deletes newly uploaded cover`() = runTest {
        val tourRepository =
            FakeGuideTourRepository().apply {
                submitChangeResult = DataResult.Error(AppError.GenericFailure)
            }
        val mediaRepository = FakeMediaRepository()
        val useCase = SubmitGuideTourContentChangeUseCase(tourRepository, mediaRepository)

        assertTrue(
            useCase("tour-1", 3, validContent(), "content://cover") is DataResult.Error,
        )
        assertEquals(listOf("media-1"), mediaRepository.deletedMediaIds)
    }

    @Test
    fun `failed upload does not submit content change`() = runTest {
        val tourRepository = FakeGuideTourRepository()
        val mediaRepository =
            FakeMediaRepository().apply {
                uploadResult = DataResult.Error(AppError.ImageUnavailable)
            }
        val useCase = SubmitGuideTourContentChangeUseCase(tourRepository, mediaRepository)

        assertTrue(
            useCase("tour-1", 3, validContent(), "content://cover") is DataResult.Error,
        )
        assertNull(tourRepository.submitChangeInput)
    }

    private fun validContent() =
        TourContentInput(
            title = "City Walk",
            description = "Historic route through the old city",
            countryCode = "TR",
            cityPlaceId = "istanbul-place-id",
            cityName = "Istanbul",
            timeZoneId = "UTC",
            category = TourCategory.CULTURE,
            languageCodes = listOf("en"),
            coverMediaId = "old-media",
        )
}
