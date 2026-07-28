package com.ahmetkaragunlu.guidemate.screens.common.guide.performance.store

import com.ahmetkaragunlu.guidemate.screens.common.guide.level.model.calculateGuideLevelTier
import com.ahmetkaragunlu.guidemate.screens.common.guide.performance.model.GuidePerformanceSummary
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Singleton
class GuidePerformanceStore
    @Inject
    constructor() {
        private val _summary = MutableStateFlow(createMockSummary())
        val summary: StateFlow<GuidePerformanceSummary> = _summary.asStateFlow()

        private companion object {
            // Mock data (MVP)
            fun createMockSummary(): GuidePerformanceSummary {
                val completedSessionCount = 45
                val averageRating = 4.9
                val reviewCount = 126

                return GuidePerformanceSummary(
                    completedSessionCount = completedSessionCount,
                    totalParticipantCount = 320,
                    averageRating = averageRating,
                    reviewCount = reviewCount,
                    level =
                        calculateGuideLevelTier(
                            completedSessionCount = completedSessionCount,
                            rating = averageRating,
                            reviewCount = reviewCount,
                        ),
                )
            }
        }
    }
