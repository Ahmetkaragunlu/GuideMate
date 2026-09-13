package com.ahmetkaragunlu.guidemate.profile.data.repository

import com.ahmetkaragunlu.guidemate.common.network.testApiCallExecutor
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.media.data.remote.model.MediaReferenceResponseDto
import com.ahmetkaragunlu.guidemate.profile.data.remote.api.UserAvatarApi
import com.ahmetkaragunlu.guidemate.profile.data.remote.model.UpdateUserAvatarRequestDto
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class UserAvatarRepositoryImplTest {
    @Test
    fun `update sends media id and maps avatar response`() = runBlocking {
        val api = FakeUserAvatarApi()
        val repository = UserAvatarRepositoryImpl(api, testApiCallExecutor())

        val result = repository.updateAvatar("media-1")

        assertTrue(result is DataResult.Success)
        assertEquals("media-1", api.lastRequest?.avatarMediaId)
        assertEquals(
            "https://example.com/avatar.jpg",
            (result as DataResult.Success).data.imageUrl,
        )
    }

    @Test
    fun `update exposes api failure`() = runBlocking {
        val repository =
            UserAvatarRepositoryImpl(
                api = FakeUserAvatarApi(Response.error(409, "conflict".toResponseBody())),
                apiCallExecutor = testApiCallExecutor(),
            )

        assertTrue(repository.updateAvatar("media-1") is DataResult.Error)
    }

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
}
