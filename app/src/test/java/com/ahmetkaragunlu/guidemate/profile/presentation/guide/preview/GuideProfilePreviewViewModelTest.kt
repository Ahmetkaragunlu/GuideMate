package com.ahmetkaragunlu.guidemate.profile.presentation.guide.preview

import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.testing.profile.FakeGuideProfileRepository
import com.ahmetkaragunlu.guidemate.testing.discovery.FakeTourDiscoveryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GuideProfilePreviewViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun cachedProfileLoadsPopularToursOnceWhenProfileRefreshFails() =
        runTest {
            val profileRepository =
                FakeGuideProfileRepository().apply {
                    results.refreshOwnProfile = DataResult.Error(AppError.NoInternet)
                }
            val tourRepository = FakeTourDiscoveryRepository()

            GuideProfilePreviewViewModel(
                profileRepository = profileRepository,
                tourRepository = tourRepository,
            )
            runCurrent()

            assertEquals(1, profileRepository.calls.refreshOwnProfile)
            assertEquals(1, tourRepository.calls.popularForGuideRequests.size)
        }
}
