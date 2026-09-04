package com.ahmetkaragunlu.guidemate.testing

import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaReference
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuidePublicSummary
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourDetails
import com.ahmetkaragunlu.guidemate.tour.domain.model.catalog.TourWithSession
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import com.ahmetkaragunlu.guidemate.tour.domain.model.discovery.TourSearchItem
import com.ahmetkaragunlu.guidemate.tour.domain.model.discovery.TourSearchQuery
import com.ahmetkaragunlu.guidemate.tour.domain.repository.TourDiscoveryRepository
import java.time.Instant

class FakeTourDiscoveryRepository : TourDiscoveryRepository {
    val searchResults = ArrayDeque<DataResult<PagedResult<TourSearchItem>>>()
    val searchRequests = mutableListOf<SearchRequest>()
    var popularForGuideResult: DataResult<PagedResult<TourSearchItem>> =
        DataResult.Success(tourSearchPage(page = 0, isLast = true))
    val popularForGuideResults = ArrayDeque<DataResult<PagedResult<TourSearchItem>>>()
    val popularForGuideRequests = mutableListOf<GuidePopularRequest>()
    var sessionResult: DataResult<TourWithSession> =
        testTourDetails().let { details ->
            DataResult.Success(TourWithSession(details.tour, details.sessions.first()))
        }

    override suspend fun searchTours(
        query: TourSearchQuery,
        page: Int,
        size: Int,
    ): DataResult<PagedResult<TourSearchItem>> {
        searchRequests += SearchRequest(query, page, size)
        return searchResults.removeFirst()
    }

    override suspend fun getPopularTours(
        page: Int,
        size: Int,
    ): DataResult<PagedResult<TourSearchItem>> = error("Not required by this test fixture")

    override suspend fun getPopularToursForGuide(
        guideId: Long,
        page: Int,
        size: Int,
    ): DataResult<PagedResult<TourSearchItem>> {
        popularForGuideRequests += GuidePopularRequest(guideId, page, size)
        return if (popularForGuideResults.isEmpty()) {
            popularForGuideResult
        } else {
            popularForGuideResults.removeFirst()
        }
    }

    override suspend fun getTour(tourId: String): DataResult<TourDetails> =
        error("Not required by this test fixture")

    override suspend fun getSession(sessionId: String): DataResult<TourWithSession> =
        sessionResult
}

data class SearchRequest(
    val query: TourSearchQuery,
    val page: Int,
    val size: Int,
)

data class GuidePopularRequest(
    val guideId: Long,
    val page: Int,
    val size: Int,
)

fun testTourSearchItem(
    tourId: String,
    sessionId: String,
): TourSearchItem =
    TourSearchItem(
        tourId = tourId,
        sessionId = sessionId,
        title = "Tour $tourId",
        category = TourCategory.CULTURE,
        cityName = "Istanbul",
        countryCode = "TR",
        cityPlaceId = "istanbul-place-id",
        startsAt = Instant.parse("2099-01-01T12:00:00Z"),
        timeZoneId = "UTC",
        durationMinutes = 120,
        priceMinor = 10_000,
        currencyCode = "USD",
        availableCapacity = 8,
        languageCodes = listOf("en"),
        cover = MediaReference("media-$tourId", "https://example.com/$tourId.jpg"),
        averageRating = 4.8,
        reviewCount = 20,
        guide = GuidePublicSummary(1L, "Ada Guide"),
    )

fun tourSearchPage(
    page: Int,
    isLast: Boolean,
    vararg items: TourSearchItem,
): PagedResult<TourSearchItem> =
    PagedResult(
        items = items.toList(),
        page = page,
        size = 20,
        totalElements = items.size.toLong() + if (isLast) 0 else 1,
        totalPages = if (isLast) page + 1 else page + 2,
        isFirst = page == 0,
        isLast = isLast,
    )
