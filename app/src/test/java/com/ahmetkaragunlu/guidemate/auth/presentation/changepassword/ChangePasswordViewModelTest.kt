package com.ahmetkaragunlu.guidemate.auth.presentation.changepassword

import com.ahmetkaragunlu.guidemate.auth.domain.validation.NumericPasswordPolicy
import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.testing.FakeAuthRepository
import com.ahmetkaragunlu.guidemate.testing.FakeResourceProvider
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChangePasswordViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `successful change clears form and confirmation clears local session`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository = FakeAuthRepository()
            val viewModel =
                ChangePasswordViewModel(
                    repository,
                    NumericPasswordPolicy(),
                    FakeResourceProvider(),
                )
            viewModel.onCurrentPasswordChange("12345678")
            viewModel.onNewPasswordChange("87654321")
            viewModel.onConfirmNewPasswordChange("87654321")

            viewModel.onChangePasswordClick()
            runCurrent()

            assertEquals("12345678" to "87654321", repository.changePasswordRequest)
            assertTrue(viewModel.screenState.value.showSuccessDialog)
            assertTrue(viewModel.formState.value.currentPassword.isEmpty())

            viewModel.confirmSuccess()
            runCurrent()

            assertFalse(viewModel.screenState.value.showSuccessDialog)
            assertEquals(1, repository.clearLocalSessionCalls)
        }
}
