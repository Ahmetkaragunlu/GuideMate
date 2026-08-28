package com.ahmetkaragunlu.guidemate.profile.data.repository

import com.ahmetkaragunlu.guidemate.auth.domain.repository.UserRepository
import com.ahmetkaragunlu.guidemate.common.network.ApiCallExecutor
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.media.data.mapper.toDomain
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaPurpose
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaReference
import com.ahmetkaragunlu.guidemate.media.domain.repository.MediaRepository
import com.ahmetkaragunlu.guidemate.profile.data.remote.api.UserAvatarApi
import com.ahmetkaragunlu.guidemate.profile.data.remote.model.UpdateUserAvatarRequestDto
import com.ahmetkaragunlu.guidemate.profile.domain.repository.UserAvatarRepository
import javax.inject.Inject

class UserAvatarRepositoryImpl @Inject constructor(
    private val api: UserAvatarApi,
    private val mediaRepository: MediaRepository,
    private val userRepository: UserRepository,
    private val apiCallExecutor: ApiCallExecutor,
) : UserAvatarRepository {
    override suspend fun updateAvatar(localUri: String): DataResult<MediaReference> {
        val upload = mediaRepository.uploadImage(localUri, MediaPurpose.USER_AVATAR)
        if (upload is DataResult.Error) return upload

        val uploadedMedia = (upload as DataResult.Success).data
        val update =
            apiCallExecutor.execute(
                request = {
                    api.updateAvatar(UpdateUserAvatarRequestDto(uploadedMedia.mediaAssetId))
                },
                transform = { it.toDomain() },
            )
        if (update is DataResult.Error) {
            mediaRepository.deleteUnreferenced(uploadedMedia.mediaAssetId)
            return update
        }

        val avatar = (update as DataResult.Success).data
        userRepository.updateAvatar(avatar.mediaAssetId, avatar.imageUrl)
        return update
    }
}
