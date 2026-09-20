package com.ahmetkaragunlu.guidemate.auth.presentation.forgotpassword

import com.ahmetkaragunlu.guidemate.auth.domain.validation.EmailPolicy
import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.BackendErrorCode
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.testing.auth.FakeAuthRepository
import com.ahmetkaragunlu.guidemate.testing.common.FakeResourceProvider
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ForgotPasswordViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `successful request sends email and opens confirmation dialog`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository = FakeAuthRepository()
            val viewModel =
                ForgotPasswordViewModel(repository, EmailPolicy(), FakeResourceProvider())
            viewModel.onEmailChange("user@example.com")

            viewModel.onSubmitClick()
            runCurrent()

            assertEquals("user@example.com", repository.calls.forgotPasswordEmail)
            assertFalse(viewModel.screenState.value.isLoading)
            assertTrue(viewModel.screenState.value.showSuccessDialog)
        }

    @Test
    fun `rate limit blocks another request until countdown finishes`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository =
                FakeAuthRepository().apply {
                    results.forgotPassword =
                        DataResult.Error(
                            AppError.Backend(
                                code = BackendErrorCode.RATE_LIMITED,
                                fallbackMessage = null,
                                retryAfterSeconds = 2,
                            ),
                        )
                }
            val viewModel =
                ForgotPasswordViewModel(repository, EmailPolicy(), FakeResourceProvider())
            viewModel.onEmailChange("user@example.com")

            viewModel.onSubmitClick()
            runCurrent()
            assertEquals(2, viewModel.screenState.value.retryAfterSeconds)

            repository.calls.forgotPasswordEmail = null
            viewModel.onSubmitClick()
            assertEquals(null, repository.calls.forgotPasswordEmail)

            advanceTimeBy(2_000)
            runCurrent()
            assertEquals(0, viewModel.screenState.value.retryAfterSeconds)
            assertTrue(viewModel.screenState.value.errorMessage != null)
        }
}
