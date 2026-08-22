package com.ahmetkaragunlu.guidemate.profile.data.repository

import com.ahmetkaragunlu.guidemate.auth.domain.repository.UserRepository
import com.ahmetkaragunlu.guidemate.common.network.error.ApiErrorParser
import com.ahmetkaragunlu.guidemate.common.network.error.NetworkExceptionMapper
import com.ahmetkaragunlu.guidemate.common.pagination.PagedResult
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.profile.data.mapper.toDomain
import com.ahmetkaragunlu.guidemate.profile.data.mapper.toDto
import com.ahmetkaragunlu.guidemate.profile.data.remote.api.GuideProfileApi
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuideProfile
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuideProfileUpdate
import com.ahmetkaragunlu.guidemate.profile.domain.model.GuideSearchResult
import com.ahmetkaragunlu.guidemate.profile.domain.repository.GuideProfileRepository
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import retrofit2.Response

class GuideProfileRepositoryImpl @Inject constructor(
    private val api: GuideProfileApi,
    private val userRepository: UserRepository,
    private val apiErrorParser: ApiErrorParser,
    private val networkExceptionMapper: NetworkExceptionMapper,
) : GuideProfileRepository {
    private val ownProfileCache = MutableStateFlow<GuideProfile?>(null)

    override val ownProfile: Flow<GuideProfile?> =
        combine(ownProfileCache, userRepository.userState) { profile, user ->
            profile?.takeIf { it.guideId == user.userId }
        }.distinctUntilChanged()

    override val cachedOwnProfile: GuideProfile?
        get() = ownProfileCache.value?.takeIf { it.guideId == userRepository.userState.value.userId }

    override suspend fun refreshOwnProfile(): DataResult<GuideProfile> =
        execute(
            request = api::getOwnProfile,
            transform = { it.toDomain() },
        ).also(::cacheSuccess)

    override suspend fun updateOwnProfile(update: GuideProfileUpdate): DataResult<GuideProfile> =
        execute(
            request = { api.updateOwnProfile(update.toDto()) },
            transform = { it.toDomain() },
        ).also(::cacheSuccess)

    override suspend fun getPublicProfile(guideId: Long): DataResult<GuideProfile> =
        execute(
            request = { api.getPublicProfile(guideId) },
            transform = { it.toDomain() },
        )

    override suspend fun searchGuides(
        query: String?,
        page: Int,
        size: Int,
    ): DataResult<PagedResult<GuideSearchResult>> =
        execute(
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
        execute(
            request = { api.getTopGuides(limit) },
            transform = { guides -> guides.map { it.toDomain() } },
        )

    private fun cacheSuccess(result: DataResult<GuideProfile>) {
        if (result is DataResult.Success) ownProfileCache.value = result.data
    }

    private suspend fun <ResponseBody, Domain> execute(
        request: suspend () -> Response<ResponseBody>,
        transform: (ResponseBody) -> Domain,
    ): DataResult<Domain> =
        try {
            val response = request()
            if (!response.isSuccessful) {
                DataResult.Error(apiErrorParser.parse(response))
            } else {
                val body = response.body()
                if (body == null) {
                    DataResult.Error(AppError.NoResponseFromServer)
                } else {
                    DataResult.Success(transform(body))
                }
            }
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            DataResult.Error(networkExceptionMapper.map(exception), exception)
        }
}
