package com.ahmetkaragunlu.guidemate.auth.presentation.signin

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.auth.domain.validation.EmailPolicy
import com.ahmetkaragunlu.guidemate.auth.domain.validation.NumericPasswordPolicy
import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.BackendErrorCode
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.result.AppFieldError
import com.ahmetkaragunlu.guidemate.testing.auth.FakeAuthRepository
import com.ahmetkaragunlu.guidemate.testing.auth.LoginCall
import com.ahmetkaragunlu.guidemate.testing.common.FakeResourceProvider
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SignInViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `valid credentials are sanitized and sent once`() = runTest(mainDispatcherRule.dispatcher) {
        val repository = FakeAuthRepository()
        val viewModel = createViewModel(repository)

        viewModel.onEmailChange("User@Example.com")
        viewModel.onPasswordChange("12ab345678")
        viewModel.onSignInClick()
        runCurrent()

        assertEquals(
            LoginCall(email = "User@Example.com", password = "12345678"),
            repository.calls.login,
        )
        assertFalse(viewModel.screenState.value.isLoading)
    }

    @Test
    fun `pending verification opens dialog with normalized email`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository =
                FakeAuthRepository().apply {
                    results.login =
                        DataResult.Error(
                            AppError.Backend(
                                code = BackendErrorCode.ACCOUNT_PENDING_VERIFICATION,
                                fallbackMessage = null,
                            ),
                        )
                }
            val viewModel = createViewModel(repository)

            viewModel.onEmailChange(" User@Example.com ")
            viewModel.onPasswordChange("12345678")
            viewModel.onSignInClick()
            runCurrent()

            assertTrue(viewModel.screenState.value.showVerificationDialog)
            assertEquals("user@example.com", viewModel.screenState.value.verificationEmail)
        }

    @Test
    fun `rate limit blocks another login until cooldown finishes`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository =
                FakeAuthRepository().apply {
                    results.login =
                        DataResult.Error(
                            AppError.Backend(
                                code = BackendErrorCode.RATE_LIMITED,
                                fallbackMessage = null,
                                retryAfterSeconds = 2,
                            )
                        )
                }
            val viewModel = createViewModel(repository)
            viewModel.onEmailChange("user@example.com")
            viewModel.onPasswordChange("12345678")

            viewModel.onSignInClick()
            runCurrent()
            assertEquals(2, viewModel.screenState.value.loginRetryAfterSeconds)

            repository.calls.login = null
            viewModel.onSignInClick()
            runCurrent()
            assertNull(repository.calls.login)

            advanceTimeBy(2_000)
            runCurrent()
            assertEquals(0, viewModel.screenState.value.loginRetryAfterSeconds)
        }

    @Test
    fun `backend field errors are assigned to matching form fields`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository =
                FakeAuthRepository().apply {
                    results.login =
                        DataResult.Error(
                            AppError.Backend(
                                code = BackendErrorCode.VALIDATION_FAILED,
                                fallbackMessage = null,
                                fieldErrors =
                                    listOf(
                                        AppFieldError("email", "INVALID_EMAIL", null),
                                        AppFieldError("password", "INVALID_PASSWORD", null),
                                    ),
                            )
                        )
                }
            val viewModel = createViewModel(repository)
            viewModel.onEmailChange("user@example.com")
            viewModel.onPasswordChange("12345678")

            viewModel.onSignInClick()
            runCurrent()

            assertEquals(
                "string-${R.string.email_error_message}",
                viewModel.screenState.value.emailErrorMessage,
            )
            assertEquals(
                "string-${R.string.error_invalid_field}",
                viewModel.screenState.value.passwordErrorMessage,
            )
        }

    private fun createViewModel(repository: FakeAuthRepository): SignInViewModel =
        SignInViewModel(
            authRepository = repository,
            emailPolicy = EmailPolicy(),
            passwordPolicy = NumericPasswordPolicy(),
            resourceProvider = FakeResourceProvider(),
        )
}
