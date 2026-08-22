package com.ahmetkaragunlu.guidemate.tour.data.repository

import com.ahmetkaragunlu.guidemate.common.network.error.ApiErrorParser
import com.ahmetkaragunlu.guidemate.common.network.error.NetworkExceptionMapper
import com.ahmetkaragunlu.guidemate.common.network.model.ApiPageResponse
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.media.data.remote.model.MediaReferenceResponseDto
import com.ahmetkaragunlu.guidemate.tour.data.remote.api.TourDiscoveryApi
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.PublicGuideSummaryResponseDto
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.TourDetailResponseDto
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.TourSearchItemResponseDto
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.TourSessionResponseDto
import com.ahmetkaragunlu.guidemate.tour.domain.model.discovery.TourSearchQuery
import com.ahmetkaragunlu.guidemate.tour.domain.model.discovery.TourSearchSort
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class TourDiscoveryRepositoryImplTest {
    @Test
    fun `search forwards typed filters and maps backend pagination`() = runBlocking {
        val api = FakeTourDiscoveryApi()
        val repository = createRepository(api)

        val result =
            repository.searchTours(
                query =
                    TourSearchQuery(
                        text = "  tarih  ",
                        countryCode = "TR",
                        cityPlaceId = "place-istanbul",
                        categoryCode = "culture",
                        languageCodes = listOf("tr", "en"),
                        minimumRating = 4.5,
                        minimumPriceMinor = 10_000,
                        maximumPriceMinor = 30_000,
                        sort = TourSearchSort.RATING_DESC,
                    ),
                page = 2,
                size = 20,
            )

        assertTrue(result is DataResult.Success)
        val page = (result as DataResult.Success).data
        assertEquals("tarih", api.query)
        assertEquals("TR", api.countryCode)
        assertEquals("place-istanbul", api.cityPlaceId)
        assertEquals("culture", api.categoryCode)
        assertEquals(listOf("tr", "en"), api.languageCodes)
        assertEquals("RATING_DESC", api.sort)
        assertEquals(2, page.page)
        assertEquals(8, page.items.single().availableCapacity)
    }

    @Test
    fun `tour detail preserves all sessions while session detail requires one session`() =
        runBlocking {
            val api = FakeTourDiscoveryApi()
            val repository = createRepository(api)

            val tourResult = repository.getTour("tour-1")
            val sessionResult = repository.getSession("session-1")

            assertTrue(tourResult is DataResult.Success)
            assertEquals(2, (tourResult as DataResult.Success).data.sessions.size)
            assertTrue(sessionResult is DataResult.Success)
            assertEquals("session-1", (sessionResult as DataResult.Success).data.session.id)
        }

    private fun createRepository(api: TourDiscoveryApi): TourDiscoveryRepositoryImpl =
        TourDiscoveryRepositoryImpl(
            api = api,
            apiErrorParser = ApiErrorParser(Gson()),
            networkExceptionMapper = NetworkExceptionMapper(),
        )

    private class FakeTourDiscoveryApi : TourDiscoveryApi {
        var query: String? = null
        var countryCode: String? = null
        var cityPlaceId: String? = null
        var categoryCode: String? = null
        var languageCodes: List<String>? = null
        var sort: String? = null

        override suspend fun searchTours(
            query: String?,
            countryCode: String?,
            cityPlaceId: String?,
            categoryCode: String?,
            languageCodes: List<String>?,
            minimumRating: Double?,
            minimumPriceMinor: Long?,
            maximumPriceMinor: Long?,
            page: Int,
            size: Int,
            sort: String,
        ): Response<ApiPageResponse<TourSearchItemResponseDto>> {
            this.query = query
            this.countryCode = countryCode
            this.cityPlaceId = cityPlaceId
            this.categoryCode = categoryCode
            this.languageCodes = languageCodes
            this.sort = sort
            return Response.success(
                ApiPageResponse(
                    content = listOf(searchItemResponse()),
                    page = page,
                    size = size,
                    totalElements = 41,
                    totalPages = 3,
                    isFirst = false,
                    isLast = false,
                ),
            )
        }

        override suspend fun getPopularTours(
            page: Int,
            size: Int,
        ): Response<ApiPageResponse<TourSearchItemResponseDto>> =
            Response.success(
                ApiPageResponse(
                    content = listOf(searchItemResponse()),
                    page = page,
                    size = size,
                    totalElements = 1,
                    totalPages = 1,
                    isFirst = true,
                    isLast = true,
                ),
            )

        override suspend fun getTour(tourId: String): Response<TourDetailResponseDto> =
            Response.success(
                detailResponse(
                    sessions =
                        listOf(
                            sessionResponse("session-1"),
                            sessionResponse("session-2"),
                        ),
                ),
            )

        override suspend fun getSession(sessionId: String): Response<TourDetailResponseDto> =
            Response.success(detailResponse(sessions = listOf(sessionResponse(sessionId))))

        private fun searchItemResponse(): TourSearchItemResponseDto =
            TourSearchItemResponseDto(
                tourId = "tour-1",
                sessionId = "session-1",
                title = "Tarihi İstanbul",
                categoryCode = "culture",
                cityName = "İstanbul",
                countryCode = "TR",
                cityPlaceId = "place-istanbul",
                startsAt = "2027-05-24T06:00:00Z",
                timeZoneId = "Europe/Istanbul",
                durationMinutes = 180,
                priceMinor = 15_000,
                currencyCode = "USD",
                availableCapacity = 8,
                languageCodes = listOf("tr", "en"),
                cover = mediaResponse(),
                averageRating = 4.8,
                reviewCount = 46,
                guide = guideResponse(),
            )

        private fun detailResponse(
            sessions: List<TourSessionResponseDto>,
        ): TourDetailResponseDto =
            TourDetailResponseDto(
                tourId = "tour-1",
                version = 7,
                guide = guideResponse(),
                title = "Tarihi İstanbul",
                description = "İstanbul'un tarihi rotalarını birlikte keşfedin.",
                countryCode = "TR",
                cityPlaceId = "place-istanbul",
                cityName = "İstanbul",
                timeZoneId = "Europe/Istanbul",
                categoryCode = "culture",
                languageCodes = emptyList(),
                cover = mediaResponse(),
                approvalStatus = "APPROVED",
                submittedAt = "2026-08-01T10:00:00Z",
                publishedAt = "2026-08-02T10:00:00Z",
                reviewedAt = "2026-08-02T09:30:00Z",
                rejectionReason = null,
                averageRating = 4.8,
                reviewCount = 46,
                sessions = sessions,
            )

        private fun sessionResponse(sessionId: String): TourSessionResponseDto =
            TourSessionResponseDto(
                sessionId = sessionId,
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

        private fun guideResponse(): PublicGuideSummaryResponseDto =
            PublicGuideSummaryResponseDto(
                guideId = 42,
                displayName = "Ahmet Karagünlü",
                avatar = null,
            )

        private fun mediaResponse(): MediaReferenceResponseDto =
            MediaReferenceResponseDto(
                mediaAssetId = "media-cover",
                imageUrl = "https://example.com/cover",
            )
    }
}
