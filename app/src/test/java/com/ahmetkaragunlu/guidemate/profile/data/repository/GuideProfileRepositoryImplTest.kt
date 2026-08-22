package com.ahmetkaragunlu.guidemate.profile.data.repository

import com.ahmetkaragunlu.guidemate.auth.domain.model.UserRole
import com.ahmetkaragunlu.guidemate.auth.domain.model.UserState
import com.ahmetkaragunlu.guidemate.auth.domain.repository.UserRepository
import com.ahmetkaragunlu.guidemate.common.network.error.ApiErrorParser
import com.ahmetkaragunlu.guidemate.common.network.error.NetworkExceptionMapper
import com.ahmetkaragunlu.guidemate.common.network.model.ApiPageResponse
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.profile.data.remote.api.GuideProfileApi
import com.ahmetkaragunlu.guidemate.profile.data.remote.model.GuidePerformanceResponseDto
import com.ahmetkaragunlu.guidemate.profile.data.remote.model.GuideProfileResponseDto
import com.ahmetkaragunlu.guidemate.profile.data.remote.model.GuideSearchItemResponseDto
import com.ahmetkaragunlu.guidemate.profile.data.remote.model.UpdateGuideProfileRequestDto
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class GuideProfileRepositoryImplTest {
    @Test
    fun `refresh caches own profile only for authenticated guide`() = runBlocking {
        val userRepository = FakeUserRepository()
        val repository = createRepository(userRepository)

        val result = repository.refreshOwnProfile()

        assertTrue(result is DataResult.Success)
        assertEquals(42L, repository.cachedOwnProfile?.guideId)
        assertEquals(42L, repository.ownProfile.first()?.guideId)

        userRepository.updateUser(userId = 7)

        assertNull(repository.cachedOwnProfile)
        assertNull(repository.ownProfile.first())
    }

    private fun createRepository(userRepository: UserRepository): GuideProfileRepositoryImpl =
        GuideProfileRepositoryImpl(
            api = FakeGuideProfileApi(),
            userRepository = userRepository,
            apiErrorParser = ApiErrorParser(Gson()),
            networkExceptionMapper = NetworkExceptionMapper(),
        )

    private class FakeUserRepository : UserRepository {
        private val state = MutableStateFlow(userState(userId = 42))
        override val userState: StateFlow<UserState> = state

        override suspend fun restoreCachedUser(): UserState = state.value

        fun updateUser(userId: Long) {
            state.value = userState(userId)
        }

        private fun userState(userId: Long): UserState =
            UserState(
                userId = userId,
                email = "guide@example.com",
                firstName = "Ahmet",
                lastName = "Karagünlü",
                isRoleSelected = true,
                role = UserRole.GUIDE,
            )
    }

    private class FakeGuideProfileApi : GuideProfileApi {
        override suspend fun getOwnProfile(): Response<GuideProfileResponseDto> =
            Response.success(profileResponse())

        override suspend fun updateOwnProfile(
            request: UpdateGuideProfileRequestDto,
        ): Response<GuideProfileResponseDto> = Response.success(profileResponse())

        override suspend fun getPublicProfile(
            guideId: Long,
        ): Response<GuideProfileResponseDto> = Response.success(profileResponse())

        override suspend fun searchGuides(
            query: String?,
            page: Int,
            size: Int,
        ): Response<ApiPageResponse<GuideSearchItemResponseDto>> =
            Response.success(
                ApiPageResponse(
                    content = emptyList(),
                    page = page,
                    size = size,
                    totalElements = 0,
                    totalPages = 0,
                    isFirst = true,
                    isLast = true,
                ),
            )

        override suspend fun getTopGuides(
            limit: Int,
        ): Response<List<GuideSearchItemResponseDto>> = Response.success(emptyList())

        private fun profileResponse(): GuideProfileResponseDto =
            GuideProfileResponseDto(
                guideId = 42,
                firstName = "Ahmet",
                lastName = "Karagünlü",
                displayName = "Ahmet Karagünlü",
                specialtyTitle = "Tarih Rehberi",
                biography = "İstanbul'un tarihini yerel hikayelerle anlatan profesyonel rehber.",
                languageCodes = listOf("tr", "en"),
                avatar = null,
                performance =
                    GuidePerformanceResponseDto(
                        completedSessionCount = 25,
                        totalParticipantCount = 180,
                        averageRating = 4.8,
                        reviewCount = 32,
                        level = "SUPER",
                    ),
            )
    }
}
