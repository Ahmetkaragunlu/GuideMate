package com.ahmetkaragunlu.guidemate.domain.repository




import com.ahmetkaragunlu.guidemate.common.DataResult
import com.ahmetkaragunlu.guidemate.data.remote.model.request.ForgotPasswordRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.request.GoogleLoginRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.request.LoginRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.request.RegisterRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.request.ResetPasswordRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.request.RoleSelectionRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.response.AuthResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun register(request: RegisterRequest): DataResult<String>
    suspend fun login(request: LoginRequest): DataResult<AuthResponse>
    suspend fun googleLogin(request: GoogleLoginRequest): DataResult<AuthResponse>
    suspend fun logout(): DataResult<String>
    suspend fun refreshToken(): DataResult<AuthResponse>
    suspend fun selectRole(request: RoleSelectionRequest): DataResult<AuthResponse>
    suspend fun forgotPassword(request: ForgotPasswordRequest): DataResult<String>
    suspend fun resetPassword(request: ResetPasswordRequest): DataResult<String>

    val getUserName: Flow<String?>

    suspend fun saveUserName(name: String)}