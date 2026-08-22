package com.ahmetkaragunlu.guidemate.tour.data.repository

import com.ahmetkaragunlu.guidemate.common.network.error.ApiErrorParser
import com.ahmetkaragunlu.guidemate.common.network.error.NetworkExceptionMapper
import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.tour.data.mapper.toDomain
import com.ahmetkaragunlu.guidemate.tour.data.mapper.toDto
import com.ahmetkaragunlu.guidemate.tour.data.remote.api.GuideTourApi
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.CancelTourSessionRequestDto
import com.ahmetkaragunlu.guidemate.tour.domain.model.guide.GuideDashboard
import com.ahmetkaragunlu.guidemate.tour.domain.model.guide.GuideTourCard
import com.ahmetkaragunlu.guidemate.tour.domain.model.guide.GuideTourDetails
import com.ahmetkaragunlu.guidemate.tour.domain.model.guide.GuideTourListType
import com.ahmetkaragunlu.guidemate.tour.domain.model.guide.TourReviewSubmission
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.CreateGuideTourInput
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.SubmitTourChangeInput
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.TourSessionInput
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.UpdateTourSessionInput
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.TourSession
import com.ahmetkaragunlu.guidemate.tour.domain.repository.GuideTourRepository
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import retrofit2.Response

class GuideTourRepositoryImpl @Inject constructor(
    private val api: GuideTourApi,
    private val apiErrorParser: ApiErrorParser,
    private val networkExceptionMapper: NetworkExceptionMapper,
) : GuideTourRepository {
    override suspend fun getTours(
        tab: GuideTourListType,
        page: Int,
        size: Int,
    ): DataResult<PagedResult<GuideTourCard>> =
        execute(
            request = { api.getTours(tab = tab.name, page = page, size = size) },
            transform = { it.toDomain() },
        )

    override suspend fun getTour(tourId: String): DataResult<GuideTourDetails> =
        execute(request = { api.getTour(tourId) }, transform = { it.toDomain() })

    override suspend fun createTour(input: CreateGuideTourInput): DataResult<TourReviewSubmission> =
        execute(request = { api.createTour(input.toDto()) }, transform = { it.toDomain() })

    override suspend fun submitChange(
        tourId: String,
        input: SubmitTourChangeInput,
    ): DataResult<TourReviewSubmission> =
        execute(request = { api.submitChange(tourId, input.toDto()) }, transform = { it.toDomain() })

    override suspend fun addSession(
        tourId: String,
        input: TourSessionInput,
    ): DataResult<TourSession> =
        execute(request = { api.addSession(tourId, input.toDto()) }, transform = { it.toDomain() })

    override suspend fun updateSession(
        sessionId: String,
        input: UpdateTourSessionInput,
    ): DataResult<TourSession> =
        execute(request = { api.updateSession(sessionId, input.toDto()) }, transform = { it.toDomain() })

    override suspend fun setSessionBookingOpen(
        sessionId: String,
        isOpen: Boolean,
    ): DataResult<TourSession> =
        execute(
            request = { if (isOpen) api.openSession(sessionId) else api.closeSession(sessionId) },
            transform = { it.toDomain() },
        )

    override suspend fun cancelSession(
        sessionId: String,
        reason: String,
        idempotencyKey: String,
    ): DataResult<TourSession> =
        execute(
            request = {
                api.cancelSession(
                    sessionId = sessionId,
                    idempotencyKey = idempotencyKey,
                    request = CancelTourSessionRequestDto(reason.trim()),
                )
            },
            transform = { it.toDomain() },
        )

    override suspend fun archiveTour(tourId: String): DataResult<GuideTourDetails> =
        execute(request = { api.archiveTour(tourId) }, transform = { it.toDomain() })

    override suspend fun getDashboard(): DataResult<GuideDashboard> =
        execute(request = api::getDashboard, transform = { it.toDomain() })

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
