package com.ahmetkaragunlu.guidemate.data.remote.api


import com.ahmetkaragunlu.guidemate.data.remote.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface AuthApi {

    @POST("register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<String>

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
    ): Response<String>

    @POST("select-role")
    suspend fun selectRole(
        @Body request: RoleSelectionRequest
    ): Response<AuthResponse>

    @POST("forgot-password")
    suspend fun forgotPassword(
        @Body request: ForgotPasswordRequest
    ): Response<String>

    @POST("reset-password")
    suspend fun resetPassword(
        @Body request: ResetPasswordRequest
    ): Response<String>

    @GET("confirm")
    suspend fun confirmAccount(
        @Query("token") token: String
    ): Response<Unit>
}