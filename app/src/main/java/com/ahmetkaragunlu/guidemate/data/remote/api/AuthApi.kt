package com.ahmetkaragunlu.guidemate.data.remote.api


import com.ahmetkaragunlu.guidemate.data.remote.model.request.ForgotPasswordRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.request.GoogleLoginRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.request.LoginRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.request.RefreshTokenRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.request.RegisterRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.request.ResetPasswordRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.request.RoleSelectionRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.response.AuthResponse
import retrofit2.Call
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

    @POST("refresh-token")
    fun refreshTokenSync(@Body request: RefreshTokenRequest): Call<AuthResponse>

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