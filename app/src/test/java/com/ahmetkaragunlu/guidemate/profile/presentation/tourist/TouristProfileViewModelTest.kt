package com.ahmetkaragunlu.guidemate.profile.presentation.tourist

import com.ahmetkaragunlu.guidemate.auth.domain.model.UserRole
import com.ahmetkaragunlu.guidemate.auth.domain.model.UserState
import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.testing.FakeResourceProvider
import com.ahmetkaragunlu.guidemate.testing.FakeMediaRepository
import com.ahmetkaragunlu.guidemate.testing.FakeUserAvatarRepository
import com.ahmetkaragunlu.guidemate.testing.FakeUserRepository
import com.ahmetkaragunlu.guidemate.testing.FakeWalletRepository
import com.ahmetkaragunlu.guidemate.wallet.domain.model.WalletAccount
import com.ahmetkaragunlu.guidemate.profile.domain.usecase.UpdateUserAvatarUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TouristProfileViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun selectedAvatarUsesSharedRepositoryAndUpdatesProfileState() =
        runTest {
            val userRepository =
                FakeUserRepository(
                    UserState(
                        userId = 7,
                        email = "tourist@example.com",
                        firstName = "Ada",
                        lastName = "Lovelace",
                        isRoleSelected = true,
                        role = UserRole.TOURIST,
                        avatarMediaId = "old-avatar",
                        avatarUrl = "https://example.com/old-avatar.jpg",
                    ),
                )
            val avatarRepository = FakeUserAvatarRepository()
            val mediaRepository = FakeMediaRepository()
            val viewModel =
                TouristProfileViewModel(
                    userRepository = userRepository,
                    walletRepository = FakeWalletRepository(),
                    updateUserAvatar =
                        UpdateUserAvatarUseCase(
                            mediaRepository = mediaRepository,
                            userAvatarRepository = avatarRepository,
                            userRepository = userRepository,
                        ),
                    resourceProvider = FakeResourceProvider(),
                )
            runCurrent()

            assertEquals("https://example.com/old-avatar.jpg", viewModel.uiState.value.avatarUrl)

            viewModel.onProfileImageSelected("content://new-avatar")
            runCurrent()

            assertEquals("content://new-avatar", mediaRepository.uploadedUri)
            assertEquals("media-1", avatarRepository.selectedMediaAssetId)
            assertEquals("https://example.com/avatar.jpg", viewModel.uiState.value.avatarUrl)
            assertFalse(viewModel.uiState.value.isAvatarUpdating)
            assertNull(viewModel.uiState.value.selectedAvatarUri)
        }

    @Test
    fun canonicalWalletUpdateRefreshesProfileBalance() =
        runTest {
            val walletRepository = FakeWalletRepository()
            val viewModel =
                TouristProfileViewModel(
                    userRepository = FakeUserRepository(),
                    walletRepository = walletRepository,
                    updateUserAvatar =
                        UpdateUserAvatarUseCase(
                            mediaRepository = FakeMediaRepository(),
                            userAvatarRepository = FakeUserAvatarRepository(),
                            userRepository = FakeUserRepository(),
                        ),
                    resourceProvider = FakeResourceProvider(),
                )
            runCurrent()

            walletRepository.publishWallet(WalletAccount(72_500, 72_500, "USD"))
            runCurrent()

            assertEquals(72_500L, viewModel.uiState.value.balanceMinor)
        }
}
