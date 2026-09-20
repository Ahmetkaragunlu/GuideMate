package com.ahmetkaragunlu.guidemate.testing.discovery

import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaReference
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuidePublicSummary
import com.ahmetkaragunlu.guidemate.testing.tour.testTourDetails
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourDetails
import com.ahmetkaragunlu.guidemate.tour.domain.model.catalog.TourWithSession
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import com.ahmetkaragunlu.guidemate.tour.domain.model.discovery.TourSearchItem
import com.ahmetkaragunlu.guidemate.tour.domain.model.discovery.TourSearchQuery
import com.ahmetkaragunlu.guidemate.tour.domain.repository.TourDiscoveryRepository
import java.time.Instant

data class PopularToursCall(
    val page: Int,
    val size: Int,
)

class DiscoveryFakeResults {
    val searchResults = ArrayDeque<DataResult<PagedResult<TourSearchItem>>>()
    var popularResult: DataResult<PagedResult<TourSearchItem>> =
        DataResult.Success(tourSearchPage(page = 0, isLast = true))
    val popularResults = ArrayDeque<DataResult<PagedResult<TourSearchItem>>>()
    var popularForGuideResult: DataResult<PagedResult<TourSearchItem>> =
        DataResult.Success(tourSearchPage(page = 0, isLast = true))
    val popularForGuideResults = ArrayDeque<DataResult<PagedResult<TourSearchItem>>>()
    var sessionResult: DataResult<TourWithSession> =
        testTourDetails().let { details ->
            DataResult.Success(TourWithSession(details.tour, details.sessions.first()))
        }
}

class DiscoveryFakeCalls {
    val searchRequests = mutableListOf<SearchRequest>()
    val popularRequests = mutableListOf<PopularToursCall>()
    val popularForGuideRequests = mutableListOf<GuidePopularRequest>()
}

class DiscoveryFakeHandlers {
    var search: (suspend (TourSearchQuery, Int, Int) -> DataResult<PagedResult<TourSearchItem>>)? =
        null
}

class FakeTourDiscoveryRepository : TourDiscoveryRepository {
    val results = DiscoveryFakeResults()
    val calls = DiscoveryFakeCalls()
    val handlers = DiscoveryFakeHandlers()

    override suspend fun searchTours(
        query: TourSearchQuery,
        page: Int,
        size: Int,
    ): DataResult<PagedResult<TourSearchItem>> {
        calls.searchRequests += SearchRequest(query, page, size)
        return handlers.search?.invoke(query, page, size) ?: results.searchResults.removeFirst()
    }

    override suspend fun getPopularTours(
        page: Int,
        size: Int,
    ): DataResult<PagedResult<TourSearchItem>> {
        calls.popularRequests += PopularToursCall(page = page, size = size)
        return results.popularResults.removeFirstOrNull() ?: results.popularResult
    }

    override suspend fun getPopularToursForGuide(
        guideId: Long,
        page: Int,
        size: Int,
    ): DataResult<PagedResult<TourSearchItem>> {
        calls.popularForGuideRequests += GuidePopularRequest(guideId, page, size)
        return if (results.popularForGuideResults.isEmpty()) {
            results.popularForGuideResult
        } else {
            results.popularForGuideResults.removeFirst()
        }
    }

    override suspend fun getTour(tourId: String): DataResult<TourDetails> =
        error("Not required by this test fixture")

    override suspend fun getSession(sessionId: String): DataResult<TourWithSession> =
        results.sessionResult
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
