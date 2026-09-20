package com.ahmetkaragunlu.guidemate.testing.profile

import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaReference
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuideProfile
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuideProfileUpdate
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuideSearchResult
import com.ahmetkaragunlu.guidemate.profile.domain.model.level.GuideLevelTier
import com.ahmetkaragunlu.guidemate.profile.domain.model.performance.GuidePerformanceSummary
import com.ahmetkaragunlu.guidemate.profile.domain.repository.GuideProfileRepository
import com.ahmetkaragunlu.guidemate.profile.domain.repository.UserAvatarRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeUserAvatarRepository : UserAvatarRepository {
    var updateResult: DataResult<MediaReference> =
        DataResult.Success(MediaReference("media-1", "https://example.com/avatar.jpg"))
    var selectedMediaAssetId: String? = null

    override suspend fun updateAvatar(mediaAssetId: String): DataResult<MediaReference> {
        selectedMediaAssetId = mediaAssetId
        return updateResult
    }
}

class GuideProfileFakeResults(profile: GuideProfile) {
    var update: DataResult<GuideProfile> = DataResult.Success(profile)
    var refreshOwnProfile: DataResult<GuideProfile>? = null
    var publicProfile: DataResult<GuideProfile> = DataResult.Success(profile)
    var topGuides: DataResult<List<GuideSearchResult>> = DataResult.Success(emptyList())
}

class GuideProfileFakeCalls {
    var refreshOwnProfile = 0
    val publicProfileGuideIds = mutableListOf<Long>()
    val topGuideLimits = mutableListOf<Int>()
    var update: GuideProfileUpdate? = null
}

class FakeGuideProfileRepository(
    profile: GuideProfile = testGuideProfile(),
) : GuideProfileRepository {
    private val profileState = MutableStateFlow<GuideProfile?>(profile)
    override val ownProfile: Flow<GuideProfile?> = profileState
    override val cachedOwnProfile: GuideProfile?
        get() = profileState.value
    val results = GuideProfileFakeResults(profile)
    val calls = GuideProfileFakeCalls()

    override suspend fun refreshOwnProfile(): DataResult<GuideProfile> {
        calls.refreshOwnProfile += 1
        return results.refreshOwnProfile ?: DataResult.Success(checkNotNull(profileState.value))
    }

    override suspend fun updateOwnProfile(update: GuideProfileUpdate): DataResult<GuideProfile> {
        calls.update = update
        val result = results.update
        if (result is DataResult.Success) profileState.value = result.data
        return result
    }

    override suspend fun getPublicProfile(guideId: Long): DataResult<GuideProfile> {
        calls.publicProfileGuideIds += guideId
        return results.publicProfile
    }

    override suspend fun searchGuides(
        query: String?,
        page: Int,
        size: Int,
    ): DataResult<PagedResult<GuideSearchResult>> = error("Not required by this test fixture")

    override suspend fun getTopGuides(limit: Int): DataResult<List<GuideSearchResult>> {
        calls.topGuideLimits += limit
        return results.topGuides
    }
}

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

fun testGuideSearchResult(guideId: Long = 7): GuideSearchResult =
    GuideSearchResult(
        guideId = guideId,
        displayName = "Ada Guide",
        specialtyTitle = "Historian",
        avatar = null,
        languageCodes = listOf("en"),
        completedSessionCount = 10,
        totalParticipantCount = 50,
        averageRating = 4.8,
        reviewCount = 20,
        level = GuideLevelTier.SUPER,
    )
