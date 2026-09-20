package com.ahmetkaragunlu.guidemate.testing.tour

import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourDetails
import com.ahmetkaragunlu.guidemate.tour.domain.model.guide.GuideDashboard
import com.ahmetkaragunlu.guidemate.tour.domain.model.guide.GuideTourCard
import com.ahmetkaragunlu.guidemate.tour.domain.model.guide.GuideTourListType
import com.ahmetkaragunlu.guidemate.tour.domain.model.guide.TourReviewSubmission
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.CreateGuideTourInput
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.SubmitTourChangeInput
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.TourSessionInput
import com.ahmetkaragunlu.guidemate.tour.domain.model.operation.UpdateTourSessionInput
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.TourSession
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.TourSessionStatus
import com.ahmetkaragunlu.guidemate.tour.domain.repository.GuideTourRepository

data class GuideTourListCall(
    val tab: GuideTourListType,
    val page: Int,
)

data class CancelTourSessionCall(
    val sessionId: String,
    val reason: String,
    val idempotencyKey: String,
)

class GuideTourFakeResults {
    var tour: DataResult<TourDetails> = DataResult.Success(testTourDetails())
    var create: DataResult<TourReviewSubmission> =
        DataResult.Success(testReviewSubmission())
    var submitChange: DataResult<TourReviewSubmission> =
        DataResult.Success(testReviewSubmission())
    var updateSession: DataResult<TourSession> =
        DataResult.Success(testTourDetails().sessions.first())
    var bookingAvailability: DataResult<TourSession> = updateSession
    var cancelSession: DataResult<TourSession> =
        DataResult.Success(
            testTourDetails(sessionStatus = TourSessionStatus.CANCELLED).sessions.first()
        )
    var list: DataResult<PagedResult<GuideTourCard>>? = null
    val lists = ArrayDeque<DataResult<PagedResult<GuideTourCard>>>()
    val dashboards = ArrayDeque<DataResult<GuideDashboard>>()
}

class GuideTourFakeCalls {
    val listRequests = mutableListOf<GuideTourListCall>()
    var dashboardRequests = 0
    var create: CreateGuideTourInput? = null
    var submitChange: SubmitTourChangeInput? = null
    var updateSession: UpdateTourSessionInput? = null
    val submitChangeInputs = mutableListOf<SubmitTourChangeInput>()
    val updateSessionInputs = mutableListOf<UpdateTourSessionInput>()
    var requestedTourId: String? = null
    var cancelSession: CancelTourSessionCall? = null
}

class FakeGuideTourRepository : GuideTourRepository {
    val results = GuideTourFakeResults()
    val calls = GuideTourFakeCalls()

    override suspend fun getTours(
        tab: GuideTourListType,
        page: Int,
        size: Int,
    ): DataResult<PagedResult<GuideTourCard>> {
        calls.listRequests += GuideTourListCall(tab = tab, page = page)
        return if (results.lists.isNotEmpty()) {
            results.lists.removeFirst()
        } else {
            results.list ?: error("No list result configured")
        }
    }

    override suspend fun getTour(tourId: String): DataResult<TourDetails> {
        calls.requestedTourId = tourId
        return results.tour
    }

    override suspend fun createTour(input: CreateGuideTourInput): DataResult<TourReviewSubmission> {
        calls.create = input
        return results.create
    }

    override suspend fun submitChange(
        tourId: String,
        input: SubmitTourChangeInput,
    ): DataResult<TourReviewSubmission> {
        calls.submitChange = input
        calls.submitChangeInputs += input
        return results.submitChange
    }

    override suspend fun addSession(
        tourId: String,
        input: TourSessionInput,
    ): DataResult<TourSession> = error("Not required by this test fixture")

    override suspend fun updateSession(
        sessionId: String,
        input: UpdateTourSessionInput,
    ): DataResult<TourSession> {
        calls.updateSession = input
        calls.updateSessionInputs += input
        return results.updateSession
    }

    override suspend fun setSessionBookingOpen(
        sessionId: String,
        isOpen: Boolean,
    ): DataResult<TourSession> = results.bookingAvailability

    override suspend fun cancelSession(
        sessionId: String,
        reason: String,
        idempotencyKey: String,
    ): DataResult<TourSession> {
        calls.cancelSession =
            CancelTourSessionCall(
                sessionId = sessionId,
                reason = reason,
                idempotencyKey = idempotencyKey,
            )
        return results.cancelSession
    }

    override suspend fun archiveTour(tourId: String): DataResult<TourDetails> =
        error("Not required by this test fixture")

    override suspend fun getDashboard(): DataResult<GuideDashboard> {
        calls.dashboardRequests++
        return results.dashboards.removeFirstOrNull()
            ?: error("No dashboard result configured")
    }
}
