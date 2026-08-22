package com.ahmetkaragunlu.guidemate.profile.domain.repository

import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuideProfile
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuideProfileUpdate
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuideSearchResult
import kotlinx.coroutines.flow.Flow

interface GuideProfileRepository {
    val ownProfile: Flow<GuideProfile?>

    val cachedOwnProfile: GuideProfile?

    suspend fun refreshOwnProfile(): DataResult<GuideProfile>

    suspend fun updateOwnProfile(update: GuideProfileUpdate): DataResult<GuideProfile>

    suspend fun getPublicProfile(guideId: Long): DataResult<GuideProfile>

    suspend fun searchGuides(
        query: String?,
        page: Int,
        size: Int,
    ): DataResult<PagedResult<GuideSearchResult>>

    suspend fun getTopGuides(limit: Int): DataResult<List<GuideSearchResult>>
}
