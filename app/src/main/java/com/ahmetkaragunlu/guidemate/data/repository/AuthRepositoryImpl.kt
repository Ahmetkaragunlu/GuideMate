package com.ahmetkaragunlu.guidemate.data.repository

import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.AppError
import com.ahmetkaragunlu.guidemate.common.DataResult
import com.ahmetkaragunlu.guidemate.common.ResourceProvider
import com.ahmetkaragunlu.guidemate.data.local.TokenManager
import com.ahmetkaragunlu.guidemate.data.remote.api.AuthApi
import com.ahmetkaragunlu.guidemate.data.remote.model.RoleType
import com.ahmetkaragunlu.guidemate.data.remote.model.request.ForgotPasswordRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.request.GoogleLoginRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.request.LoginRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.request.RefreshTokenRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.request.RegisterRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.request.ResetPasswordRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.request.RoleSelectionRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.response.AuthResponse
import com.ahmetkaragunlu.guidemate.data.remote.model.response.ErrorResponse
import com.ahmetkaragunlu.guidemate.domain.repository.AuthRepository
import com.ahmetkaragunlu.guidemate.domain.repository.UserRepository
import com.ahmetkaragunlu.guidemate.domain.model.AuthResult
import com.ahmetkaragunlu.guidemate.domain.model.UserRole
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val tokenManager: TokenManager,
    private val resourceProvider: ResourceProvider,
    private val userRepository: UserRepository
) : AuthRepository {

    // --- Mappers ---
    private fun AuthResponse.toDomain() = AuthResult(
        accessToken = accessToken,
        refreshToken = refreshToken,
        isRoleSelected = isRoleSelected,
        role = role?.toUserRole(),
        firstName = firstName,
        lastName = lastName,
    )

    private fun UserRole.toRoleType() = when (this) {
        UserRole.TOURIST -> RoleType.ROLE_TOURIST
        UserRole.GUIDE -> RoleType.ROLE_GUIDE
        UserRole.ADMIN -> RoleType.ROLE_ADMIN
    }

    private fun RoleType.toUserRole() = when (this) {
        RoleType.ROLE_TOURIST -> UserRole.TOURIST
        RoleType.ROLE_GUIDE -> UserRole.GUIDE
        RoleType.ROLE_ADMIN -> UserRole.ADMIN
    }

    private fun Response<ResponseBody>.toDataResult(): DataResult<String> {
        return if (isSuccessful) {
            val bodyString = body()?.string()
            if (!bodyString.isNullOrBlank()) DataResult.Success(bodyString)
            else DataResult.Error(AppError.NoResponseFromServer)
        } else {
            val errorMsg = parseError(errorBody()?.string())
            DataResult.Error(errorMsg?.let(AppError::Backend) ?: AppError.GenericFailure)
        }
    }

    // --- Implementations ---
    override suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): DataResult<String> = try {
        api.register(RegisterRequest(firstName, lastName, email, password)).toDataResult()
    } catch (e: Exception) {
        handleException(e)
    }

    override suspend fun login(email: String, password: String): DataResult<AuthResult> = try {
        handleAuthResponse(api.login(LoginRequest(email, password)))
    } catch (e: Exception) {
        handleException(e)
    }

    override suspend fun googleLogin(idToken: String): DataResult<AuthResult> = try {
        handleAuthResponse(api.googleLogin(GoogleLoginRequest(idToken)))
    } catch (e: Exception) {
        handleException(e)
    }

    override suspend fun logout(): DataResult<String> {
        return try {
            val refreshToken = tokenManager.getRefreshToken()
            if (!refreshToken.isNullOrEmpty()) {
                val response = api.logout(RefreshTokenRequest(refreshToken))
                clearLocalData()
                return response.toDataResult()
            }
            clearLocalData()
            DataResult.Success(resourceProvider.getString(R.string.logout_success))
        } catch (e: Exception) {
            clearLocalData()
            DataResult.Success(resourceProvider.getString(R.string.logout_offline))
        }
    }

    override suspend fun refreshToken(): DataResult<AuthResult> = try {
        val token = tokenManager.getRefreshToken()
            ?: return DataResult.Error(AppError.SessionExpired)
        handleAuthResponse(api.refreshToken(RefreshTokenRequest(token)))
    } catch (e: Exception) {
        handleException(e)
    }

    override suspend fun selectRole(role: UserRole): DataResult<AuthResult> = try {
        val response = api.selectRole(RoleSelectionRequest(role.toRoleType()))
        if (response.isSuccessful && response.body() != null) {
            val body = response.body()!!
            body.role?.toUserRole()?.let(userRepository::saveUserRole)
            DataResult.Success(body.toDomain())
        } else {
            val errorMsg = parseError(response.errorBody()?.string())
            DataResult.Error(errorMsg?.let(AppError::Backend) ?: AppError.GenericFailure)
        }
    } catch (e: Exception) {
        handleException(e)
    }

    override suspend fun forgotPassword(
        email: String,
        firstName: String,
        lastName: String
    ): DataResult<String> = try {
        api.forgotPassword(ForgotPasswordRequest(email, firstName, lastName)).toDataResult()
    } catch (e: Exception) {
        handleException(e)
    }

    override suspend fun resetPassword(
        token: String,
        newPassword: String,
        confirmPassword: String
    ): DataResult<String> = try {
        api.resetPassword(ResetPasswordRequest(token, newPassword, confirmPassword)).toDataResult()
    } catch (e: Exception) {
        handleException(e)
    }

    private fun clearLocalData() {
        tokenManager.clearTokens()
        userRepository.clearUser()
    }

    private suspend fun handleAuthResponse(response: Response<AuthResponse>): DataResult<AuthResult> {
        return if (response.isSuccessful && response.body() != null) {
            val body = response.body()!!
            body.accessToken?.let { tokenManager.saveAccessToken(it) }
            body.refreshToken?.let { tokenManager.saveRefreshToken(it) }

            val firstName = body.firstName.orEmpty().trim()
            val lastName = body.lastName.orEmpty().trim()
            userRepository.saveUser(
                firstName = firstName.ifEmpty { null },
                lastName = lastName.ifEmpty { null },
                role = body.role?.toUserRole(),
            )
            DataResult.Success(body.toDomain())
        } else {
            val errorMsg = parseError(response.errorBody()?.string())
            DataResult.Error(errorMsg?.let(AppError::Backend) ?: AppError.GenericFailure)
        }
    }

    private fun <T> handleException(e: Exception): DataResult<T> = when (e) {
        is IOException -> DataResult.Error(AppError.NoInternet, e)

        is HttpException -> DataResult.Error(AppError.Server(e.code()), e)

        else -> DataResult.Error(AppError.Unknown, e)
    }

    private fun parseError(json: String?): String? {
        if (json.isNullOrEmpty()) return null
        return try {
            Gson().fromJson(json, ErrorResponse::class.java).message
        } catch (e: Exception) {
            null
        }
    }
}
