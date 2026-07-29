package com.ahmetkaragunlu.guidemate.data.repository

import com.ahmetkaragunlu.guidemate.common.AppError
import com.ahmetkaragunlu.guidemate.common.DataResult
import com.ahmetkaragunlu.guidemate.common.isTerminalSessionError
import com.ahmetkaragunlu.guidemate.data.local.AuthSessionManager
import com.ahmetkaragunlu.guidemate.data.local.CredentialSessionManager
import com.ahmetkaragunlu.guidemate.data.local.TokenManager
import com.ahmetkaragunlu.guidemate.data.local.preferences.AppPreferencesDataSource
import com.ahmetkaragunlu.guidemate.data.mapper.toDomain
import com.ahmetkaragunlu.guidemate.data.mapper.toNetwork
import com.ahmetkaragunlu.guidemate.data.remote.api.AuthApi
import com.ahmetkaragunlu.guidemate.data.remote.error.ApiErrorParser
import com.ahmetkaragunlu.guidemate.data.remote.error.TokenRefreshException
import com.ahmetkaragunlu.guidemate.data.remote.model.request.ChangePasswordRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.request.ForgotPasswordRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.request.GoogleLoginRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.request.LoginRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.request.RefreshTokenRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.request.RegisterRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.request.ResendVerificationRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.request.RoleSelectionRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.response.AuthResponse
import com.ahmetkaragunlu.guidemate.domain.model.UserRole
import com.ahmetkaragunlu.guidemate.domain.model.UserState
import com.ahmetkaragunlu.guidemate.domain.repository.AuthRepository
import com.ahmetkaragunlu.guidemate.domain.validation.EmailPolicy
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val tokenManager: TokenManager,
    private val preferences: AppPreferencesDataSource,
    private val authSessionManager: AuthSessionManager,
    private val credentialSessionManager: CredentialSessionManager,
    private val apiErrorParser: ApiErrorParser,
    private val emailPolicy: EmailPolicy,
) : AuthRepository {
    override suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
    ): DataResult<Unit> =
        execute {
            api
                .register(
                    RegisterRequest(
                        firstName = firstName.trim(),
                        lastName = lastName.trim(),
                        email = emailPolicy.normalize(email),
                        password = password,
                    ),
                ).toUnitResult()
        }

    override suspend fun login(
        email: String,
        password: String,
    ): DataResult<UserState> =
        execute {
            api
                .login(
                    request = LoginRequest(emailPolicy.normalize(email), password),
                    installationId = preferences.getOrCreateInstallationId(),
                ).toSessionResult(requireRefreshToken = true)
        }

    override suspend fun googleLogin(idToken: String): DataResult<UserState> =
        execute {
            api
                .googleLogin(
                    request = GoogleLoginRequest(idToken),
                    installationId = preferences.getOrCreateInstallationId(),
                ).toSessionResult(requireRefreshToken = true)
        }

    override suspend fun logout(): DataResult<Unit> {
        val accessToken = tokenManager.getAccessToken()
        val refreshToken = tokenManager.getRefreshToken()
        val installationId =
            runCatching {
                preferences.getOrCreateInstallationId()
            }.getOrNull()
        authSessionManager.clearSession()
        credentialSessionManager.clear()

        if (accessToken.isNullOrBlank() || refreshToken.isNullOrBlank() || installationId == null) {
            return DataResult.Success(Unit)
        }

        return try {
            api
                .logout(
                    request = RefreshTokenRequest(refreshToken),
                    installationId = installationId,
                    authorization = "Bearer $accessToken",
                ).toUnitResult()
        } catch (exception: CancellationException) {
            throw exception
        } catch (_: Exception) {
            DataResult.Success(Unit)
        }
    }

    override suspend fun selectRole(role: UserRole): DataResult<UserState> =
        execute {
            api
                .selectRole(RoleSelectionRequest(role.toNetwork()))
                .toSessionResult(requireRefreshToken = false)
        }

    override suspend fun currentUser(): DataResult<UserState> =
        execute {
            val response = api.currentUser()
            if (response.isSuccessful) {
                val user =
                    response.body()?.toDomain()
                        ?: return@execute DataResult.Error(AppError.NoResponseFromServer)
                authSessionManager.saveUser(user)
                DataResult.Success(user)
            } else {
                response.toErrorResult()
            }
        }

    override suspend fun resendVerification(email: String): DataResult<Unit> =
        execute {
            api
                .resendVerification(ResendVerificationRequest(emailPolicy.normalize(email)))
                .toUnitResult()
        }

    override suspend fun forgotPassword(email: String): DataResult<Unit> =
        execute {
            api
                .forgotPassword(ForgotPasswordRequest(emailPolicy.normalize(email)))
                .toUnitResult()
        }

    override suspend fun changePassword(
        currentPassword: String,
        newPassword: String,
    ): DataResult<Unit> =
        execute {
            api
                .changePassword(
                    ChangePasswordRequest(
                        currentPassword = currentPassword,
                        newPassword = newPassword,
                    ),
                ).toUnitResult()
        }

    override fun hasStoredSession(): Boolean = authSessionManager.hasStoredSession()

    override suspend fun clearLocalSession() {
        authSessionManager.clearSession()
        credentialSessionManager.clear()
    }

    private suspend fun Response<AuthResponse>.toSessionResult(
        requireRefreshToken: Boolean,
    ): DataResult<UserState> {
        if (!isSuccessful) return toErrorResult()

        val response = body() ?: return DataResult.Error(AppError.NoResponseFromServer)
        val accessToken = response.accessToken
        if (accessToken.isNullOrBlank() || (requireRefreshToken && response.refreshToken.isNullOrBlank())) {
            return DataResult.Error(AppError.NoResponseFromServer)
        }

        val result = response.toDomain()
        authSessionManager.saveSession(
            accessToken = accessToken,
            refreshToken = response.refreshToken,
            userState = result,
        )
        return DataResult.Success(result)
    }

    private suspend fun Response<ResponseBody>.toUnitResult(): DataResult<Unit> =
        if (isSuccessful) {
            DataResult.Success(Unit)
        } else {
            toErrorResult()
        }

    private suspend fun <T> Response<*>.toErrorResult(): DataResult<T> {
        val error = apiErrorParser.parse(this)
        if (error.isTerminalSessionError()) {
            authSessionManager.clearSession()
        }
        return DataResult.Error(error)
    }

    private suspend fun <T> execute(block: suspend () -> DataResult<T>): DataResult<T> =
        try {
            block()
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            when (exception) {
                is TokenRefreshException -> DataResult.Error(exception.error, exception)
                is ConnectException,
                is SocketTimeoutException,
                -> DataResult.Error(AppError.NoResponseFromServer, exception)
                is UnknownHostException -> DataResult.Error(AppError.NoInternet, exception)
                is IOException -> DataResult.Error(AppError.NoInternet, exception)
                is HttpException -> DataResult.Error(AppError.Server(exception.code()), exception)
                else -> DataResult.Error(AppError.Unknown, exception)
            }
        }
}
