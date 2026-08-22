package com.ahmetkaragunlu.guidemate.profile.data.mapper

import com.ahmetkaragunlu.guidemate.common.network.model.ApiPageResponse
import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.media.data.mapper.toDomain
import com.ahmetkaragunlu.guidemate.profile.data.remote.model.GuidePerformanceResponseDto
import com.ahmetkaragunlu.guidemate.profile.data.remote.model.GuideProfileResponseDto
import com.ahmetkaragunlu.guidemate.profile.data.remote.model.GuideSearchItemResponseDto
import com.ahmetkaragunlu.guidemate.profile.data.remote.model.UpdateGuideProfileRequestDto
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuideProfile
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuideProfileUpdate
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuideSearchResult
import com.ahmetkaragunlu.guidemate.profile.domain.model.level.GuideLevelTier
import com.ahmetkaragunlu.guidemate.profile.domain.model.performance.GuidePerformanceSummary

fun GuideProfileResponseDto.toDomain(): GuideProfile =
    GuideProfile(
        guideId = guideId,
        firstName = firstName,
        lastName = lastName,
        displayName = displayName,
        specialtyTitle = specialtyTitle,
        biography = biography,
        languageCodes = languageCodes,
        avatar = avatar?.toDomain(),
        performance = performance.toDomain(),
    )

fun GuideSearchItemResponseDto.toDomain(): GuideSearchResult =
    GuideSearchResult(
        guideId = guideId,
        displayName = displayName,
        specialtyTitle = specialtyTitle,
        avatar = avatar?.toDomain(),
        languageCodes = languageCodes,
        completedSessionCount = completedSessionCount,
        totalParticipantCount = totalParticipantCount,
        averageRating = averageRating,
        reviewCount = reviewCount,
        level = GuideLevelTier.valueOf(level),
    )

fun ApiPageResponse<GuideSearchItemResponseDto>.toDomain(): PagedResult<GuideSearchResult> =
    PagedResult(
        items = content.map(GuideSearchItemResponseDto::toDomain),
        page = page,
        size = size,
        totalElements = totalElements,
        totalPages = totalPages,
        isFirst = isFirst,
        isLast = isLast,
    )

fun GuideProfileUpdate.toDto(): UpdateGuideProfileRequestDto =
    UpdateGuideProfileRequestDto(
        specialtyTitle = specialtyTitle,
        biography = biography,
        languageCodes = languageCodes,
        avatarMediaId = avatarMediaId,
    )

private fun GuidePerformanceResponseDto.toDomain(): GuidePerformanceSummary =
    GuidePerformanceSummary(
        completedSessionCount = completedSessionCount,
        totalParticipantCount = totalParticipantCount,
        averageRating = averageRating,
        reviewCount = reviewCount,
        level = GuideLevelTier.valueOf(level),
    )
