package com.ahmetkaragunlu.guidemate.data.repository


import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.common.DataResult
import com.ahmetkaragunlu.guidemate.common.ResourceProvider
import com.ahmetkaragunlu.guidemate.data.local.TokenManager
import com.ahmetkaragunlu.guidemate.data.remote.api.AuthApi
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
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val tokenManager: TokenManager,
    private val resourceProvider: ResourceProvider
) : AuthRepository {

    private fun Response<ResponseBody>.toDataResult(): DataResult<String> {
        return if (isSuccessful) {
            val bodyString = body()?.string()
            if (!bodyString.isNullOrBlank()) {
                DataResult.Success(bodyString)
            } else {
                DataResult.Error(resourceProvider.getString(R.string.error_no_response_from_server))
            }
        } else {
            val errorMsg = parseError(errorBody()?.string())
            DataResult.Error(errorMsg ?: resourceProvider.getString(R.string.error_generic_failure))
        }
    }

    override suspend fun register(request: RegisterRequest): DataResult<String> {
        return try {
            val response = api.register(request)
            response.toDataResult()
        } catch (e: Exception) {
            handleException(e)
        }
    }

    override suspend fun login(request: LoginRequest): DataResult<AuthResponse> {
        return try {
            val response = api.login(request)
            handleAuthResponse(response)
        } catch (e: Exception) {
            handleException(e)
        }
    }

    override suspend fun googleLogin(request: GoogleLoginRequest): DataResult<AuthResponse> {
        return try {
            val response = api.googleLogin(request)
            handleAuthResponse(response)
        } catch (e: Exception) {
            handleException(e)
        }
    }

    override suspend fun logout(): DataResult<String> {
        return try {
            val refreshToken = tokenManager.getRefreshToken()
            if (!refreshToken.isNullOrEmpty()) {
                val response = api.logout(RefreshTokenRequest(refreshToken))
                tokenManager.clearTokens()
                return response.toDataResult()
            }
            tokenManager.clearTokens()
            DataResult.Success(resourceProvider.getString(R.string.logout_success))
        } catch (e: Exception) {
            tokenManager.clearTokens()
            DataResult.Success(resourceProvider.getString(R.string.logout_offline))
        }
    }

    override suspend fun refreshToken(): DataResult<AuthResponse> {
        return try {
            val refreshToken = tokenManager.getRefreshToken()
                ?: return DataResult.Error(resourceProvider.getString(R.string.error_session_expired))
            val response = api.refreshToken(RefreshTokenRequest(refreshToken))
            handleAuthResponse(response)
        } catch (e: Exception) {
            handleException(e)
        }
    }

    override suspend fun selectRole(request: RoleSelectionRequest): DataResult<AuthResponse> {
        return try {
            val response = api.selectRole(request)
            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!
                authResponse.role?.let { tokenManager.saveUserRole(it) }
                DataResult.Success(authResponse)
            } else {
                val errorMsg = parseError(response.errorBody()?.string())
                DataResult.Error(errorMsg ?: resourceProvider.getString(R.string.error_generic_failure))
            }
        } catch (e: Exception) {
            handleException(e)
        }
    }

    override suspend fun forgotPassword(request: ForgotPasswordRequest): DataResult<String> {
        return try {
            val response = api.forgotPassword(request)
            response.toDataResult()
        } catch (e: Exception) {
            handleException(e)
        }
    }

    override suspend fun resetPassword(request: ResetPasswordRequest): DataResult<String> {
        return try {
            val response = api.resetPassword(request)
            response.toDataResult()
        } catch (e: Exception) {
            handleException(e)
        }
    }

    private fun handleAuthResponse(response: Response<AuthResponse>): DataResult<AuthResponse> {
        return if (response.isSuccessful && response.body() != null) {
            val authResponse = response.body()!!
            authResponse.accessToken?.let { tokenManager.saveAccessToken(it) }
            authResponse.refreshToken?.let { tokenManager.saveRefreshToken(it) }
            authResponse.role?.let { tokenManager.saveUserRole(it) }
            DataResult.Success(authResponse)
        } else {
            val errorMsg = parseError(response.errorBody()?.string())
            DataResult.Error(errorMsg ?: resourceProvider.getString(R.string.error_generic_failure))
        }
    }

    private fun <T> handleException(e: Exception): DataResult<T> {
        return when (e) {
            is IOException -> DataResult.Error(resourceProvider.getString(R.string.error_no_internet), e)
            is HttpException -> DataResult.Error(resourceProvider.getString(R.string.error_server, e.code()), e)
            else -> DataResult.Error(resourceProvider.getString(R.string.error_unknown), e)
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