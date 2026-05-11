package com.ahmetkaragunlu.guidemate.screens.guide.profile.shared

import com.ahmetkaragunlu.guidemate.domain.repository.UserRepository
import com.ahmetkaragunlu.guidemate.screens.guide.profile.model.GuideProfileUiState
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
    ) {
        fun profileState(): Flow<GuideProfileUiState> =
            combine(userRepository.userState, sharedStore.state) { user, shared ->
                GuideProfileUiState(
                    firstName = user.firstName,
                    lastName = user.lastName,
                    profileImageResId = shared.profileImageResId,
                    profileImageUrl = shared.profileImageUrl,
                    title = shared.title,
                    guideLevel = shared.guideLevel,
                    rating = shared.rating,
                    tourCount = shared.tourCount,
                    biography = shared.biography,
                    spokenLanguages = shared.spokenLanguages,
                    popularTours = shared.popularTours,
                )
            }
    }
