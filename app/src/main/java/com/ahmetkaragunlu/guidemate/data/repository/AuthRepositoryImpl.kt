package com.ahmetkaragunlu.guidemate.data.repository

import com.ahmetkaragunlu.guidemate.R
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
        role = role,
        firstName = firstName,
        lastName = lastName
    )

    private fun UserRole.toRoleType() = when (this) {
        UserRole.TOURIST -> RoleType.ROLE_TOURIST
        UserRole.GUIDE -> RoleType.ROLE_GUIDE
        UserRole.ADMIN -> RoleType.ROLE_ADMIN
    }

    private fun Response<ResponseBody>.toDataResult(): DataResult<String> {
        return if (isSuccessful) {
            val bodyString = body()?.string()
            if (!bodyString.isNullOrBlank()) DataResult.Success(bodyString)
            else DataResult.Error(resourceProvider.getString(R.string.error_no_response_from_server))
        } else {
            val errorMsg = parseError(errorBody()?.string())
            DataResult.Error(errorMsg ?: resourceProvider.getString(R.string.error_generic_failure))
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
            ?: return DataResult.Error(resourceProvider.getString(R.string.error_session_expired))
        handleAuthResponse(api.refreshToken(RefreshTokenRequest(token)))
    } catch (e: Exception) {
        handleException(e)
    }

    override suspend fun selectRole(role: UserRole): DataResult<AuthResult> = try {
        val response = api.selectRole(RoleSelectionRequest(role.toRoleType()))
        if (response.isSuccessful && response.body() != null) {
            val body = response.body()!!
            body.role?.let { userRepository.saveUserRole(it) }
            DataResult.Success(body.toDomain())
        } else {
            val errorMsg = parseError(response.errorBody()?.string())
            DataResult.Error(errorMsg ?: resourceProvider.getString(R.string.error_generic_failure))
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
                role = body.role
            )
            DataResult.Success(body.toDomain())
        } else {
            val errorMsg = parseError(response.errorBody()?.string())
            DataResult.Error(errorMsg ?: resourceProvider.getString(R.string.error_generic_failure))
        }
    }

    private fun <T> handleException(e: Exception): DataResult<T> = when (e) {
        is IOException -> DataResult.Error(
            resourceProvider.getString(R.string.error_no_internet),
            e
        )

        is HttpException -> DataResult.Error(
            resourceProvider.getString(
                R.string.error_server,
                e.code()
            ), e
        )

        else -> DataResult.Error(resourceProvider.getString(R.string.error_unknown), e)
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