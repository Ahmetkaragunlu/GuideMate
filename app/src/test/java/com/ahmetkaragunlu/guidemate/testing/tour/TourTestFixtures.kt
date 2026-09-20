package com.ahmetkaragunlu.guidemate.testing.tour

import com.ahmetkaragunlu.guidemate.media.domain.model.MediaReference
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuidePublicSummary
import com.ahmetkaragunlu.guidemate.tour.domain.model.Tour
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourApprovalStatus
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourDetails
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourLanguage
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourLocation
import com.ahmetkaragunlu.guidemate.tour.domain.model.TourPublication
import com.ahmetkaragunlu.guidemate.tour.domain.model.category.TourCategory
import com.ahmetkaragunlu.guidemate.tour.domain.model.guide.GuideTourCard
import com.ahmetkaragunlu.guidemate.tour.domain.model.guide.GuideTourCardPricing
import com.ahmetkaragunlu.guidemate.tour.domain.model.guide.GuideTourCardSchedule
import com.ahmetkaragunlu.guidemate.tour.domain.model.guide.GuideTourCardStats
import com.ahmetkaragunlu.guidemate.tour.domain.model.guide.TourReviewSubmission
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.TourSession
import com.ahmetkaragunlu.guidemate.tour.domain.model.session.TourSessionStatus
import java.time.Instant

fun testTourDetails(
    approvalStatus: TourApprovalStatus = TourApprovalStatus.APPROVED,
    sessionStatus: TourSessionStatus = TourSessionStatus.OPEN_FOR_BOOKING,
): TourDetails =
    TourDetails(
        tour =
            Tour(
                id = "tour-1",
                version = 3,
                guide = GuidePublicSummary(1L, "Ada Guide"),
                title = "City Walk",
                description = "Historic city walk",
                location =
                    TourLocation(
                        countryCode = "TR",
                        country = "Turkiye",
                        cityPlaceId = "istanbul-place-id",
                        city = "Istanbul",
                        timeZoneId = "UTC",
                    ),
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
                cover = MediaReference("cover-1", "https://example.com/cover.jpg"),
                publication = TourPublication(approvalStatus = approvalStatus),
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
        schedule =
            GuideTourCardSchedule(
                cityName = "Istanbul",
                countryCode = "TR",
                timeZoneId = "UTC",
                startsAt = Instant.parse("2099-01-01T12:00:00Z"),
                durationMinutes = 120,
            ),
        category = TourCategory.CULTURE,
        languageCodes = listOf("en"),
        cover = MediaReference("media-$tourId", "https://example.com/$tourId.jpg"),
        pricing =
            GuideTourCardPricing(
                priceMinor = 10_000,
                currencyCode = "USD",
                netEarningsMinor = 5_000,
            ),
        stats =
            GuideTourCardStats(
                bookedCount = 2,
                capacity = 10,
                averageRating = 4.8,
                reviewCount = 20,
            ),
        approvalStatus = TourApprovalStatus.APPROVED,
        sessionStatus = TourSessionStatus.OPEN_FOR_BOOKING,
        rejectionReason = null,
        canArchive = false,
    )
