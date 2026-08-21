package com.ahmetkaragunlu.guidemate.profile.domain.model.level

import org.junit.Assert.assertEquals
import org.junit.Test

class GuideLevelCalculatorTest {
    @Test
    fun `single high rating does not increase guide level`() {
        val level =
            calculateGuideLevelTier(
                completedSessionCount = 100,
                rating = 5.0,
                reviewCount = 1,
            )

        assertEquals(GuideLevelTier.APPROVED, level)
    }

    @Test
    fun `guide reaches silver at all minimum requirements`() {
        val level =
            calculateGuideLevelTier(
                completedSessionCount = GuideLevelTier.SILVER.minTourCount,
                rating = GuideLevelTier.SILVER.minRating,
                reviewCount = GuideLevelTier.SILVER.minReviewCount,
            )

        assertEquals(GuideLevelTier.SILVER, level)
    }

    @Test
    fun `guide reaches super at all minimum requirements`() {
        val level =
            calculateGuideLevelTier(
                completedSessionCount = GuideLevelTier.SUPER.minTourCount,
                rating = GuideLevelTier.SUPER.minRating,
                reviewCount = GuideLevelTier.SUPER.minReviewCount,
            )

        assertEquals(GuideLevelTier.SUPER, level)
    }

    @Test
    fun `guide reaches legendary at all minimum requirements`() {
        val level =
            calculateGuideLevelTier(
                completedSessionCount = GuideLevelTier.LEGENDARY.minTourCount,
                rating = GuideLevelTier.LEGENDARY.minRating,
                reviewCount = GuideLevelTier.LEGENDARY.minReviewCount,
            )

        assertEquals(GuideLevelTier.LEGENDARY, level)
    }

    @Test
    fun `guide falls back to highest fully completed level`() {
        val level =
            calculateGuideLevelTier(
                completedSessionCount = GuideLevelTier.LEGENDARY.minTourCount,
                rating = GuideLevelTier.LEGENDARY.minRating,
                reviewCount = GuideLevelTier.SUPER.minReviewCount,
            )

        assertEquals(GuideLevelTier.SUPER, level)
    }
}
