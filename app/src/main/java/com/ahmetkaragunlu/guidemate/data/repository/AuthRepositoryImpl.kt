package com.ahmetkaragunlu.guidemate.data.repository


import android.content.Context
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.data.local.TokenManager
import com.ahmetkaragunlu.guidemate.data.remote.api.AuthApi
import com.ahmetkaragunlu.guidemate.data.remote.model.*
import com.ahmetkaragunlu.guidemate.domain.repository.AuthRepository
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import com.ahmetkaragunlu.guidemate.core.result.Result

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val tokenManager: TokenManager,
    @param:ApplicationContext private val context: Context
) : AuthRepository {


    override suspend fun register(request: RegisterRequest):Result<String> {
        return try {
            val response = api.register(request)
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                val errorMsg = parseError(response.errorBody()?.string())
                Result.Error(errorMsg ?: context.getString(R.string.error_generic_failure))
            }
        } catch (e: Exception) {
            handleException(e)
        }
    }

    override suspend fun login(request: LoginRequest): Result<AuthResponse> {
        return try {
            val response = api.login(request)
            handleAuthResponse(response)
        } catch (e: Exception) {
            handleException(e)
        }
    }

    override suspend fun googleLogin(request: GoogleLoginRequest): Result<AuthResponse> {
        return try {
            val response = api.googleLogin(request)
            handleAuthResponse(response)
        } catch (e: Exception) {
            handleException(e)
        }
    }


    override suspend fun logout(): Result<String> {
        return try {
            val refreshToken = tokenManager.getRefreshToken()
            if (!refreshToken.isNullOrEmpty()) {
                api.logout(RefreshTokenRequest(refreshToken))
            }
            tokenManager.clearTokens()
            Result.Success(context.getString(R.string.logout_success))
        } catch (e: Exception) {
            tokenManager.clearTokens()
            Result.Success(context.getString(R.string.logout_offline))
        }
    }
    override suspend fun refreshToken(): Result<AuthResponse> {
        return try {
            val refreshToken = tokenManager.getRefreshToken()
                ?: return Result.Error(context.getString(R.string.error_session_expired))
            val response = api.refreshToken(RefreshTokenRequest(refreshToken))
            handleAuthResponse(response)
        } catch (e: Exception) {
            handleException(e)
        }
    }


    override suspend fun selectRole(request: RoleSelectionRequest): Result<AuthResponse> {
        return try {
            val response = api.selectRole(request)
            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!
                authResponse.role?.let { tokenManager.saveUserRole(it) }
                Result.Success(authResponse)
            } else {
                val errorMsg = parseError(response.errorBody()?.string())
                Result.Error(errorMsg ?: context.getString(R.string.error_generic_failure))
            }
        } catch (e: Exception) {
            handleException(e)
        }
    }

    override suspend fun forgotPassword(request: ForgotPasswordRequest): Result<String> {
        return try {
            val response = api.forgotPassword(request)
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                val errorMsg = parseError(response.errorBody()?.string())
                Result.Error(errorMsg ?: context.getString(R.string.error_generic_failure))
            }
        } catch (e: Exception) {
            handleException(e)
        }
    }


    override suspend fun resetPassword(request: ResetPasswordRequest): Result<String> {
        return try {
            val response = api.resetPassword(request)
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                val errorMsg = parseError(response.errorBody()?.string())
                Result.Error(errorMsg ?: context.getString(R.string.error_generic_failure))
            }
        } catch (e: Exception) {
            handleException(e)
        }
    }

    private fun handleAuthResponse(response: retrofit2.Response<AuthResponse>): Result<AuthResponse> {
        return if (response.isSuccessful && response.body() != null) {
            val authResponse = response.body()!!
            authResponse.accessToken?.let { tokenManager.saveAccessToken(it) }
            authResponse.refreshToken?.let { tokenManager.saveRefreshToken(it) }
            authResponse.role?.let { tokenManager.saveUserRole(it) }
            Result.Success(authResponse)
        } else {
            val errorMsg = parseError(response.errorBody()?.string())
            Result.Error(errorMsg ?: context.getString(R.string.error_generic_failure))
        }
    }

    private fun <T> handleException(e: Exception): Result<T> {
        return when (e) {
            is IOException -> Result.Error(context.getString(R.string.error_no_internet), e)
            is HttpException -> Result.Error(context.getString(R.string.error_server, e.code()), e)
            else -> Result.Error(context.getString(R.string.error_unknown), e)
        }
    }

    private fun parseError(json: String?): String? {
        if (json.isNullOrEmpty()) return null
        return try {
            val errorResponse = Gson().fromJson(json, ErrorResponse::class.java)
            errorResponse.message
        } catch (e: Exception) {
            null
        }
    }
}