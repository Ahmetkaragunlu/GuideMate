package com.ahmetkaragunlu.guidemate.testing

import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaAsset
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaPurpose
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaReference
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaStatus
import com.ahmetkaragunlu.guidemate.media.domain.repository.MediaRepository
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuideProfile
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuideProfileUpdate
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuidePublicSummary
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuideSearchResult
import com.ahmetkaragunlu.guidemate.profile.domain.model.level.GuideLevelTier
import com.ahmetkaragunlu.guidemate.profile.domain.model.performance.GuidePerformanceSummary
import com.ahmetkaragunlu.guidemate.profile.domain.repository.GuideProfileRepository
import com.ahmetkaragunlu.guidemate.tour.domain.model.Tour
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourApprovalStatus
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourDetails
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourLanguage
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
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
import java.time.Instant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeGuideTourRepository : GuideTourRepository {
    var tourResult: DataResult<TourDetails> = DataResult.Success(testTourDetails())
    var createResult: DataResult<TourReviewSubmission> =
        DataResult.Success(testReviewSubmission())
    var submitChangeResult: DataResult<TourReviewSubmission> =
        DataResult.Success(testReviewSubmission())
    var updateSessionResult: DataResult<TourSession> =
        DataResult.Success(testTourDetails().sessions.first())
    var bookingAvailabilityResult: DataResult<TourSession> = updateSessionResult
    var cancelSessionResult: DataResult<TourSession> =
        DataResult.Success(
            testTourDetails(sessionStatus = TourSessionStatus.CANCELLED).sessions.first()
        )
    var listResult: DataResult<PagedResult<GuideTourCard>>? = null
    val listResults = ArrayDeque<DataResult<PagedResult<GuideTourCard>>>()
    val listRequests = mutableListOf<Pair<GuideTourListType, Int>>()
    var createInput: CreateGuideTourInput? = null
    var submitChangeInput: SubmitTourChangeInput? = null
    var updateSessionInput: UpdateTourSessionInput? = null
    var requestedTourId: String? = null
    var cancelSessionRequest: Triple<String, String, String>? = null

    override suspend fun getTours(
        tab: GuideTourListType,
        page: Int,
        size: Int,
    ): DataResult<PagedResult<GuideTourCard>> {
        listRequests += tab to page
        return if (listResults.isNotEmpty()) {
            listResults.removeFirst()
        } else {
            listResult ?: error("No list result configured")
        }
    }

    override suspend fun getTour(tourId: String): DataResult<TourDetails> {
        requestedTourId = tourId
        return tourResult
    }

    override suspend fun createTour(input: CreateGuideTourInput): DataResult<TourReviewSubmission> {
        createInput = input
        return createResult
    }

    override suspend fun submitChange(
        tourId: String,
        input: SubmitTourChangeInput,
    ): DataResult<TourReviewSubmission> {
        submitChangeInput = input
        return submitChangeResult
    }

    override suspend fun addSession(
        tourId: String,
        input: TourSessionInput,
    ): DataResult<TourSession> = error("Not required by this test fixture")

    override suspend fun updateSession(
        sessionId: String,
        input: UpdateTourSessionInput,
    ): DataResult<TourSession> {
        updateSessionInput = input
        return updateSessionResult
    }

    override suspend fun setSessionBookingOpen(
        sessionId: String,
        isOpen: Boolean,
    ): DataResult<TourSession> = bookingAvailabilityResult

    override suspend fun cancelSession(
        sessionId: String,
        reason: String,
        idempotencyKey: String,
    ): DataResult<TourSession> {
        cancelSessionRequest = Triple(sessionId, reason, idempotencyKey)
        return cancelSessionResult
    }

    override suspend fun archiveTour(tourId: String): DataResult<TourDetails> =
        error("Not required by this test fixture")

    override suspend fun getDashboard(): DataResult<GuideDashboard> =
        error("Not required by this test fixture")
}

class FakeMediaRepository : MediaRepository {
    var uploadResult: DataResult<MediaAsset> = DataResult.Success(testMediaAsset())
    var uploadedUri: String? = null
    var deletedMediaIds = mutableListOf<String>()

    override suspend fun uploadImage(
        localUri: String,
        purpose: MediaPurpose,
    ): DataResult<MediaAsset> {
        uploadedUri = localUri
        return uploadResult
    }

    override suspend fun deleteUnreferenced(mediaAssetId: String): DataResult<Unit> {
        deletedMediaIds += mediaAssetId
        return DataResult.Success(Unit)
    }
}

class FakeGuideProfileRepository(
    profile: GuideProfile = testGuideProfile(),
) : GuideProfileRepository {
    private val profileState = MutableStateFlow<GuideProfile?>(profile)
    override val ownProfile: Flow<GuideProfile?> = profileState
    override val cachedOwnProfile: GuideProfile?
        get() = profileState.value
    var updateResult: DataResult<GuideProfile> = DataResult.Success(profile)
    var lastUpdate: GuideProfileUpdate? = null

    override suspend fun refreshOwnProfile(): DataResult<GuideProfile> =
        DataResult.Success(checkNotNull(profileState.value))

    override suspend fun updateOwnProfile(update: GuideProfileUpdate): DataResult<GuideProfile> {
        lastUpdate = update
        val result = updateResult
        if (result is DataResult.Success) profileState.value = result.data
        return result
    }

    override suspend fun getPublicProfile(guideId: Long): DataResult<GuideProfile> =
        error("Not required by this test fixture")

    override suspend fun searchGuides(
        query: String?,
        page: Int,
        size: Int,
    ): DataResult<PagedResult<GuideSearchResult>> = error("Not required by this test fixture")

    override suspend fun getTopGuides(limit: Int): DataResult<List<GuideSearchResult>> =
        error("Not required by this test fixture")
}

fun testTourDetails(
    approvalStatus: TourApprovalStatus = TourApprovalStatus.APPROVED,
    sessionStatus: TourSessionStatus = TourSessionStatus.OPEN_FOR_BOOKING,
): TourDetails =
    TourDetails(
        tour =
            Tour(
                id = "tour-1",
                version = 3,
                guide = GuidePublicSummary("guide-1", "Ada Guide"),
                title = "City Walk",
                description = "Historic city walk",
                countryCode = "TR",
                country = "Turkiye",
                cityPlaceId = "istanbul-place-id",
                city = "Istanbul",
                timeZoneId = "UTC",
                category = TourCategory.CULTURE,
                languages =
                    listOf(
                        TourLanguage(
                            code = "en",
                            flagEmoji = "",
                            displayName = "English",
                            shortCode = "EN",
                        )
                    ),
                coverMediaId = "cover-1",
                coverImageUrl = "https://example.com/cover.jpg",
                approvalStatus = approvalStatus,
            ),
        sessions =
            listOf(
                TourSession(
                    id = "session-1",
                    tourId = "tour-1",
                    version = 5,
                    meetingPoint = "Main square",
                    startsAt = Instant.parse("2099-01-01T12:00:00Z"),
                    durationMinutes = 120,
                    priceMinor = 10_000,
                    capacity = 10,
                    bookedCount = 2,
                    status = sessionStatus,
                )
            ),
    )

fun testReviewSubmission(): TourReviewSubmission =
    TourReviewSubmission(
        reviewId = "review-1",
        reviewType = "CREATE",
        reviewStatus = "PENDING",
        details = testTourDetails(),
    )

fun testGuideTourCard(
    tourId: String,
    sessionId: String,
): GuideTourCard =
    GuideTourCard(
        tourId = tourId,
        sessionId = sessionId,
        tourVersion = 1,
        sessionVersion = 1,
        title = "Tour $tourId",
        cityName = "Istanbul",
        countryCode = "TR",
        timeZoneId = "UTC",
        category = TourCategory.CULTURE,
        languageCodes = listOf("en"),
        cover = MediaReference("media-$tourId", "https://example.com/$tourId.jpg"),
        startsAt = Instant.parse("2099-01-01T12:00:00Z"),
        durationMinutes = 120,
        priceMinor = 10_000,
        currencyCode = "USD",
        bookedCount = 2,
        capacity = 10,
        averageRating = 4.8,
        reviewCount = 20,
        netEarningsMinor = 5_000,
        approvalStatus = TourApprovalStatus.APPROVED,
        sessionStatus = TourSessionStatus.OPEN_FOR_BOOKING,
        rejectionReason = null,
        canArchive = false,
    )

fun testMediaAsset(): MediaAsset =
    MediaAsset(
        mediaAssetId = "media-1",
        purpose = MediaPurpose.TOUR_COVER,
        status = MediaStatus.READY,
        imageUrl = "https://example.com/media.jpg",
        contentType = "image/jpeg",
        sizeBytes = 1_024,
    )

fun testGuideProfile(): GuideProfile =
    GuideProfile(
        guideId = 7,
        firstName = "Ada",
        lastName = "Guide",
        displayName = "Ada Guide",
        specialtyTitle = "Historian",
        biography = "A detailed biography for profile testing.",
        languageCodes = listOf("en"),
        avatar = null,
        performance =
            GuidePerformanceSummary(
                completedSessionCount = 10,
                totalParticipantCount = 50,
                averageRating = 4.8,
                reviewCount = 20,
                level = GuideLevelTier.SUPER,
            ),
    )
