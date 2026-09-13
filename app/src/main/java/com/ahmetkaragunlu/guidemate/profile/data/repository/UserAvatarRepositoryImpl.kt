package com.ahmetkaragunlu.guidemate.profile.data.repository

import com.ahmetkaragunlu.guidemate.common.network.ApiCallExecutor
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.media.data.mapper.toDomain
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaReference
import com.ahmetkaragunlu.guidemate.profile.data.remote.api.UserAvatarApi
import com.ahmetkaragunlu.guidemate.profile.data.remote.model.UpdateUserAvatarRequestDto
import com.ahmetkaragunlu.guidemate.profile.domain.repository.UserAvatarRepository
import javax.inject.Inject

class UserAvatarRepositoryImpl @Inject constructor(
    private val api: UserAvatarApi,
    private val apiCallExecutor: ApiCallExecutor,
) : UserAvatarRepository {
    override suspend fun updateAvatar(mediaAssetId: String): DataResult<MediaReference> =
        apiCallExecutor.execute(
            request = { api.updateAvatar(UpdateUserAvatarRequestDto(mediaAssetId)) },
            transform = { it.toDomain() },
        )
}
