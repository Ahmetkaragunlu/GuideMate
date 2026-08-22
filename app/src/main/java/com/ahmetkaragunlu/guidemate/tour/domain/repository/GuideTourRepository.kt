package com.ahmetkaragunlu.guidemate.tour.domain.repository

import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.common.result.DataResult
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

interface GuideTourRepository {
    suspend fun getTours(
        tab: GuideTourListType,
        page: Int,
        size: Int,
    ): DataResult<PagedResult<GuideTourCard>>

    suspend fun getTour(tourId: String): DataResult<GuideTourDetails>

    suspend fun createTour(input: CreateGuideTourInput): DataResult<TourReviewSubmission>

    suspend fun submitChange(
        tourId: String,
        input: SubmitTourChangeInput,
    ): DataResult<TourReviewSubmission>

    suspend fun addSession(
        tourId: String,
        input: TourSessionInput,
    ): DataResult<TourSession>

    suspend fun updateSession(
        sessionId: String,
        input: UpdateTourSessionInput,
    ): DataResult<TourSession>

    suspend fun setSessionBookingOpen(
        sessionId: String,
        isOpen: Boolean,
    ): DataResult<TourSession>

    suspend fun cancelSession(
        sessionId: String,
        reason: String,
        idempotencyKey: String,
    ): DataResult<TourSession>

    suspend fun archiveTour(tourId: String): DataResult<GuideTourDetails>

    suspend fun getDashboard(): DataResult<GuideDashboard>
}
