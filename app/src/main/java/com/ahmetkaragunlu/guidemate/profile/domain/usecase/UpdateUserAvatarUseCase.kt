package com.ahmetkaragunlu.guidemate.profile.domain.usecase

import com.ahmetkaragunlu.guidemate.auth.domain.repository.UserRepository
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaPurpose
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaReference
import com.ahmetkaragunlu.guidemate.media.domain.repository.MediaRepository
import com.ahmetkaragunlu.guidemate.profile.domain.repository.UserAvatarRepository
import javax.inject.Inject
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext

class UpdateUserAvatarUseCase @Inject constructor(
    private val mediaRepository: MediaRepository,
    private val userAvatarRepository: UserAvatarRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(localUri: String): DataResult<MediaReference> {
        val upload = mediaRepository.uploadImage(localUri, MediaPurpose.USER_AVATAR)
        if (upload is DataResult.Error) return upload

        val uploadedMedia = (upload as DataResult.Success).data
        var isAttached = false
        return try {
            when (val update = userAvatarRepository.updateAvatar(uploadedMedia.mediaAssetId)) {
                is DataResult.Error -> update
                is DataResult.Success -> {
                    isAttached = true
                    userRepository.updateAvatar(update.data.mediaAssetId, update.data.imageUrl)
                    update
                }
            }
        } finally {
            if (!isAttached) {
                withContext(NonCancellable) {
                    mediaRepository.deleteUnreferenced(uploadedMedia.mediaAssetId)
                }
            }
        }
    }
}
