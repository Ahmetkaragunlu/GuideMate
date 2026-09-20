package com.ahmetkaragunlu.guidemate.testing.auth

import com.ahmetkaragunlu.guidemate.auth.domain.model.UserRole
import com.ahmetkaragunlu.guidemate.auth.domain.model.UserState
import com.ahmetkaragunlu.guidemate.auth.domain.repository.AuthRepository
import com.ahmetkaragunlu.guidemate.auth.domain.repository.OnboardingRepository
import com.ahmetkaragunlu.guidemate.auth.domain.repository.UserRepository
import com.ahmetkaragunlu.guidemate.common.result.DataResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class RegisterCall(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
)

data class LoginCall(
    val email: String,
    val password: String,
)

data class ChangePasswordCall(
    val currentPassword: String,
    val newPassword: String,
)

class AuthFakeResults {
    var login: DataResult<UserState> = DataResult.Success(authenticatedUser())
    var register: DataResult<Unit> = DataResult.Success(Unit)
    var googleLogin: DataResult<UserState> = DataResult.Success(authenticatedUser())
    var logout: DataResult<Unit> = DataResult.Success(Unit)
    var selectRole: DataResult<UserState> = DataResult.Success(authenticatedUser())
    var currentUser: DataResult<UserState> = DataResult.Success(authenticatedUser())
    var resendVerification: DataResult<Unit> = DataResult.Success(Unit)
    var forgotPassword: DataResult<Unit> = DataResult.Success(Unit)
    var changePassword: DataResult<Unit> = DataResult.Success(Unit)
}

class AuthFakeState {
    var hasStoredSession = false
}

class AuthFakeCalls {
    var login: LoginCall? = null
    var register: RegisterCall? = null
    var selectedRole: UserRole? = null
    var forgotPasswordEmail: String? = null
    var changePassword: ChangePasswordCall? = null
    var clearLocalSession = 0
    var logout = 0
}

class AuthFakeFailures {
    var logout: Throwable? = null
}

class FakeAuthRepository : AuthRepository {
    val results = AuthFakeResults()
    val state = AuthFakeState()
    val calls = AuthFakeCalls()
    val failures = AuthFakeFailures()

    override suspend fun login(email: String, password: String): DataResult<UserState> {
        calls.login = LoginCall(email = email, password = password)
        return results.login
    }

    override suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
    ): DataResult<Unit> {
        calls.register =
            RegisterCall(
                firstName = firstName,
                lastName = lastName,
                email = email,
                password = password,
            )
        return results.register
    }

    override suspend fun googleLogin(idToken: String): DataResult<UserState> = results.googleLogin

    override suspend fun logout(): DataResult<Unit> {
        calls.logout++
        failures.logout?.let { throw it }
        return results.logout
    }

    override suspend fun selectRole(role: UserRole): DataResult<UserState> {
        calls.selectedRole = role
        return results.selectRole
    }

    override suspend fun currentUser(): DataResult<UserState> = results.currentUser

    override suspend fun resendVerification(email: String): DataResult<Unit> =
        results.resendVerification

    override suspend fun forgotPassword(email: String): DataResult<Unit> {
        calls.forgotPasswordEmail = email
        return results.forgotPassword
    }

    override suspend fun changePassword(
        currentPassword: String,
        newPassword: String,
    ): DataResult<Unit> {
        calls.changePassword =
            ChangePasswordCall(
                currentPassword = currentPassword,
                newPassword = newPassword,
            )
        return results.changePassword
    }

    override fun hasStoredSession(): Boolean = state.hasStoredSession

    override suspend fun clearLocalSession() {
        calls.clearLocalSession++
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
