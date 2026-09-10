package com.ahmetkaragunlu.guidemate.auth.presentation.signup

import com.ahmetkaragunlu.guidemate.auth.domain.validation.EmailPolicy
import com.ahmetkaragunlu.guidemate.auth.domain.validation.NumericPasswordPolicy
import com.ahmetkaragunlu.guidemate.auth.domain.validation.PersonalNamePolicy
import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.testing.FakeAuthRepository
import com.ahmetkaragunlu.guidemate.testing.FakeResourceProvider
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SignUpViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `valid form requires accepted terms before registration`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository = FakeAuthRepository()
            val viewModel = createViewModel(repository)
            fillValidForm(viewModel)

            viewModel.onSignUpClick()
            assertNull(repository.registerRequest)

            viewModel.markTermsAsRead()
            viewModel.acceptTerms()
            viewModel.onSignUpClick()
            runCurrent()

            assertEquals(
                listOf("Ada", "Lovelace", "ada@example.com", "12345678"),
                repository.registerRequest,
            )
            assertTrue(viewModel.screenState.value.isRegistrationSuccess)
        }

    @Test
    fun `terms can only be accepted after reading and can be declined later`() {
        val viewModel = createViewModel(FakeAuthRepository())

        viewModel.onTermsCheckboxClicked()
        assertTrue(viewModel.screenState.value.showTermsSheet)

        viewModel.acceptTerms()
        assertTrue(!viewModel.screenState.value.isTermsAccepted)

        viewModel.markTermsAsRead()
        viewModel.acceptTerms()
        assertTrue(viewModel.screenState.value.isTermsAccepted)
        assertTrue(!viewModel.screenState.value.showTermsSheet)

        viewModel.onTermsCheckboxClicked()
        assertTrue(!viewModel.screenState.value.isTermsAccepted)
    }

    private fun createViewModel(repository: FakeAuthRepository): SignUpViewModel =
        SignUpViewModel(
            authRepository = repository,
            emailPolicy = EmailPolicy(),
            namePolicy = PersonalNamePolicy(),
            passwordPolicy = NumericPasswordPolicy(),
            resourceProvider = FakeResourceProvider(),
        )

    private fun fillValidForm(viewModel: SignUpViewModel) {
        viewModel.inputFirstNameChange("Ada")
        viewModel.onLastNameChange("Lovelace")
        viewModel.onEmailChange("ada@example.com")
        viewModel.onPasswordChange("12345678")
        viewModel.onConfirmPasswordChange("12345678")
    }
}
