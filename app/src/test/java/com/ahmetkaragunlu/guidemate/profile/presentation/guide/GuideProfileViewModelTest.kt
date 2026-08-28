package com.ahmetkaragunlu.guidemate.profile.presentation.guide

import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.testing.FakeGuideProfileRepository
import com.ahmetkaragunlu.guidemate.testing.FakeNotificationRepository
import com.ahmetkaragunlu.guidemate.testing.FakeResourceProvider
import com.ahmetkaragunlu.guidemate.testing.FakeTourDiscoveryRepository
import com.ahmetkaragunlu.guidemate.testing.FakeUserAvatarRepository
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
            val viewModel =
                GuideProfileViewModel(
                    profileRepository = profileRepository,
                    userAvatarRepository = userAvatarRepository,
                    resourceProvider = FakeResourceProvider(),
                    tourRepository = FakeTourDiscoveryRepository(),
                    notificationRepository = FakeNotificationRepository(),
                )
            val collection = backgroundScope.launch { viewModel.profileState.collect {} }
            runCurrent()

            viewModel.onProfileImageSelected("content://avatar")
            runCurrent()

            assertEquals("content://avatar", userAvatarRepository.selectedUri)
            assertFalse(viewModel.profileState.value.isAvatarUpdating)
            assertNull(viewModel.profileState.value.selectedProfileImageUri)
            collection.cancel()
        }
}
