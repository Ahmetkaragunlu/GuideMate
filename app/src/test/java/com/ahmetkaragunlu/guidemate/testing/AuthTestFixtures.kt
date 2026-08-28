package com.ahmetkaragunlu.guidemate.testing

import com.ahmetkaragunlu.guidemate.auth.domain.model.UserRole
import com.ahmetkaragunlu.guidemate.auth.domain.model.UserState
import com.ahmetkaragunlu.guidemate.auth.domain.repository.AuthRepository
import com.ahmetkaragunlu.guidemate.auth.domain.repository.OnboardingRepository
import com.ahmetkaragunlu.guidemate.auth.domain.repository.UserRepository
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import com.ahmetkaragunlu.guidemate.common.ui.resource.ResourceProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeAuthRepository : AuthRepository {
    var loginResult: DataResult<UserState> = DataResult.Success(authenticatedUser())
    var registerResult: DataResult<Unit> = DataResult.Success(Unit)
    var googleLoginResult: DataResult<UserState> = DataResult.Success(authenticatedUser())
    var logoutResult: DataResult<Unit> = DataResult.Success(Unit)
    var selectRoleResult: DataResult<UserState> = DataResult.Success(authenticatedUser())
    var currentUserResult: DataResult<UserState> = DataResult.Success(authenticatedUser())
    var resendResult: DataResult<Unit> = DataResult.Success(Unit)
    var forgotPasswordResult: DataResult<Unit> = DataResult.Success(Unit)
    var changePasswordResult: DataResult<Unit> = DataResult.Success(Unit)
    var storedSession = false
    var loginRequest: Pair<String, String>? = null
    var registerRequest: List<String>? = null
    var selectedRole: UserRole? = null
    var forgotPasswordEmail: String? = null
    var changePasswordRequest: Pair<String, String>? = null
    var clearLocalSessionCalls = 0
    var logoutCalls = 0

    override suspend fun login(email: String, password: String): DataResult<UserState> {
        loginRequest = email to password
        return loginResult
    }

    override suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
    ): DataResult<Unit> {
        registerRequest = listOf(firstName, lastName, email, password)
        return registerResult
    }

    override suspend fun googleLogin(idToken: String): DataResult<UserState> = googleLoginResult

    override suspend fun logout(): DataResult<Unit> {
        logoutCalls++
        return logoutResult
    }

    override suspend fun selectRole(role: UserRole): DataResult<UserState> {
        selectedRole = role
        return selectRoleResult
    }

    override suspend fun currentUser(): DataResult<UserState> = currentUserResult

    override suspend fun resendVerification(email: String): DataResult<Unit> = resendResult

    override suspend fun forgotPassword(email: String): DataResult<Unit> {
        forgotPasswordEmail = email
        return forgotPasswordResult
    }

    override suspend fun changePassword(
        currentPassword: String,
        newPassword: String,
    ): DataResult<Unit> {
        changePasswordRequest = currentPassword to newPassword
        return changePasswordResult
    }

    override fun hasStoredSession(): Boolean = storedSession

    override suspend fun clearLocalSession() {
        clearLocalSessionCalls++
    }
}

class FakeUserRepository(initialState: UserState = UserState()) : UserRepository {
    val state = MutableStateFlow(initialState)
    var restoredUser: UserState = initialState
    override val userState: StateFlow<UserState> = state

    override suspend fun restoreCachedUser(): UserState = restoredUser

    override suspend fun updateAvatar(mediaAssetId: String, imageUrl: String) {
        state.value = state.value.copy(avatarMediaId = mediaAssetId, avatarUrl = imageUrl)
    }
}

class FakeOnboardingRepository(
    var completed: Boolean = true,
) : OnboardingRepository {
    var completeCalls = 0

    override suspend fun isCompleted(): Boolean = completed

    override suspend fun complete() {
        completed = true
        completeCalls++
    }
}

class FakeResourceProvider : ResourceProvider {
    override fun getString(id: Int): String = "string-$id"

    override fun getString(id: Int, vararg args: Any): String = "string-$id"

    override fun getQuantityString(id: Int, quantity: Int, vararg args: Any): String =
        "quantity-$id-$quantity"
}

fun authenticatedUser(
    role: UserRole = UserRole.TOURIST,
    isRoleSelected: Boolean = true,
): UserState =
    UserState(
        userId = 7,
        email = "user@example.com",
        firstName = "Ada",
        lastName = "Lovelace",
        isRoleSelected = isRoleSelected,
        role = role,
    )
