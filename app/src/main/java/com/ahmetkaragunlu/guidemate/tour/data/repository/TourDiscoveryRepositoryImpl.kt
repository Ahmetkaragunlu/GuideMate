package com.ahmetkaragunlu.guidemate.tour.data.repository

import com.ahmetkaragunlu.guidemate.common.network.ApiCallExecutor
import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.tour.data.mapper.toTourSearchDomain
import com.ahmetkaragunlu.guidemate.tour.data.mapper.toTourWithSessionDomain
import com.ahmetkaragunlu.guidemate.tour.data.mapper.toDomain
import com.ahmetkaragunlu.guidemate.tour.data.remote.api.TourDiscoveryApi
import com.ahmetkaragunlu.guidemate.tour.domain.model.catalog.TourWithSession
import com.ahmetkaragunlu.guidemate.tour.domain.model.discovery.TourSearchItem
import com.ahmetkaragunlu.guidemate.tour.domain.model.discovery.TourSearchQuery
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourDetails
import com.ahmetkaragunlu.guidemate.tour.domain.repository.TourDiscoveryRepository
import javax.inject.Inject

class TourDiscoveryRepositoryImpl @Inject constructor(
    private val api: TourDiscoveryApi,
    private val apiCallExecutor: ApiCallExecutor,
) : TourDiscoveryRepository {
    override suspend fun searchTours(
        query: TourSearchQuery,
        page: Int,
        size: Int,
    ): DataResult<PagedResult<TourSearchItem>> =
        apiCallExecutor.execute(
            request = {
                api.searchTours(
                    query = query.text.normalizedOrNull(),
                    countryCode = query.countryCode.normalizedOrNull(),
                    cityPlaceId = query.cityPlaceId.normalizedOrNull(),
                    categoryCode = query.categoryCode.normalizedOrNull(),
                    languageCodes = query.languageCodes.takeIf(List<String>::isNotEmpty),
                    minimumRating = query.minimumRating,
                    minimumPriceMinor = query.minimumPriceMinor,
                    maximumPriceMinor = query.maximumPriceMinor,
                    page = page,
                    size = size,
                    sort = query.sort.name,
                )
            },
            transform = { it.toTourSearchDomain() },
        )

    override suspend fun getPopularTours(
        page: Int,
        size: Int,
    ): DataResult<PagedResult<TourSearchItem>> = getPopularTours(guideId = null, page, size)

    override suspend fun getPopularToursForGuide(
        guideId: Long,
        page: Int,
        size: Int,
    ): DataResult<PagedResult<TourSearchItem>> = getPopularTours(guideId, page, size)

    private suspend fun getPopularTours(
        guideId: Long?,
        page: Int,
        size: Int,
    ): DataResult<PagedResult<TourSearchItem>> =
        apiCallExecutor.execute(
            request = { api.getPopularTours(guideId = guideId, page = page, size = size) },
            transform = { it.toTourSearchDomain() },
        )

    override suspend fun getTour(tourId: String): DataResult<TourDetails> =
        apiCallExecutor.execute(
            request = { api.getTour(tourId) },
            transform = { it.toDomain() },
        )

    override suspend fun getSession(sessionId: String): DataResult<TourWithSession> =
        apiCallExecutor.execute(
            request = { api.getSession(sessionId) },
            transform = { it.toTourWithSessionDomain() },
        )
}

private fun String?.normalizedOrNull(): String? = this?.trim()?.takeIf(String::isNotEmpty)
