package com.ahmetkaragunlu.guidemate.media.data.repository

import com.ahmetkaragunlu.guidemate.common.network.error.ApiErrorParser
import com.ahmetkaragunlu.guidemate.common.network.error.NetworkExceptionMapper
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.BackendErrorCode
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.media.data.multipart.MediaPartFactory
import com.ahmetkaragunlu.guidemate.media.data.multipart.MediaPreparationException
import com.ahmetkaragunlu.guidemate.media.data.remote.api.MediaApi
import com.ahmetkaragunlu.guidemate.media.data.remote.model.MediaDeletionResponse
import com.ahmetkaragunlu.guidemate.media.data.remote.model.MediaUploadResponse
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaPurpose
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaStatus
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class MediaRepositoryImplTest {
    @Test
    fun `uploads local image and maps canonical backend reference`() = runBlocking {
        val api = FakeMediaApi()
        val repository = createRepository(api)

        val result = repository.uploadImage("content://image/1", MediaPurpose.USER_AVATAR)

        assertTrue(result is DataResult.Success)
        val asset = (result as DataResult.Success).data
        assertEquals("media-1", asset.mediaAssetId)
        assertEquals(MediaPurpose.USER_AVATAR, asset.purpose)
        assertEquals(MediaStatus.READY, asset.status)
        assertEquals(MediaPurpose.USER_AVATAR.name, api.lastUploadPurpose)
    }

    @Test
    fun `returns local validation error without calling backend`() = runBlocking {
        val api = FakeMediaApi()
        val repository =
            createRepository(
                api = api,
                partFactory = MediaPartFactory { throw MediaPreparationException(AppError.ImageTooLarge) },
            )

        val result = repository.uploadImage("content://image/large", MediaPurpose.TOUR_COVER)

        assertTrue(result is DataResult.Error)
        assertSame(AppError.ImageTooLarge, (result as DataResult.Error).error)
        assertEquals(0, api.uploadCallCount)
    }

    @Test
    fun `does not expose media before backend marks it ready`() = runBlocking {
        val repository = createRepository(FakeMediaApi(uploadStatus = "PENDING"))

        val result = repository.uploadImage("content://image/1", MediaPurpose.USER_AVATAR)

        assertTrue(result is DataResult.Error)
        assertSame(AppError.Unknown, (result as DataResult.Error).error)
    }

    @Test
    fun `maps media in use error when backend rejects deletion`() = runBlocking {
        val api = FakeMediaApi(deleteShouldFail = true)
        val repository = createRepository(api)

        val result = repository.deleteUnreferenced("media-1")

        assertTrue(result is DataResult.Error)
        val error = (result as DataResult.Error).error as AppError.Backend
        assertEquals(BackendErrorCode.MEDIA_IN_USE, error.code)
    }

    private fun createRepository(
        api: MediaApi,
        partFactory: MediaPartFactory =
            MediaPartFactory {
                MultipartBody.Part.createFormData(
                    "file",
                    "image.jpg",
                    byteArrayOf(0xFF.toByte(), 0xD8.toByte(), 0xFF.toByte())
                        .toRequestBody("image/jpeg".toMediaType()),
                )
            },
    ): MediaRepositoryImpl =
        MediaRepositoryImpl(
            api = api,
            multipartFactory = partFactory,
            apiErrorParser = ApiErrorParser(Gson()),
            networkExceptionMapper = NetworkExceptionMapper(),
        )

    private class FakeMediaApi(
        private val deleteShouldFail: Boolean = false,
        private val uploadStatus: String = "READY",
    ) : MediaApi {
        var uploadCallCount = 0
        var lastUploadPurpose: String? = null

        override suspend fun upload(
            file: MultipartBody.Part,
            purpose: String,
        ): Response<MediaUploadResponse> {
            uploadCallCount++
            lastUploadPurpose = purpose
            return Response.success(
                MediaUploadResponse(
                    mediaAssetId = "media-1",
                    purpose = purpose,
                    status = uploadStatus,
                    imageUrl = "http://localhost:8080/api/v1/media/media-1/content",
                    contentType = "image/jpeg",
                    sizeBytes = 3L,
                ),
            )
        }

        override suspend fun delete(mediaAssetId: String): Response<MediaDeletionResponse> =
            if (deleteShouldFail) {
                Response.error(
                    409,
                    """{"code":"MEDIA_IN_USE","message":"in use"}"""
                        .toResponseBody("application/json".toMediaType()),
                )
            } else {
                Response.success(
                    MediaDeletionResponse(
                        mediaAssetId = mediaAssetId,
                        status = "DELETED",
                    ),
                )
            }
    }
}
