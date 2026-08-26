package com.ahmetkaragunlu.guidemate.auth.presentation.signin

import com.ahmetkaragunlu.guidemate.auth.domain.validation.EmailPolicy
import com.ahmetkaragunlu.guidemate.auth.domain.validation.NumericPasswordPolicy
import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.common.result.AppError
import com.ahmetkaragunlu.guidemate.common.result.BackendErrorCode
import com.ahmetkaragunlu.guidemate.common.result.DataResult
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

        assertEquals("User@Example.com" to "12345678", repository.loginRequest)
        assertFalse(viewModel.screenState.value.isLoading)
    }

    @Test
    fun `pending verification opens dialog with normalized email`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository =
                FakeAuthRepository().apply {
                    loginResult =
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

    private fun createViewModel(repository: FakeAuthRepository): SignInViewModel =
        SignInViewModel(
            authRepository = repository,
            emailPolicy = EmailPolicy(),
            passwordPolicy = NumericPasswordPolicy(),
            resourceProvider = FakeResourceProvider(),
        )
}
