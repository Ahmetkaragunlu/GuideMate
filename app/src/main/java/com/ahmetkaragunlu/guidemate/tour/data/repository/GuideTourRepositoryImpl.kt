package com.ahmetkaragunlu.guidemate.tour.data.repository

import com.ahmetkaragunlu.guidemate.common.network.ApiCallExecutor
import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.tour.data.mapper.toDomain
import com.ahmetkaragunlu.guidemate.tour.data.mapper.toDto
import com.ahmetkaragunlu.guidemate.tour.data.remote.api.GuideTourApi
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.CancelTourSessionRequestDto
import com.ahmetkaragunlu.guidemate.tour.domain.model.guide.GuideDashboard
import com.ahmetkaragunlu.guidemate.tour.domain.model.guide.GuideTourCard
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourDetails
import com.ahmetkaragunlu.guidemate.tour.domain.model.guide.GuideTourListType
import com.ahmetkaragunlu.guidemate.tour.domain.model.guide.TourReviewSubmission
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.CreateGuideTourInput
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.SubmitTourChangeInput
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.TourSessionInput
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.UpdateTourSessionInput
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.TourSession
import com.ahmetkaragunlu.guidemate.tour.domain.repository.GuideTourRepository
import javax.inject.Inject

class GuideTourRepositoryImpl @Inject constructor(
    private val api: GuideTourApi,
    private val apiCallExecutor: ApiCallExecutor,
) : GuideTourRepository {
    override suspend fun getTours(
        tab: GuideTourListType,
        page: Int,
        size: Int,
    ): DataResult<PagedResult<GuideTourCard>> =
        apiCallExecutor.execute(
            request = { api.getTours(tab = tab.name, page = page, size = size) },
            transform = { it.toDomain() },
        )

    override suspend fun getTour(tourId: String): DataResult<TourDetails> =
        apiCallExecutor.execute(request = { api.getTour(tourId) }, transform = { it.toDomain() })

    override suspend fun createTour(input: CreateGuideTourInput): DataResult<TourReviewSubmission> =
        apiCallExecutor.execute(request = { api.createTour(input.toDto()) }, transform = { it.toDomain() })

    override suspend fun submitChange(
        tourId: String,
        input: SubmitTourChangeInput,
    ): DataResult<TourReviewSubmission> =
        apiCallExecutor.execute(
            request = { api.submitChange(tourId, input.toDto()) },
            transform = { it.toDomain() },
        )

    override suspend fun addSession(
        tourId: String,
        input: TourSessionInput,
    ): DataResult<TourSession> =
        apiCallExecutor.execute(
            request = { api.addSession(tourId, input.toDto()) },
            transform = { it.toDomain() },
        )

    override suspend fun updateSession(
        sessionId: String,
        input: UpdateTourSessionInput,
    ): DataResult<TourSession> =
        apiCallExecutor.execute(
            request = { api.updateSession(sessionId, input.toDto()) },
            transform = { it.toDomain() },
        )

    override suspend fun setSessionBookingOpen(
        sessionId: String,
        isOpen: Boolean,
    ): DataResult<TourSession> =
        apiCallExecutor.execute(
            request = { if (isOpen) api.openSession(sessionId) else api.closeSession(sessionId) },
            transform = { it.toDomain() },
        )

    override suspend fun cancelSession(
        sessionId: String,
        reason: String,
        idempotencyKey: String,
    ): DataResult<TourSession> =
        apiCallExecutor.execute(
            request = {
                api.cancelSession(
                    sessionId = sessionId,
                    idempotencyKey = idempotencyKey,
                    request = CancelTourSessionRequestDto(reason.trim()),
                )
            },
            transform = { it.toDomain() },
        )

    override suspend fun archiveTour(tourId: String): DataResult<TourDetails> =
        apiCallExecutor.execute(
            request = { api.archiveTour(tourId) },
            transform = { it.toDomain() },
        )

    override suspend fun getDashboard(): DataResult<GuideDashboard> =
        apiCallExecutor.execute(request = api::getDashboard, transform = { it.toDomain() })
}
