package com.ahmetkaragunlu.guidemate.profile.presentation.guide

import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.testing.profile.FakeGuideProfileRepository
import com.ahmetkaragunlu.guidemate.testing.media.FakeMediaRepository
import com.ahmetkaragunlu.guidemate.testing.notification.FakeNotificationRepository
import com.ahmetkaragunlu.guidemate.testing.common.FakeResourceProvider
import com.ahmetkaragunlu.guidemate.testing.discovery.FakeTourDiscoveryRepository
import com.ahmetkaragunlu.guidemate.testing.profile.FakeUserAvatarRepository
import com.ahmetkaragunlu.guidemate.testing.auth.FakeUserRepository
import com.ahmetkaragunlu.guidemate.profile.domain.usecase.UpdateUserAvatarUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class GuideProfileViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun selectedAvatarIsUpdatedThroughSharedUserAvatarRepository() =
        runTest {
            val profileRepository = FakeGuideProfileRepository()
            val userAvatarRepository = FakeUserAvatarRepository()
            val mediaRepository = FakeMediaRepository()
            val viewModel =
                GuideProfileViewModel(
                    profileRepository = profileRepository,
                    updateUserAvatar =
                        UpdateUserAvatarUseCase(
                            mediaRepository = mediaRepository,
                            userAvatarRepository = userAvatarRepository,
                            userRepository = FakeUserRepository(),
                        ),
                    resourceProvider = FakeResourceProvider(),
                    tourRepository = FakeTourDiscoveryRepository(),
                    notificationRepository = FakeNotificationRepository(),
                )
            val collection = backgroundScope.launch { viewModel.profileState.collect {} }
            runCurrent()

            viewModel.onProfileImageSelected("content://avatar")
            runCurrent()

            assertEquals("content://avatar", mediaRepository.uploadedUri)
            assertEquals("media-1", userAvatarRepository.selectedMediaAssetId)
            assertFalse(viewModel.profileState.value.isAvatarUpdating)
            assertNull(viewModel.profileState.value.selectedProfileImageUri)
            collection.cancel()
        }

    @Test
    fun cachedProfileLoadsPopularToursOnceAfterSuccessfulRefresh() =
        runTest {
            val profileRepository = FakeGuideProfileRepository()
            val tourRepository = FakeTourDiscoveryRepository()

            GuideProfileViewModel(
                profileRepository = profileRepository,
                updateUserAvatar =
                    UpdateUserAvatarUseCase(
                        mediaRepository = FakeMediaRepository(),
                        userAvatarRepository = FakeUserAvatarRepository(),
                        userRepository = FakeUserRepository(),
                    ),
                resourceProvider = FakeResourceProvider(),
                tourRepository = tourRepository,
                notificationRepository = FakeNotificationRepository(),
            )
            runCurrent()

            assertEquals(1, profileRepository.calls.refreshOwnProfile)
            assertEquals(1, tourRepository.calls.popularForGuideRequests.size)
        }
}
