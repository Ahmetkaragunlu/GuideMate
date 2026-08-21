package com.ahmetkaragunlu.guidemate.media.data.repository

import com.ahmetkaragunlu.guidemate.common.network.error.ApiErrorParser
import com.ahmetkaragunlu.guidemate.common.network.error.NetworkExceptionMapper
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.media.data.mapper.toDomain
import com.ahmetkaragunlu.guidemate.media.data.multipart.MediaPartFactory
import com.ahmetkaragunlu.guidemate.media.data.multipart.MediaPreparationException
import com.ahmetkaragunlu.guidemate.media.data.remote.api.MediaApi
import com.ahmetkaragunlu.guidemate.media.data.remote.model.MediaDeletionResponse
import com.ahmetkaragunlu.guidemate.media.data.remote.model.MediaUploadResponse
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaAsset
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaPurpose
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaStatus
import com.ahmetkaragunlu.guidemate.media.domain.repository.MediaRepository
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import retrofit2.Response

class MediaRepositoryImpl @Inject constructor(
    private val api: MediaApi,
    private val multipartFactory: MediaPartFactory,
    private val apiErrorParser: ApiErrorParser,
    private val networkExceptionMapper: NetworkExceptionMapper,
) : MediaRepository {
    override suspend fun uploadImage(
        localUri: String,
        purpose: MediaPurpose,
    ): DataResult<MediaAsset> {
        val filePart =
            try {
                multipartFactory.create(localUri)
            } catch (exception: MediaPreparationException) {
                return DataResult.Error(exception.error, exception)
            }

        return execute {
            api.upload(file = filePart, purpose = purpose.name).toMediaResult(purpose)
        }
    }

    override suspend fun deleteUnreferenced(mediaAssetId: String): DataResult<Unit> =
        execute { api.delete(mediaAssetId).toDeletionResult() }

    private fun Response<MediaUploadResponse>.toMediaResult(
        expectedPurpose: MediaPurpose,
    ): DataResult<MediaAsset> {
        if (!isSuccessful) return DataResult.Error(apiErrorParser.parse(this))
        val response = body() ?: return DataResult.Error(AppError.NoResponseFromServer)
        val mediaAsset = response.toDomain()
        return if (mediaAsset.purpose == expectedPurpose && mediaAsset.status == MediaStatus.READY) {
            DataResult.Success(mediaAsset)
        } else {
            DataResult.Error(AppError.Unknown)
        }
    }

    private fun Response<MediaDeletionResponse>.toDeletionResult(): DataResult<Unit> {
        if (!isSuccessful) return DataResult.Error(apiErrorParser.parse(this))
        val response = body() ?: return DataResult.Error(AppError.NoResponseFromServer)
        return if (MediaStatus.valueOf(response.status) == MediaStatus.DELETED) {
            DataResult.Success(Unit)
        } else {
            DataResult.Error(AppError.Unknown)
        }
    }

    private suspend fun <T> execute(block: suspend () -> DataResult<T>): DataResult<T> =
        try {
            block()
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            DataResult.Error(networkExceptionMapper.map(exception), exception)
        }
}
