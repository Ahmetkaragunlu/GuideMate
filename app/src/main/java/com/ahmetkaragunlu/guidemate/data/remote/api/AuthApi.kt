package com.ahmetkaragunlu.guidemate.data.remote.api

import com.ahmetkaragunlu.guidemate.data.remote.model.request.ChangePasswordRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.request.ForgotPasswordRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.request.GoogleLoginRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.request.LoginRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.request.RefreshTokenRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.request.RegisterRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.request.ResendVerificationRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.request.RoleSelectionRequest
import com.ahmetkaragunlu.guidemate.data.remote.model.response.AuthResponse
import com.ahmetkaragunlu.guidemate.data.remote.model.response.CurrentUserResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    @POST("api/v1/auth/register")
    suspend fun register(
        @Body request: RegisterRequest,
    ): Response<ResponseBody>

    @POST("api/v1/auth/login")
    suspend fun login(
        @Body request: LoginRequest,
        @Header(INSTALLATION_HEADER) installationId: String,
    ): Response<AuthResponse>

    @POST("api/v1/auth/google")
    suspend fun googleLogin(
        @Body request: GoogleLoginRequest,
        @Header(INSTALLATION_HEADER) installationId: String,
    ): Response<AuthResponse>

    @POST("api/v1/auth/refresh-token")
    fun refreshTokenSync(
        @Body request: RefreshTokenRequest,
        @Header(INSTALLATION_HEADER) installationId: String,
    ): Call<AuthResponse>

    @POST("api/v1/auth/logout")
    suspend fun logout(
        @Body request: RefreshTokenRequest,
        @Header(INSTALLATION_HEADER) installationId: String,
        @Header(AUTHORIZATION_HEADER) authorization: String,
    ): Response<ResponseBody>

    @POST("api/v1/auth/select-role")
    suspend fun selectRole(
        @Body request: RoleSelectionRequest,
    ): Response<AuthResponse>

    @GET("api/v1/auth/me")
    suspend fun currentUser(): Response<CurrentUserResponse>

    @POST("api/v1/auth/resend-verification")
    suspend fun resendVerification(
        @Body request: ResendVerificationRequest,
    ): Response<ResponseBody>

    @POST("api/v1/auth/forgot-password")
    suspend fun forgotPassword(
        @Body request: ForgotPasswordRequest,
    ): Response<ResponseBody>

    @POST("api/v1/auth/change-password")
    suspend fun changePassword(
        @Body request: ChangePasswordRequest,
    ): Response<ResponseBody>

    private companion object {
        const val INSTALLATION_HEADER = "X-Installation-Id"
        const val AUTHORIZATION_HEADER = "Authorization"
    }
}
