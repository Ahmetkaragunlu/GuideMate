package com.ahmetkaragunlu.guidemate.profile.data.mapper

import com.ahmetkaragunlu.guidemate.common.network.model.ApiPageResponse
import com.ahmetkaragunlu.guidemate.profile.data.remote.model.GuidePerformanceResponseDto
import com.ahmetkaragunlu.guidemate.profile.data.remote.model.GuideProfileResponseDto
import com.ahmetkaragunlu.guidemate.profile.data.remote.model.GuideSearchItemResponseDto
import com.ahmetkaragunlu.guidemate.media.data.remote.model.MediaReferenceResponseDto
import com.ahmetkaragunlu.guidemate.profile.domain.model.level.GuideLevelTier
import org.junit.Assert.assertEquals
import org.junit.Test

class GuideProfileMapperTest {
    @Test
    fun `maps canonical profile and performance projection`() {
        val response = profileResponse()

        val result = response.toDomain()

        assertEquals(42L, result.guideId)
        assertEquals("Ahmet Karagünlü", result.displayName)
        assertEquals("media-1", result.avatar?.mediaAssetId)
        assertEquals(25L, result.performance.completedSessionCount)
        assertEquals(GuideLevelTier.SUPER, result.performance.level)
    }

    @Test
    fun `maps guide search page without losing pagination metadata`() {
        val response =
            ApiPageResponse(
                content = listOf(searchItem()),
                page = 1,
                size = 20,
                totalElements = 23,
                totalPages = 2,
                isFirst = false,
                isLast = true,
            )

        val result = response.toDomain()

        assertEquals(1, result.page)
        assertEquals(23L, result.totalElements)
        assertEquals(true, result.isLast)
        assertEquals(42L, result.items.single().guideId)
    }

    private fun profileResponse(): GuideProfileResponseDto =
        GuideProfileResponseDto(
            guideId = 42,
            firstName = "Ahmet",
            lastName = "Karagünlü",
            displayName = "Ahmet Karagünlü",
            specialtyTitle = "Tarih Rehberi",
            biography = "İstanbul'un tarihini yerel hikayelerle anlatan profesyonel rehber.",
            languageCodes = listOf("tr", "en"),
            avatar = MediaReferenceResponseDto("media-1", "https://example.com/avatar"),
            performance =
                GuidePerformanceResponseDto(
                    completedSessionCount = 25,
                    totalParticipantCount = 180,
                    averageRating = 4.8,
                    reviewCount = 32,
                    level = "SUPER",
                ),
        )

    private fun searchItem(): GuideSearchItemResponseDto =
        GuideSearchItemResponseDto(
            guideId = 42,
            displayName = "Ahmet Karagünlü",
            specialtyTitle = "Tarih Rehberi",
            avatar = null,
            languageCodes = listOf("tr"),
            completedSessionCount = 25,
            totalParticipantCount = 180,
            averageRating = 4.8,
            reviewCount = 32,
            level = "SUPER",
        )
}
