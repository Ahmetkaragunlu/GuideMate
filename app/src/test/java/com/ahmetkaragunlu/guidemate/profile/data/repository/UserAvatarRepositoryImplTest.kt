package com.ahmetkaragunlu.guidemate.profile.data.repository

import com.ahmetkaragunlu.guidemate.auth.domain.model.UserState
import com.ahmetkaragunlu.guidemate.auth.domain.repository.UserRepository
import com.ahmetkaragunlu.guidemate.common.network.testApiCallExecutor
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.media.data.remote.model.MediaReferenceResponseDto
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaAsset
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaPurpose
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaStatus
import com.ahmetkaragunlu.guidemate.media.domain.repository.MediaRepository
import com.ahmetkaragunlu.guidemate.profile.data.remote.api.UserAvatarApi
import com.ahmetkaragunlu.guidemate.profile.data.remote.model.UpdateUserAvatarRequestDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class UserAvatarRepositoryImplTest {
    @Test
    fun `successful update uploads user avatar and updates cached identity`() = runBlocking {
        val api = FakeUserAvatarApi()
        val mediaRepository = FakeMediaRepository()
        val userRepository = FakeUserRepository()
        val repository = createRepository(api, mediaRepository, userRepository)

        val result = repository.updateAvatar("content://avatar")

        assertTrue(result is DataResult.Success)
        assertEquals("content://avatar", mediaRepository.uploadedUri)
        assertEquals(MediaPurpose.USER_AVATAR, mediaRepository.uploadedPurpose)
        assertEquals("media-1", api.lastRequest?.avatarMediaId)
        assertEquals("media-1", userRepository.userState.value.avatarMediaId)
        assertEquals("https://example.com/avatar.jpg", userRepository.userState.value.avatarUrl)
        assertTrue(mediaRepository.deletedMediaIds.isEmpty())
    }

    @Test
    fun `failed attachment deletes the unreferenced upload`() = runBlocking {
        val api =
            FakeUserAvatarApi(
                response = Response.error(409, "conflict".toResponseBody()),
            )
        val mediaRepository = FakeMediaRepository()
        val userRepository = FakeUserRepository()
        val repository = createRepository(api, mediaRepository, userRepository)

        val result = repository.updateAvatar("content://avatar")

        assertTrue(result is DataResult.Error)
        assertEquals(listOf("media-1"), mediaRepository.deletedMediaIds)
        assertEquals(null, userRepository.userState.value.avatarMediaId)
    }

    private fun createRepository(
        api: UserAvatarApi,
        mediaRepository: MediaRepository,
        userRepository: UserRepository,
    ): UserAvatarRepositoryImpl =
        UserAvatarRepositoryImpl(
            api = api,
            mediaRepository = mediaRepository,
            userRepository = userRepository,
            apiCallExecutor = testApiCallExecutor(),
        )

    private class FakeUserAvatarApi(
        var response: Response<MediaReferenceResponseDto> =
            Response.success(
                MediaReferenceResponseDto("media-1", "https://example.com/avatar.jpg"),
            ),
    ) : UserAvatarApi {
        var lastRequest: UpdateUserAvatarRequestDto? = null

        override suspend fun updateAvatar(
            request: UpdateUserAvatarRequestDto,
        ): Response<MediaReferenceResponseDto> {
            lastRequest = request
            return response
        }
    }

    private class FakeMediaRepository : MediaRepository {
        var uploadedUri: String? = null
        var uploadedPurpose: MediaPurpose? = null
        val deletedMediaIds = mutableListOf<String>()

        override suspend fun uploadImage(
            localUri: String,
            purpose: MediaPurpose,
        ): DataResult<MediaAsset> {
            uploadedUri = localUri
            uploadedPurpose = purpose
            return DataResult.Success(
                MediaAsset(
                    mediaAssetId = "media-1",
                    purpose = purpose,
                    status = MediaStatus.READY,
                    imageUrl = "https://example.com/avatar.jpg",
                    contentType = "image/jpeg",
                    sizeBytes = 1_024,
                ),
            )
        }

        override suspend fun deleteUnreferenced(mediaAssetId: String): DataResult<Unit> {
            deletedMediaIds += mediaAssetId
            return DataResult.Success(Unit)
        }
    }

    private class FakeUserRepository : UserRepository {
        private val state = MutableStateFlow(UserState())
        override val userState: StateFlow<UserState> = state

        override suspend fun restoreCachedUser(): UserState = state.value

        override suspend fun updateAvatar(mediaAssetId: String, imageUrl: String) {
            state.value = state.value.copy(avatarMediaId = mediaAssetId, avatarUrl = imageUrl)
        }
    }
}
