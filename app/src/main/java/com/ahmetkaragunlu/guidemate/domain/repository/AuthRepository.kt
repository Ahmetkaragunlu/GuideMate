package com.ahmetkaragunlu.guidemate.domain.repository



import com.ahmetkaragunlu.guidemate.data.remote.model.*
import com.ahmetkaragunlu.guidemate.core.result.Result


interface AuthRepository {

    suspend fun register(request: RegisterRequest): Result<String>
    suspend fun login(request: LoginRequest): Result<AuthResponse>
    suspend fun googleLogin(request: GoogleLoginRequest): Result<AuthResponse>
    suspend fun logout(): Result<String>
    suspend fun refreshToken(): Result<AuthResponse>
    suspend fun selectRole(request: RoleSelectionRequest): Result<AuthResponse>
    suspend fun forgotPassword(request: ForgotPasswordRequest): Result<String>
    suspend fun resetPassword(request: ResetPasswordRequest): Result<String>
}