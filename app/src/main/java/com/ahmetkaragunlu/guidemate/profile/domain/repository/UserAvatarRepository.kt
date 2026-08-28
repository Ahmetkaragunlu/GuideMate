package com.ahmetkaragunlu.guidemate.profile.domain.repository

import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaReference

interface UserAvatarRepository {
    suspend fun updateAvatar(localUri: String): DataResult<MediaReference>
}
