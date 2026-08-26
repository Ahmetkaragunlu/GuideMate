package com.ahmetkaragunlu.guidemate.auth.presentation.forgotpassword

import com.ahmetkaragunlu.guidemate.auth.domain.validation.EmailPolicy
import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.BackendErrorCode
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.testing.FakeAuthRepository
import com.ahmetkaragunlu.guidemate.testing.FakeResourceProvider
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ForgotPasswordViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `rate limit blocks another request until countdown finishes`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository =
                FakeAuthRepository().apply {
                    forgotPasswordResult =
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

            repository.forgotPasswordEmail = null
            viewModel.onSubmitClick()
            assertEquals(null, repository.forgotPasswordEmail)

            advanceTimeBy(2_000)
            runCurrent()
            assertEquals(0, viewModel.screenState.value.retryAfterSeconds)
            assertTrue(viewModel.screenState.value.errorMessage != null)
        }
}
