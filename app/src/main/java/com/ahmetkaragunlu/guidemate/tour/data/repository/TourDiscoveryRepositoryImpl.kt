package com.ahmetkaragunlu.guidemate.tour.data.repository

import com.ahmetkaragunlu.guidemate.common.network.error.ApiErrorParser
import com.ahmetkaragunlu.guidemate.common.network.error.NetworkExceptionMapper
import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.common.result.AppError
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
import kotlinx.coroutines.CancellationException
import retrofit2.Response

class TourDiscoveryRepositoryImpl @Inject constructor(
    private val api: TourDiscoveryApi,
    private val apiErrorParser: ApiErrorParser,
    private val networkExceptionMapper: NetworkExceptionMapper,
) : TourDiscoveryRepository {
    override suspend fun searchTours(
        query: TourSearchQuery,
        page: Int,
        size: Int,
    ): DataResult<PagedResult<TourSearchItem>> =
        execute(
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
    ): DataResult<PagedResult<TourSearchItem>> =
        execute(
            request = { api.getPopularTours(page = page, size = size) },
            transform = { it.toTourSearchDomain() },
        )

    override suspend fun getTour(tourId: String): DataResult<TourDetails> =
        execute(
            request = { api.getTour(tourId) },
            transform = { it.toDomain() },
        )

    override suspend fun getSession(sessionId: String): DataResult<TourWithSession> =
        execute(
            request = { api.getSession(sessionId) },
            transform = { it.toTourWithSessionDomain() },
        )

    private suspend fun <ResponseBody, Domain> execute(
        request: suspend () -> Response<ResponseBody>,
        transform: (ResponseBody) -> Domain,
    ): DataResult<Domain> =
        try {
            val response = request()
            if (!response.isSuccessful) {
                DataResult.Error(apiErrorParser.parse(response))
            } else {
                response.body()?.let { DataResult.Success(transform(it)) }
                    ?: DataResult.Error(AppError.NoResponseFromServer)
            }
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            DataResult.Error(networkExceptionMapper.map(exception), exception)
        }
}

private fun String?.normalizedOrNull(): String? = this?.trim()?.takeIf(String::isNotEmpty)
