package com.ahmetkaragunlu.guidemate.tour.domain.repository

import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourDetails
import com.ahmetkaragunlu.guidemate.tour.domain.model.catalog.TourWithSession
import com.ahmetkaragunlu.guidemate.tour.domain.model.discovery.TourSearchItem
import com.ahmetkaragunlu.guidemate.tour.domain.model.discovery.TourSearchQuery

interface TourDiscoveryRepository {
    suspend fun searchTours(
        query: TourSearchQuery,
        page: Int,
        size: Int,
    ): DataResult<PagedResult<TourSearchItem>>

    suspend fun getPopularTours(
        page: Int,
        size: Int,
    ): DataResult<PagedResult<TourSearchItem>>

    suspend fun getTour(tourId: String): DataResult<TourDetails>

    suspend fun getSession(sessionId: String): DataResult<TourWithSession>
}
