package com.ahmetkaragunlu.guidemate.profile.domain.usecase

import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.media.domain.model.MediaPurpose
import com.ahmetkaragunlu.guidemate.testing.FakeMediaRepository
import com.ahmetkaragunlu.guidemate.testing.FakeUserAvatarRepository
import com.ahmetkaragunlu.guidemate.testing.FakeUserRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class UpdateUserAvatarUseCaseTest {
    @Test
    fun `successful update uploads attaches and refreshes cached identity`() = runTest {
        val mediaRepository = FakeMediaRepository()
        val avatarRepository = FakeUserAvatarRepository()
        val userRepository = FakeUserRepository()
        val useCase = UpdateUserAvatarUseCase(mediaRepository, avatarRepository, userRepository)

        val result = useCase("content://avatar")

        assertTrue(result is DataResult.Success)
        assertEquals("content://avatar", mediaRepository.uploadedUri)
        assertEquals(MediaPurpose.USER_AVATAR, mediaRepository.uploadedPurpose)
        assertEquals("media-1", avatarRepository.selectedMediaAssetId)
        assertEquals("media-1", userRepository.userState.value.avatarMediaId)
        assertTrue(mediaRepository.deletedMediaIds.isEmpty())
    }

    @Test
    fun `failed attachment deletes uploaded media`() = runTest {
        val mediaRepository = FakeMediaRepository()
        val avatarRepository =
            FakeUserAvatarRepository().apply {
                updateResult = DataResult.Error(AppError.GenericFailure)
            }
        val useCase =
            UpdateUserAvatarUseCase(
                mediaRepository,
                avatarRepository,
                FakeUserRepository(),
            )

        assertTrue(useCase("content://avatar") is DataResult.Error)
        assertEquals(listOf("media-1"), mediaRepository.deletedMediaIds)
    }

    @Test
    fun `failed upload does not call avatar repository`() = runTest {
        val mediaRepository =
            FakeMediaRepository().apply {
                uploadResult = DataResult.Error(AppError.ImageUnavailable)
            }
        val avatarRepository = FakeUserAvatarRepository()
        val useCase =
            UpdateUserAvatarUseCase(
                mediaRepository,
                avatarRepository,
                FakeUserRepository(),
            )

        assertTrue(useCase("content://avatar") is DataResult.Error)
        assertEquals(null, avatarRepository.selectedMediaAssetId)
    }
}
