package com.ahmetkaragunlu.guidemate.tour.data.repository

import com.ahmetkaragunlu.guidemate.common.network.error.ApiErrorParser
import com.ahmetkaragunlu.guidemate.common.network.error.NetworkExceptionMapper
import com.ahmetkaragunlu.guidemate.common.network.model.ApiPageResponse
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.media.data.remote.model.MediaReferenceResponseDto
import com.ahmetkaragunlu.guidemate.tour.data.remote.api.GuideTourApi
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.CancelTourSessionRequestDto
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.CreateGuideTourRequestDto
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.GuideDashboardResponseDto
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.GuideTourCardResponseDto
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.PublicGuideSummaryResponseDto
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.SubmitTourChangeRequestDto
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.TourDetailResponseDto
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.TourReviewSubmissionResponseDto
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.TourSessionRequestDto
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.TourSessionResponseDto
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.UpdateTourSessionRequestDto
import com.ahmetkaragunlu.guidemate.tour.domain.model.guide.GuideTourListType
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.TourSessionInput
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.UpdateTourSessionInput
import com.google.gson.Gson
import java.time.Instant
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class GuideTourRepositoryImplTest {
    @Test
    fun `list query keeps backend pagination and guide tab contract`() = runBlocking {
        val api = FakeGuideTourApi()
        val repository = createRepository(api)

        val result = repository.getTours(GuideTourListType.REVIEW, page = 2, size = 20)

        assertTrue(result is DataResult.Success)
        val page = (result as DataResult.Success).data
        assertEquals("REVIEW", api.requestedTab)
        assertEquals(2, page.page)
        assertEquals("tour-1", page.items.single().tourId)
    }

    @Test
    fun `session update forwards optimistic version and canonical instant`() = runBlocking {
        val api = FakeGuideTourApi()
        val repository = createRepository(api)
        val startsAt = Instant.parse("2027-05-24T06:00:00Z")

        val result =
            repository.updateSession(
                sessionId = "session-1",
                input =
                    UpdateTourSessionInput(
                        version = 9,
                        session =
                            TourSessionInput(
                                meetingPoint = "Sultanahmet Meydanı",
                                startsAt = startsAt,
                                durationMinutes = 180,
                                priceMinor = 15_000,
                                capacity = 12,
                            ),
                    ),
            )

        assertTrue(result is DataResult.Success)
        assertEquals(9L, api.lastSessionUpdate?.version)
        assertEquals(startsAt.toString(), api.lastSessionUpdate?.startsAt)
        assertEquals(12, api.lastSessionUpdate?.capacity)
    }

    private fun createRepository(api: GuideTourApi): GuideTourRepositoryImpl =
        GuideTourRepositoryImpl(
            api = api,
            apiErrorParser = ApiErrorParser(Gson()),
            networkExceptionMapper = NetworkExceptionMapper(),
        )

    private class FakeGuideTourApi : GuideTourApi {
        var requestedTab: String? = null
        var lastSessionUpdate: UpdateTourSessionRequestDto? = null

        override suspend fun getTours(
            tab: String,
            page: Int,
            size: Int,
        ): Response<ApiPageResponse<GuideTourCardResponseDto>> {
            requestedTab = tab
            return Response.success(
                ApiPageResponse(
                    content = listOf(cardResponse()),
                    page = page,
                    size = size,
                    totalElements = 41,
                    totalPages = 3,
                    isFirst = page == 0,
                    isLast = false,
                ),
            )
        }

        override suspend fun getTour(tourId: String): Response<TourDetailResponseDto> =
            Response.success(detailResponse())

        override suspend fun createTour(
            request: CreateGuideTourRequestDto,
        ): Response<TourReviewSubmissionResponseDto> = Response.success(reviewResponse())

        override suspend fun submitChange(
            tourId: String,
            request: SubmitTourChangeRequestDto,
        ): Response<TourReviewSubmissionResponseDto> = Response.success(reviewResponse())

        override suspend fun addSession(
            tourId: String,
            request: TourSessionRequestDto,
        ): Response<TourSessionResponseDto> = Response.success(sessionResponse())

        override suspend fun updateSession(
            sessionId: String,
            request: UpdateTourSessionRequestDto,
        ): Response<TourSessionResponseDto> {
            lastSessionUpdate = request
            return Response.success(sessionResponse().copy(version = request.version + 1))
        }

        override suspend fun openSession(sessionId: String): Response<TourSessionResponseDto> =
            Response.success(sessionResponse())

        override suspend fun closeSession(sessionId: String): Response<TourSessionResponseDto> =
            Response.success(sessionResponse().copy(status = "CLOSED"))

        override suspend fun cancelSession(
            sessionId: String,
            idempotencyKey: String,
            request: CancelTourSessionRequestDto,
        ): Response<TourSessionResponseDto> =
            Response.success(
                sessionResponse().copy(
                    status = "CANCELLED",
                    cancellationActor = "GUIDE",
                    cancellationReason = request.reason,
                ),
            )

        override suspend fun archiveTour(tourId: String): Response<TourDetailResponseDto> =
            Response.success(detailResponse().copy(approvalStatus = "ARCHIVED"))

        override suspend fun getDashboard(): Response<GuideDashboardResponseDto> =
            Response.success(
                GuideDashboardResponseDto(
                    activeSessionCount = 2,
                    pendingReviewCount = 1,
                    completedSessionCount = 12,
                    totalParticipantCount = 84,
                    averageRating = 4.8,
                    reviewCount = 46,
                    level = "SUPER",
                    currentMonthEarningsMinor = 12_500,
                    currencyCode = "USD",
                ),
            )

        private fun reviewResponse(): TourReviewSubmissionResponseDto =
            TourReviewSubmissionResponseDto(
                reviewId = "review-1",
                reviewType = "CREATE",
                reviewStatus = "PENDING",
                tour = detailResponse(),
            )

        private fun cardResponse(): GuideTourCardResponseDto =
            GuideTourCardResponseDto(
                tourId = "tour-1",
                sessionId = "session-1",
                tourVersion = 7,
                sessionVersion = 3,
                title = "Tarihi İstanbul",
                cityName = "İstanbul",
                countryCode = "TR",
                timeZoneId = "Europe/Istanbul",
                categoryCode = "culture",
                languageCodes = listOf("tr", "en"),
                cover = MediaReferenceResponseDto("media-cover", "https://example.com/cover"),
                startsAt = "2027-05-24T06:00:00Z",
                durationMinutes = 180,
                priceMinor = 15_000,
                currencyCode = "USD",
                bookedCount = 4,
                capacity = 12,
                averageRating = 4.8,
                reviewCount = 46,
                netEarningsMinor = 9_800,
                approvalStatus = "APPROVED",
                sessionStatus = "OPEN_FOR_BOOKING",
                rejectionReason = null,
                canArchive = false,
            )

        private fun detailResponse(): TourDetailResponseDto =
            TourDetailResponseDto(
                tourId = "tour-1",
                version = 7,
                guide = PublicGuideSummaryResponseDto(42, "Ahmet Karagünlü", null),
                title = "Tarihi İstanbul",
                description = "İstanbul'un tarihi rotalarını birlikte keşfedin.",
                countryCode = "TR",
                cityPlaceId = "place-istanbul",
                cityName = "İstanbul",
                timeZoneId = "Europe/Istanbul",
                categoryCode = "culture",
                languageCodes = listOf("tr", "en"),
                cover = MediaReferenceResponseDto("media-cover", "https://example.com/cover"),
                approvalStatus = "APPROVED",
                submittedAt = "2026-08-01T10:00:00Z",
                publishedAt = "2026-08-02T10:00:00Z",
                reviewedAt = "2026-08-02T09:30:00Z",
                rejectionReason = null,
                averageRating = 4.8,
                reviewCount = 46,
                sessions = listOf(sessionResponse()),
            )

        private fun sessionResponse(): TourSessionResponseDto =
            TourSessionResponseDto(
                sessionId = "session-1",
                tourId = "tour-1",
                version = 3,
                meetingPoint = "Sultanahmet Meydanı",
                startsAt = "2027-05-24T06:00:00Z",
                durationMinutes = 180,
                priceMinor = 15_000,
                currencyCode = "USD",
                capacity = 12,
                bookedCount = 4,
                availableCapacity = 8,
                status = "OPEN_FOR_BOOKING",
                cancellationActor = null,
                cancellationReason = null,
                cancelledAt = null,
            )
    }
}
