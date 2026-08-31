package com.ahmetkaragunlu.guidemate.tour.data.mapper

import com.ahmetkaragunlu.guidemate.common.network.model.ApiPageResponse
import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.media.data.mapper.toDomain
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuidePublicSummary
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.TourDetailResponseDto
import com.ahmetkaragunlu.guidemate.tour.data.remote.model.TourSearchItemResponseDto
import com.ahmetkaragunlu.guidemate.tour.domain.model.catalog.TourWithSession
import com.ahmetkaragunlu.guidemate.tour.domain.model.discovery.TourSearchItem
import java.time.Instant

fun ApiPageResponse<TourSearchItemResponseDto>.toTourSearchDomain(): PagedResult<TourSearchItem> =
    PagedResult(
        items = content.map(TourSearchItemResponseDto::toDomain),
        page = page,
        size = size,
        totalElements = totalElements,
        totalPages = totalPages,
        isFirst = isFirst,
        isLast = isLast,
    )

fun TourSearchItemResponseDto.toDomain(): TourSearchItem =
    TourSearchItem(
        tourId = tourId,
        sessionId = sessionId,
        title = title,
        category = categoryCode.toTourCategory(),
        cityName = cityName,
        countryCode = countryCode,
        cityPlaceId = cityPlaceId,
        startsAt = Instant.parse(startsAt),
        timeZoneId = timeZoneId,
        durationMinutes = durationMinutes,
        priceMinor = priceMinor,
        currencyCode = currencyCode,
        availableCapacity = availableCapacity,
        languageCodes = languageCodes,
        cover = cover.toDomain(),
        averageRating = averageRating.takeIf { reviewCount > 0 },
        reviewCount = reviewCount,
        guide =
            GuidePublicSummary(
                id = guide.guideId,
                displayName = guide.displayName,
                profileImageUrl = guide.avatar?.imageUrl,
            ),
    )

fun TourDetailResponseDto.toTourWithSessionDomain(): TourWithSession {
    val details = toDomain()
    return TourWithSession(
        tour = details.tour,
        session = checkNotNull(details.sessions.singleOrNull()) {
            "Public tour detail must contain exactly one session"
        },
    )
}
