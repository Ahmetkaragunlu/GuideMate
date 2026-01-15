package com.ahmetkaragunlu.guidemate.data.remote.api

import com.ahmetkaragunlu.guidemate.data.remote.model.AuthResponse
import com.ahmetkaragunlu.guidemate.data.remote.model.ForgotPasswordRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.GoogleLoginRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.LoginRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.RefreshTokenRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.RegisterRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.ResetPasswordRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.RoleSelectionRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<ResponseBody>

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    @POST("google")
    suspend fun googleLogin(
        @Body request: GoogleLoginRequest
    ): Response<AuthResponse>

    @POST("refresh-token")
    suspend fun refreshToken(
        @Body request: RefreshTokenRequest
    ): Response<AuthResponse>

    @POST("logout")
    suspend fun logout(
        @Body request: RefreshTokenRequest
    ): Response<ResponseBody>

    @POST("select-role")
    suspend fun selectRole(
        @Body request: RoleSelectionRequest
    ): Response<AuthResponse>

    @POST("forgot-password")
    suspend fun forgotPassword(
        @Body request: ForgotPasswordRequest
    ): Response<ResponseBody>

    @POST("reset-password")
    suspend fun resetPassword(
        @Body request: ResetPasswordRequest
    ): Response<ResponseBody>

}