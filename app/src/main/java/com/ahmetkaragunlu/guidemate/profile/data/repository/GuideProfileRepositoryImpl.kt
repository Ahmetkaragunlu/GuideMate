package com.ahmetkaragunlu.guidemate.profile.data.repository

import com.ahmetkaragunlu.guidemate.auth.domain.repository.UserRepository
import com.ahmetkaragunlu.guidemate.common.network.ApiCallExecutor
import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.profile.data.mapper.toDomain
import com.ahmetkaragunlu.guidemate.profile.data.mapper.toDto
import com.ahmetkaragunlu.guidemate.profile.data.remote.api.GuideProfileApi
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuideProfile
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuideProfileUpdate
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuideSearchResult
import com.ahmetkaragunlu.guidemate.profile.domain.repository.GuideProfileRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged

class GuideProfileRepositoryImpl @Inject constructor(
    private val api: GuideProfileApi,
    private val userRepository: UserRepository,
    private val apiCallExecutor: ApiCallExecutor,
) : GuideProfileRepository {
    private val ownProfileCache = MutableStateFlow<GuideProfile?>(null)

    override val ownProfile: Flow<GuideProfile?> =
        combine(ownProfileCache, userRepository.userState) { profile, user ->
            profile?.takeIf { it.guideId == user.userId }
        }.distinctUntilChanged()

    override val cachedOwnProfile: GuideProfile?
        get() = ownProfileCache.value?.takeIf { it.guideId == userRepository.userState.value.userId }

    override suspend fun refreshOwnProfile(): DataResult<GuideProfile> =
        apiCallExecutor.execute(
            request = api::getOwnProfile,
            transform = { it.toDomain() },
        ).also(::cacheSuccess)

    override suspend fun updateOwnProfile(update: GuideProfileUpdate): DataResult<GuideProfile> =
        apiCallExecutor.execute(
            request = { api.updateOwnProfile(update.toDto()) },
            transform = { it.toDomain() },
        ).also(::cacheSuccess)

    override suspend fun getPublicProfile(guideId: Long): DataResult<GuideProfile> =
        apiCallExecutor.execute(
            request = { api.getPublicProfile(guideId) },
            transform = { it.toDomain() },
        )

    override suspend fun searchGuides(
        query: String?,
        page: Int,
        size: Int,
    ): DataResult<PagedResult<GuideSearchResult>> =
        apiCallExecutor.execute(
            request = {
                api.searchGuides(
                    query = query?.trim()?.takeIf(String::isNotEmpty),
                    page = page,
                    size = size,
                )
            },
            transform = { it.toDomain() },
        )

    override suspend fun getTopGuides(limit: Int): DataResult<List<GuideSearchResult>> =
        apiCallExecutor.execute(
            request = { api.getTopGuides(limit) },
            transform = { guides -> guides.map { it.toDomain() } },
        )

    private fun cacheSuccess(result: DataResult<GuideProfile>) {
        if (result is DataResult.Success) ownProfileCache.value = result.data
    }
}
