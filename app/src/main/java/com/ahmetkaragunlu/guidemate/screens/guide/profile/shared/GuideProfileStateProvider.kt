package com.ahmetkaragunlu.guidemate.screens.guide.profile.shared

import com.ahmetkaragunlu.guidemate.domain.model.UserState
import com.ahmetkaragunlu.guidemate.domain.repository.UserRepository
import com.ahmetkaragunlu.guidemate.screens.common.guide.model.MOCK_CURRENT_GUIDE_ID
import com.ahmetkaragunlu.guidemate.screens.common.guide.performance.model.GuidePerformanceSummary
import com.ahmetkaragunlu.guidemate.screens.common.guide.performance.store.GuidePerformanceStore
import com.ahmetkaragunlu.guidemate.screens.common.tours.mapper.toPopularTourCardUiModel
import com.ahmetkaragunlu.guidemate.screens.common.tours.model.catalog.TourCatalogState
import com.ahmetkaragunlu.guidemate.screens.common.tours.store.TourCatalogStore
import com.ahmetkaragunlu.guidemate.screens.common.tours.store.refreshAtSessionTransitions
import com.ahmetkaragunlu.guidemate.screens.guide.profile.model.GuideProfileSharedState
import com.ahmetkaragunlu.guidemate.screens.guide.profile.model.GuideProfileUiState
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class GuideProfileStateProvider
    @Inject
    constructor(
        private val userRepository: UserRepository,
        private val sharedStore: GuideProfileSharedStore,
        private val performanceStore: GuidePerformanceStore,
        private val tourCatalogStore: TourCatalogStore,
    ) {
        fun profileState(): Flow<GuideProfileUiState> =
            combine(
                userRepository.userState,
                sharedStore.state,
                performanceStore.summary,
                tourCatalogStore.state.refreshAtSessionTransitions(),
            ) { user, shared, performance, catalog ->
                createProfileState(
                    user = user,
                    shared = shared,
                    performance = performance,
                    catalog = catalog,
                )
            }

        fun currentProfileState(): GuideProfileUiState =
            createProfileState(
                user = userRepository.userState.value,
                shared = sharedStore.state.value,
                performance = performanceStore.summary.value,
                catalog = tourCatalogStore.state.value,
            )

        private fun createProfileState(
            user: UserState,
            shared: GuideProfileSharedState,
            performance: GuidePerformanceSummary,
            catalog: TourCatalogState,
        ): GuideProfileUiState =
            GuideProfileUiState(
                firstName = user.firstName,
                lastName = user.lastName,
                profileImageResId = shared.profileImageResId,
                profileImageUrl = shared.profileImageUrl,
                selectedProfileImageUri = shared.selectedProfileImageUri,
                title = shared.title,
                guideLevel = performance.level,
                rating = performance.averageRating,
                tourCount = performance.completedSessionCount,
                biography = shared.biography,
                spokenLanguages = shared.spokenLanguages,
                popularTours =
                    catalog
                        .bookableTourItemsForGuideAt(
                            guideId = MOCK_CURRENT_GUIDE_ID,
                            now = Instant.now(),
                        ).map { tourWithSession ->
                            tourWithSession.toPopularTourCardUiModel()
                        },
            )
    }
