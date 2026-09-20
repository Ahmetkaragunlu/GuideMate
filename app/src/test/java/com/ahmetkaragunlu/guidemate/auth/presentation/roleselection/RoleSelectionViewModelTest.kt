package com.ahmetkaragunlu.guidemate.auth.presentation.roleselection

import com.ahmetkaragunlu.guidemate.auth.domain.model.UserRole
import com.ahmetkaragunlu.guidemate.common.coroutines.MainDispatcherRule
import com.ahmetkaragunlu.guidemate.testing.auth.FakeAuthRepository
import com.ahmetkaragunlu.guidemate.testing.common.FakeResourceProvider
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RoleSelectionViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `role is required and selected role is sent once`() = runTest(mainDispatcherRule.dispatcher) {
        val repository = FakeAuthRepository()
        val viewModel = RoleSelectionViewModel(repository, FakeResourceProvider())

        viewModel.confirmRoleSelection()
        assertNull(repository.calls.selectedRole)
        assertTrue(viewModel.screenState.value.errorMessage != null)

        viewModel.selectRole(UserRole.GUIDE)
        viewModel.confirmRoleSelection()
        runCurrent()

        assertEquals(UserRole.GUIDE, repository.calls.selectedRole)
    }
}
